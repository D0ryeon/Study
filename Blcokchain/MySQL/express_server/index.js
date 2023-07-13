// express 모듈을 로드
const express = require('express');
const app = express();
// 웹서버의 port 번호를 지정
const port = 3000; 

// 기본 경로 localhost:3000
// api 생성
// localhost:3000/ 요청이 들어오면

// 뷰 파일들의 기본 경로 설정
// __dirname : 현재 파일의 경로
app.set('views',__dirname + '/views');
// 뷰 파일의 엔진을 어떤것을 사용할것인가 지정
app.set('vies engine','ejs');



app.get("/",function(req,res){
    // req request 유저가 서버에게 요청을 보내는 부분
    // res response 서버가 유저에게 응답을 보내는 부분
    // res.send('Hello World !!');
    // ejs 파일을 응답
     res.render('main.ejs'); 
});



app.get("/second",function(req,res){

    //res.send('asdasdasd'); 
    res.render('data.ejs')
});

app.get("/third",function(req,res){

    res.send('5');

});



// 웹 서버의 시작
app.listen(port,function(){
    console.log('server start');
});