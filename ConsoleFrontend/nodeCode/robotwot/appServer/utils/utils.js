var model = require('./../models/robotModel');
 
exports.randomInt = function(low, high) {
  return Math.floor(Math.random() * (high - low + 1) + low);
};

exports.findObjectInArray = function(array, filterObj) {
  //TODO: should be made async (what if array is big!)
  return _.find(array, filterObj);
};

exports.findProperty = function(propertyId){
  return model.links.properties.resources[propertyId];
};

exports.isoTimestamp = function(){
  var date = new Date();
  return date.toISOString();
};

exports.extractFields = function(fields, object, target) {
  if(!target) var target = {};
  var arrayLength = fields.length;
  for (var i = 0; i < arrayLength; i++) {
    var field = fields[i];
    target[field] = object[field];
  }
  return target;
};

 
exports.modelToResources = function(subModel, withValue) {
  var resources = [];
  Object.keys(subModel).forEach(function(key) {
    var val       = subModel[key];
    var resource  = {};
    resource.id   = key;
    resource.name = val['name'];
    if(withValue) resource.values = val.data[val.data.length-1];
    console.log("utils modelToResources name=" + resource.name + 
    		" key=" + key + " resource.values=" + resource.values );   
    resources.push(resource);
  });
  return resources;
};

exports.isTokenValid = function(token) {
  return keys.apiToken === token;
};

 


exports.cappedPush = function(array, entry) {
  if(array.length >= model.customFields.dataArraySize) {
    array.shift();
    array.push(entry);
  } else {
    array.push(entry);
  }
  return array;
};

