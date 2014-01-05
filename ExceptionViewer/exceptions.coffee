# An example Backbone application contributed by
# [Jérôme Gravel-Niquet](http://jgn.me/). This demo uses
# [Parse.com](http://parse.com)
# to persist Backbone models.
# 
# This [CoffeeScript](http://jashkenas.github.com/coffee-script/) variation has been provided by [Jason Giedymin](http://jasongiedymin.com/).
#
# This [Parse.com](http://parse.com) variation has been provided by [Eric Butler](http://codebutler.com).
#
# Note: two things you will notice with my CoffeeScript are that I prefer to
# use four space indents and prefer to use `()` for all functions.

# Load the application once the DOM is ready, using a `jQuery.ready` shortcut.
$ ->    
    # Set the Parse.com Application ID and REST API Key.
    Parse.initialize 'ORL3ZTS0YA4r0nCNRZ4G7C6pXswluZN0axqAFUAx', '4GVt9YvmNsEc6NgeHsUQf065IHxlX5bT9kfLEBz8'

    ### Exception Model ###

    # Our basic **Exception** model has `message`, `order`, and `done` attributes.
    class Exception extends ParseObject
        parseClassName: 'Exception'

        # Default attributes for the exception.
        defaults:
            versionCode: 0
            message: "empty exception..."
            done: false

        # Ensure that each exception created has `message`.
        initialize: ->
            if !@get("message")
                @set({ "message": @defaults.message })

            if !@get("versionCode")
                @set({ "versionCode": @defaults.versionCode })

        # Toggle the `done` state of this exception item.
        toggle: ->
            @save({ done: !@get("done") })

        # Remove this Exception from the Parse.com database and delete its view.
        clear: ->
            @destroy()
            @view.remove()

    ### Exception Collection ###

    # The collection of exceptions is backed by a Parse.com application.
    class ExceptionList extends ParseCollection

        # Reference to this collection's model.
        model: Exception

        # Attribute getter/setter
        getDone = (exception) ->
            return exception.get("done")

        # Filter down the list of all exception items that are finished.
        done: ->
            return @filter( getDone )

        # Filter down the list to only exception items that are still not finished.
        remaining: ->
            return @without.apply( this, @done() )

        # We keep the Exceptions in sequential order, despite being saved by unordered
        # GUID in the database. This generates the next order number for new items.
        nextOrder: ->
            return 1 if !@length
            return @last().get('order') + 1

        # Exceptions are sorted by their original insertion order.
        comparator: (exception) ->
            return exception.get("order")

    ### Exception Item View ###

    # The DOM element for a exception item...
    class ExceptionView extends Backbone.View

        #... is a list tag.
        tagName:  "li"

        # Cache the template function for a single item.
        template: _.template( $("#item-template").html() )

        # The DOM events specific to an item.
        events:
            "click .check"              : "toggleDone",
            "dblclick div.exception-message" : "edit",
            "click span.exception-destroy"   : "clear",
            "keypress .exception-input"      : "updateOnEnter"

        # The ExceptionView listens for changes to its model, re-rendering. Since there's
        # a one-to-one correspondence between a **Exception** and a **ExceptionView** in this
        # app, we set a direct reference on the model for convenience.
        initialize: ->
            @model.bind('change', this.render);
            @model.view = this;

        # Re-render the contents of the exception item.
        render: =>
            this.$(@el).html( @template(@model.toJSON()) )
            @setContent()
            return this

        # To avoid XSS (not that it would be harmful in this particular app),
        # we use `jQuery.text` to set the contents of the exception item.
        setContent: ->
            message = @model.get("message")
            this.$(".exception-message").text(message)
            @input = this.$(".exception-input");
            @input.bind("blur", @close);
            @input.val(message);

        # Toggle the `"done"` state of the model.
        toggleDone: ->
            @model.toggle()

        # Switch this view into `"editing"` mode, displaying the input field.
        edit: =>
            this.$(@el).addClass("editing")
            @input.focus()

        # Close the `"editing"` mode, saving changes to the exception.
        close: =>
            @model.save({ message: @input.val() })
            $(@el).removeClass("editing")

        # If you hit `enter`, we're through editing the item.
        updateOnEnter: (e) =>
            @close() if e.keyCode is 13

        # Remove this view from the DOM.
        remove: ->
            $(@el).remove()

        # Remove the item, destroy the model.
        clear: () ->
            @model.clear()

    ### The Application ###

    # Our overall **AppView** is the top-level piece of UI.
    class AppView extends Backbone.View
        # Instead of generating a new element, bind to the existing skeleton of
        # the App already present in the HTML.
        el_tag = "#exceptionapp"
        el: $(el_tag)

        # Our template for the line of statistics at the bottom of the app.
        statsTemplate: _.template( $("#stats-template").html() )

        # Delegated events for creating new items, and clearing completed ones.
        events:
            "keypress #new-exception"  : "createOnEnter",
            "keyup #new-exception"     : "showTooltip",
            "click .exception-clear a" : "clearCompleted"

        # At initialization we bind to the relevant events on the `Exceptions`
        # collection, when items are added or changed. Kick things off by
        # loading any preexisting exceptions that might be saved at Parse.com.
        initialize: =>
            @input = this.$("#new-exception")

            Exceptions.bind("add", @addOne)
            Exceptions.bind("reset", @addAll)
            Exceptions.bind("all", @render)

            Exceptions.fetch()

        # Re-rendering the App just means refreshing the statistics -- the rest
        # of the app doesn't change.
        render: =>
            this.$('#exception-stats').html( @statsTemplate({
                total:      Exceptions.length,
                done:       Exceptions.done().length,
                remaining:  Exceptions.remaining().length
            }))

        # Add a single exception item to the list by creating a view for it, and
        # appending its element to the `<ul>`.
        addOne: (exception) =>
            view = new ExceptionView( {model: exception} )
            this.$("#exception-list").append( view.render().el )

        # Add all items in the **Exceptions** collection at once.
        addAll: =>
            Exceptions.each(@addOne);

        # Generate the attributes for a new Exception item.
        newAttributes: ->
            return {
                message: @input.val(),
                order:   Exceptions.nextOrder(),
                done:    false
            }

        # If you hit return in the main input field, create new **Exception** model,
        # persisting it to Parse.com.
        createOnEnter: (e) ->
            return if (e.keyCode != 13)
            Exceptions.create( @newAttributes() )
            @input.val('')

        # Clear all done exception items, destroying their models.
        clearCompleted: ->
            _.each(Exceptions.done(), (exception) ->
                exception.clear()
            )
            return false

        # Lazily show the tooltip that tells you to press `enter` to save
        # a new exception item, after one second.
        showTooltip: (e) ->
            tooltip = this.$(".ui-tooltip-top")
            val = @input.val()
            tooltip.fadeOut()
            clearTimeout(@tooltipTimeout) if (@tooltipTimeout)
            return if (val is '' || val is @input.attr("placeholder"))
            
            show = () ->
                tooltip.show().fadeIn()
            @tooltipTimeout = _.delay(show, 1000)

    # Create our global collection of **Exceptions**.
    # Note: I've actually chosen not to export globally to `window`.
    # Original documentation has been left intact.
    Exceptions = new ExceptionList
    App = new AppView()

