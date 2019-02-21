const 
//	msgpack = require('msgpack5')(),
//  encode = msgpack.encode,
  model = require('./../models/robotModel');

module.exports = function () {
  return function (req, res, next) {
	console.log("converter html="+ req.accepts('html') + " req.type=" + req.type);
    if (req.myresult) {

      req.rooturl = req.headers.host;
      req.qp      = req._parsedUrl.search;
      
      console.log("converter html="+ req.accepts('html') + " req.type=" + req.type);

      if (req.accepts('html')) {

        var helpers = {
          json: function (object) {
            return JSON.stringify(object);
          },
          getById: function (object, id) {
            return object[id];
          }
        };

        // Check if there's a custom renderer for this media type and resource
        if (req.type) res.render(req.type, {req: req, helpers: helpers});
        else res.render('default', {req: req, helpers: helpers});

        return;
      }

//      if (req.accepts('application/x-msgpack')) {
//        console.info('MessagePack representation selected!');
//        res.type('application/x-msgpack');
//        res.send(encode(req.result));
//        return;
//      }
//
//      if (req.accepts('application/ld+json')) {
//        console.info('JSON-ld representation selected!');
//        res.send(model);
//        return;
//      }

      console.info('converter Defaulting to JSON representation!');
      res.send(req.myresult);
      return;

    }
    else if (res.location) {
      res.status(204).send();
      return;
    } else {
      next();
    }
  }
};

