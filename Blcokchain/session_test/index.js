// express 로드
const express = require('express');
const session = require('express-session');
const app = express();
const port = 3000;

// view 파일의 기본 경로 설정
app.set('views',__dirname+"/views");
// view engine 지정
app.set('view engine', 'ejs');
// post 데이터를 받는 경우에 json형태로 데이터 변환
app.use(express.urlencoded({extended:false}));
app.use(session({
    secret: '12345',
    resave: false,
    saveUninitialized: false,
    cookie: { secure: false }
}))
app.get('/',(req, res) => {
    console.log(req.session);
    console.log("id : " + req.sessionID);
    res.send('hello world');
})
app.get('/makeSession', (req, res) => {
    if(req.session.test){
     //   res.send('세션이 이미 존재');
    }
    else {  
        req.session.test = "test string";
        console.log("id : " + req.sessionID)
        res.send('세션 생성');
    }
})

app.get('/confirmSession', (req, res) => {
    if(req.session.test){
        console.log(req.session);
        res.send('세션 o');
    }
    else {
        console.log('no session');
        res.send('세션 x');
    }
})

app.get('/deleteSession', (req, res) => {
    if(req.session){
        req.session.destroy(()=>{
            res.redirect('/');
        });
    }
    else {
        res.send('제거할 세션이 없습니다.');
    }
})

app.listen(port, () => {
    console.log(`session 테스트용 앱 http://localhost:${port}`)
})