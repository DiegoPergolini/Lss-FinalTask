const lowlevel = require( "child_process" );
var sys = require('sys');
 
function puts(error, stdout, stderr) { sys.puts(stdout) }
function endCmd(error, stdout, stderr) { if( ! error)  lowlevel.exec( cmdStop,  puts) }

var cmdLedOn = "gpio mode 6 out; gpio write 6 1; sleep 2.0; gpio write 6 0";

//var cmdForward  = "gpio write 10 0; gpio write 9 1; gpio write 2 0; gpio write 3 1; sleep 1.0";
//var cmdStop     = "gpio write 10 0; gpio write 9 0; gpio write 2 0; gpio write 3 0";


var cmdForward  = "gpio write 8 0; gpio write 9 1; gpio write 12 0; gpio write 13 1; sleep 1.0";
var cmdStop     = "gpio write 8 0; gpio write 9 0; gpio write 12 0; gpio write 13 0";


var cmdBackward = "gpio write 8 1; gpio write 9 0; gpio write 12 1; gpio write 13 0; sleep 1.0";

 
lowlevel.exec( cmdLedOn,     puts);	 
lowlevel.exec( cmdForward,  endCmd);	


//lowlevel.exec( "sudo bash nanoMotorDriveB.sh",puts);	 
//lowlevel.exec( "bash led25Gpio.sh",puts);	 

 

//console.log("ends with:"+ lowlevel);


//https://stackoverflow.com/questions/20643470/execute-a-command-line-binary-with-node-js