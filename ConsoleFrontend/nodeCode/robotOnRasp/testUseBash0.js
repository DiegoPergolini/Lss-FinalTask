const lowlevel = require( "child_process" );
var sys = require('sys');

//console.log("starts with:"+ lowlevel);

//sys.print('-------------- '  );

/*
function puts(error, stdout, stderr) { sys.puts(stdout) }
lowlevel.exec("sudo sudo bash nanoMotorDriveB.sh", puts);


lowlevel.exec("ls -la", puts);
*/

function puts(error, stdout, stderr) { sys.puts(stdout) }

var cmdForward = "gpio write 10 0\ngpio write 9 1"
lowlevel.exec( "sudo bash led25Gpio.sh",puts);	//nanoMotorDriveB.sh

/*
lowlevel.exec( "sudo bash nanoMotorDriveB.sh",
//lowlevel.exec( "sudo ./SonarAlone ",
 function(err, stdout, stderr) {
  if (err) {
    // node couldn't execute the command
    console.log("ERROR ");
    return;
  }
  
   
  else{
    	sys.print('stdout: ' + stdout);
  	console.log("done");
  }
   
});
*/

console.log("ends with:"+ lowlevel);


//https://stackoverflow.com/questions/20643470/execute-a-command-line-binary-with-node-js