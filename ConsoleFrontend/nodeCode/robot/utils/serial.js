/*
 * nodeCode/robot/utils/serial.js
 */
const echannel   =  require("./channel");
const serialport = require("serialport");		//npm install --save serialport
const SerialPort = serialport.SerialPort;

// list serial ports:

serialport.list(function (err, ports) {
  ports.forEach(function(port) {
    console.log(port.comName);
  });
}); 

/*
 * Out of the box, node-serialport provides four parsers: 
 * one that simply emits the raw buffer as a data event, 
 * one that emits a data event when a specfic byte sequence is received, 
 * one that emits a data event every 'length' bytes, 
 * and one which provides familiar "readline" style parsing.
 */
var portName="COM6";  //"/dev/ttyUSB0";
var inputStr = "";
//console.log(serialport.parsers);

var port = new serialport (portName, {
  baudRate: 115200, //9600,
  parser: new serialport.parsers.Readline("\r\n")
});
//var msg  = "1";
port.on('open', 
		function() { console.log('port open '); 
});
port.on('data', function (data) {
	//console.log('Data: ' + data);
	inputStr = inputStr + data;
	if( inputStr.indexOf("\n") >= 0 ){
		//console.log("serial port input= " + inputStr);
		echannel.emit("sonarEvent", inputStr);
		inputStr = "";
	}
});	 

port.on('error', function(err) {
	  console.log('Error: ', err.message);
});
	
//ledOn = function(){ console.log("ledOn");  port.write("1"); }
//ledOff= function(){ console.log("ledOff"); port.write("0"); }
//
//setTimeout( ledOn,  2000 );
//setTimeout( ledOff, 3000 );

module.exports=port;	
	