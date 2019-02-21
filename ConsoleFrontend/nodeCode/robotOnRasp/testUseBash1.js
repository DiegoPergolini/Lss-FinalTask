var spawn = require('child_process').spawn;
var child = spawn('sudo ./SonarAlone', [
  '-v', 'builds/pdf/book.html',
  '-o', 'builds/pdf/book.pdf'
]);

child.stdout.on('data', function(chunk) {
  // output will be here in chunks
	console.log(chunk);
});

// or if you want to send output elsewhere
//child.stdout.pipe(dest);

//https://stackoverflow.com/questions/20643470/execute-a-command-line-binary-with-node-js