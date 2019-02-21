/*
* =====================================
* expressBasic/appViewEjs.js
* =====================================
*/
var express = require("express");
var http    = require("http");
var logger  = require("morgan");	//npm install --save morgan (a logger)
var path    = require("path");
var app     = express();         
var cors    = require("cors");  //npm install --save cors (enable cross-site requests)

app.use( logger("short") ) ;	 
app.use( cors() ) ;

app.use(express.static(path.join(__dirname, 'views'))); //for style.css;

app.set("view engine", "ejs");			//npm install --save ejs (Embedded JavaScript)
app.set("views", path.resolve(__dirname, "views"));
 
app.get("/", function(request, response,next) {
	response.write("Welcome\n");
	next();
});

app.get("/data", function(request, response,next) {
	console.log("data " );
	response.render("example.ejs", { title: "Application", data:['a','b'] });
	next();
});

/* 
 * Experiment ejs at raw leve
 */
var ejs  = require("ejs");

app.get("/raw", function(request, response, next) {
  	var template="<html><h1><%= title %></h1> <p><% for(var i=0; i<data.length; i++) { %><li><%= data[i] %><% } %></p></html>";
	var mydata =  [ 'a',  'b'];
	var html = ejs.render( template , { title: "Application", data:  mydata  } );
	console.log( html );
	response.write(html);
	next();
});


//no next => terminate;
app.use(function( request, response ) {
  	response.end("bye\n");
});




//var html = new EJS({url: 'cleaning.ejs'}).render(data);
http.createServer(app).listen(3000, function(){
	console.log('bound to port 3000');
});

