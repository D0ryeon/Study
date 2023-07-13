// express 로드
const express =require('express')
const app = express()
const port = 3000;

// view 파일들의 기본 경로 설정
app.set('views',__dirname + '/views');
// view engine 설정
app.set('view engine','ejs');

//post 형태로 데이터가 들어오는 경우 json형태로 변경
app.use(express.urlencoded({extended:false}));


//contract의 정보가 담겨있는 json파일 로드
const contract_info = require("./build/contracts/test.json");
const contract_abi = contract_info.abi;
const contract_address = contract_info.networks[5777].address;

console.log(contract_address);


// 컨트랙트가 배포된 네트워크 등록
const { Web3 } = require('web3');
const web3 = new Web3(new Web3.providers.HttpProvider('http://127.0.0.1:7545'));


// 배포한 컨트랙트의 주소와 abi를 이용하여 컨트랙트 로드
const smartContract = new web3.eth.Contract(contract_abi, contract_address);


app.get('/user_list',async function(req, res){
    // count 값을 로드
    const count = await smartContract.methods.view_count().call()
    console.log(count);
    // [] 값을 로드
    const user_list = await smartContract.methods.view_list().call()
    console.log(user_list);
    // 배열의 데이터들을 하나씩 뽑아서 contract 안에 있는 view_info()에 넣어준다.
    // 결과값을 새로운 배열 데이터에 추가
    let result = new Array();
    for(var i=0; i<count; i++){
        let _id = user_list[i];
        const data = await smartContract.methods.view_user(_id).call()
        data['id'] = _id;
        result.push(data); 
    }

    
    res.render('user_list.ejs', {
        'data' : result
    })

})

// 로그인 관련 주소를 생성
app.post('/signin', function (req, res) {
    // 유저가 보낸 데이터를 변수에 대입
    const input_id = req.body._id;
    const input_pw = req.body._pw;
    // 값 잘 들어왔는지 확인하는 작업은 꼭 해주세요
    console.log(input_id, input_pw);
 
    // smartcontract를 이용하여 해당하는 아이디가 존재하는지 체크
    // 데이터가 존재한다면 유저가 입력한 password와 데이터의 password 값을 비교
    // 두 값이 같다면 로그인 성공
    // 그 외의 경우는 로그인 실패
    smartContract.methods
       .view_user(input_id) // 해당 함수가 요구하는 인자는 하나 (키값)! 얘는 데이터를 리턴하기만 해서(호출만 함) 트랜잭션 x 수수료 발생 x
       .call() // 호출
       .then(function (result) {
          //  res.send(result); // 데이터가 어떻게 들어오는지 확인해봄
          // result는 {'0': password, '1': name, '2': age}
          // 로그인이 성공하는 조건
          // result['0'] == input_pass 그리고 result['0'] != ""
          if ((result['0'] == input_pw) & (result['0'] != '')) {
             res.render('index.ejs',{
                'name' : result['1']
             });
          } else {
             res.redirect('/');
          }
       });
 });
/*
// fhrmdls rhksfus wnthfmf todtjd
app.post('/signin',function(req,res){
    //유저가 보낸 데이터를 변수에 대입
    const input_id = req.body._id;
    const input_pw = req.body._pw;
    console.log(input_id,input_pw);

    
    //smartcontract를 이용하여 해당하는 아이디가 존재하는지 체크
    //데이터가 존재한다면 유저가 입력한 password와 데이터의 password값을 비교
    //두 값이 같다면 로그인 성공
    //그 외의 경우는 로그인 실패
    smartContract.methods.view_user(input_id).call().then(function(result){
        res.send(result);
    })

})
*/

// localhost:3000/ 요청이 들어오는 경우
app.get("/",function(req,res){
    res.render('login.ejs')
})

app.get('/signup',(req,res)=>{
    res.render('signup.ejs')
})

app.post('/signup2',(req,res)=>{
    // post 형태에서 데이터가 존재하는곳은? => req.body.(key)
    const input_id = req.body._id;
    const input_pw = req.body._pw;
    const input_name = req.body._name;
    const input_age = req.body._age;
    console.log(input_id,input_pw,input_name,input_age);
    //res.send('signup2');
    smartContract.methods.add_user(input_id,input_pw,input_name,input_age)
    .send(
        {
            gas : 200000,
            from : '0x72121d968C742d7d4e428b02e2faBbB7F3777bE5'

        }
    ).then(function(receipt){
        console.log(receipt);
        res.redirect('/');
    })

})


app.listen(port,function(){
    console.log('server start');
})