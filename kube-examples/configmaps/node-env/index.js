var http = require('http');

http.createServer(function (req, res) {
  res.write(process.env.JAHANS); 
  res.end();
}).listen(8080); 
