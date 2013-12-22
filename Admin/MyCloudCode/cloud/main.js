Parse.Cloud.define("copyInventory", function(request, response) {
  var query = new Parse.Query("Inventory");
  query.equalTo("objectId", request.params.objectId);
  query.first({
    success: function(result) {

     var ins = new Parse.Object("Inventory");
      ins.set("desc", result.get("desc"));
     ins.save(null, {
       success: function(ins) {
         // Execute any logic that should take place after the object is saved.
         response.success(ins);
       },
       error: function(ins, error) {
         // Execute any logic that should take place if the save fails.
         // error is a Parse.Error with an error code and description.
         response.error('Failed to create new object, with error code: ' + error.description);
       }
     });
    },
    error: function() {
      response.error("movie lookup failed");
    }
  });
});