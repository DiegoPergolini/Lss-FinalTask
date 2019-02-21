/*
 * createDataFields
 */
exports.createdatafields = function (model) {
	  //console.log( model  ); 
 	  createDefaultData(model.links.properties.resources);
	  createDefaultData(model.links.actions.resources);
}; //create



function createDefaultData(resources) {
  Object.keys(resources).forEach(function (resKey) {
     console.log("createDataFields finds: "+ resKey);
	 var resource  = resources[resKey];
	 resource.data = [];
  });
}//createDefaultData


 

 
