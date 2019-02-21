/*
 * it.unibo.mbot2018/nodeCode/robotFrontend/appServer/plugins/ledsPlugin.js
 */
var resourceModel = require('./../models/model');
var observable    = require('./../uniboSupports/observableFactory'); 	 
var mqttUtils     = require('./../uniboSupports/mqttUtils'); 	 

var actuator, interval;
var ledModel      = resourceModel.pi.actuators.leds['1'];
var pluginName    = ledModel.name;
var localParams   = {'simulate': false, 'frequency': 2000};
var counter       = 1;

exports.start = function (params) {
  localParams = params;
  observe(ledModel); 	//work as an observer;

  if (localParams.simulate) {
//    simulate();		 
  } else {
    connectHardware();
  }
};

exports.stop = function () {
  if (localParams.simulate) {
    clearInterval(interval);
  } else {
    actuator.unexport();
  }
  console.info('%s plugin stopped!', pluginName);
};

function observe(what) {
	console.info('plugin observe: ' + localParams.frequency + " CHANGE MDOEL INTO OBSERVABLE");
 	console.info( what ); 
//Change the ledModel into an observable;	
const whatObservable = new observable(what);	
observable           = whatObservable.data;
whatObservable.observe('value', () => {
	var val =  "off";
	if( observable.value === "true" ) val = "on";
	var eventstr = "msg(ctrlEvent,event,js,none,ctrlEvent(leds, led1, " +val + ")," +  counter++ + ")"
		console.log("	ledPlugin LED emits> "+ eventstr);
// 		mqttUtils.publish( eventstr );
  	});
};

function switchOnOff(value) {
  if (!localParams.simulate) {
    actuator.write(value === true ? 1 : 0, function () {  
      console.info('Changed value of %s to %s', pluginName, value);
    });
  }
};

function connectHardware() {
  var Gpio = require('onoff').Gpio;
  actuator = new Gpio(ledModel.gpio, 'out');  
  console.info('Hardware %s actuator started!', pluginName);
};

function simulate() {
  interval = setInterval(function () {
    // Switch value on a regular basis;
    if (ledModel.value) {
      ledModel.value = false;
    } else {
      ledModel.value = true;
    }
//  console.log("LED=" + ledModel.value);
  }, localParams.frequency);
  console.info('Simulated %s actuator started!', pluginName);
};