Parse.Cloud.define("copyInventory", function(request, response) {

  var Inventory = Parse.Object.extend("Inventory");

  var query = new Parse.Query("Inventory");

  var invObjectId = request.params.objectId;

  query.equalTo("objectId", invObjectId);
  query.first().then(function(inv) {

        inv.clone().save().then(function(cloneInv){

           var dummyInv = new Inventory();
           dummyInv.id = invObjectId;

           var query = new Parse.Query("Room");
           query.equalTo("inventory", dummyInv);
           query.find().then(function(rooms){

                    var newRooms = [];
                  for(var i=0; i<rooms.length;++i){
                    var room = rooms[i];
                   var newRoom = room.clone();
                     newRoom.set("inventory", cloneInv);
                     newRooms.push(newRoom);
                  }

                  Parse.Object.saveAll(newRooms, function(list, error) {
                                                     if (list) {
                                                       response.success("Hello");
                                                     } else {
                                                      response.error(error);
                                                     }
                                                   });

           });
  });

  });


  });
