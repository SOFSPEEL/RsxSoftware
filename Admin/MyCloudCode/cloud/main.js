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
                var newRoom = rooms[i].clone(i);
                 newRoom.set("inventory", cloneInv);
                 newRoom.save();
                  if (i==0)response.success(newRoom.id);
              }
    
    
       });
    });
    
    });
    
    
    });
