# Basic Parse.com integration for backbone.
#
# Authors:
#   Eric Butler <eric@codebutler.com>
#
# Permission is hereby granted, free of charge, to any person obtaining
# a copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so, subject to
# the following conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
# LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

BASE_URL = "https://api.parse.com/1/classes"

Parse =
  initialize: (applicationId, restKey) ->
    @applicationId = applicationId
    @restKey       = restKey

class ParseObject extends Backbone.Model
  idAttribute: 'objectId'
  parseClassName: null
   
  urlRoot: ->
    BASE_URL + "/#{@parseClassName}"

  toJSON: ->
    _.tap super(), (attrs) ->
      delete attrs.createdAt
      delete attrs.updatedAt

class ParseCollection extends Backbone.Collection
  parse: (response) ->
    super(response).results

  url: ->
    BASE_URL + "/#{@model.name}"

class ParseUser extends ParseObject
  parseClassName: 'ParseUser'

class ParseUserList extends ParseCollection
  model: ParseUser

methodMap =
  create: 'POST'
  update: 'PUT'
  delete: 'DELETE'
  read:   'GET'

Backbone.ajaxSync = Backbone.sync
Backbone.sync = (method, model, options) ->
  isParse = (model instanceof ParseObject || model instanceof ParseCollection)
  return Backbone.ajaxSync(method, model, options) unless isParse

  if method == 'read' && options.query
    data = encodeURI("where=#{JSON.stringify(options.query)}")
  else if method == 'create' || method == 'update'
    data = JSON.stringify(model.toJSON())

  params =
    contentType: 'application/json'
    dataType:    'json'
    data:        data
    url:         model.url()
    type:        methodMap[method]
    headers:
      'X-Parse-Application-Id': Parse.applicationId
      'X-Parse-REST-API-Key':   Parse.restKey

  params.processData = false if (params.type != 'GET' && !Backbone.emulateJSON)
  
  $.ajax _.extend(options, params)
  
window.Parse           = Parse
window.ParseCollection = ParseCollection
window.ParseObject     = ParseObject
window.ParseUser       = ParseUser
window.ParseUserList   = ParseUserList
