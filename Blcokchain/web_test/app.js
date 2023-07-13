const express = require ("express");
const path = require('path');
require("ejs");
// console.log("Hello World!!");

var bodyParser = require('body-parser');
const app = express();
// view engine setup
app.set('views', path.join(__dirname, '/views'));
app.set('view engine', 'ejs');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));



app.get('/',function (req, res) {
    res.render('index');
})

app.post('/test',(req,res) =>{
    console.log(req.body)
    return res.send("Hello Post");
    
})


const port = 3000;
app.listen(port, function(){
    console.log(`server is runing at http://localhost:${port}`)
});
