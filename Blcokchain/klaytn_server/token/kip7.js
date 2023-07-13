// caver-js-ext-kas 모듈을 로드
const CaverExtKas = require("caver-js-ext-kas");
// class생성
const caver = new CaverExtKas();
// fs모듈 로드
const fs = require('fs');
// dotenv 로드 (환경변수 설정) *환경변수는 어떤 폴더 내에서도 사용할 수 있는 변수
require('dotenv').config();

// KAS에 접속하기 위한 ID, PASSWORD의 파일을 로드
const kas_info = require('./kas.json')
console.log(kas_info)
// accesskeyId 변수, secretAccessKey 생성
const accesskeyId = kas_info.accessKeyId
const secretAccessKey = kas_info.secretAccessKey
// testnet의 chainid 지정
const chainid = 1001;
console.log(accesskeyId,secretAccessKey);

//생성자 함수 호출
caver.initKASAPI(chainid,accesskeyId,secretAccessKey);

// KAS에서 외부의 지갑을 사용하기 위해선 지갑 등록
const keyringContainer = new caver.keyringContainer();
const keyring = keyringContainer.keyring.createFromPrivateKey(
    process.env.private_key
)
keyringContainer.add(keyring)

async function create_token(_name, _sybmbol, _decimal, _amount){
    const kip7 = await caver.kct.kip7.deploy(
        {
            name : _name,
            symbol : _sybmbol,
            decimals : _decimal,
            initialSupply : _amount
        },
        keyring.address,
        keyringContainer
    )
    const addr = kip7._address
    console.log(addr)
    // 토큰의 주소 값을 json 파일 안에 대입
    const kip7_address = {
        address : addr
    }
    // json을 문자형을 변환
    const data = JSON.stringify(kip7_address)
    // json 파일의 형태로 저장
    fs.writeFileSync('./token/kip7.json',data)
    return '토큰 발행 완료'

}

//create_token('block_test','BT',0,1000000)

//토큰을 거래하는 함수 선언
async function transfer(_address,_amount){
    //발행한 토큰을 wallet에 추가
    const token_info = require('./kip7.json')
    const kip7 = await new caver.kct.kip7(token_info.address)
    kip7.setWallet(keyringContainer)

    const receipt = await kip7.transfer(
        _address,
        _amount,
        {
            from : keyring.address
        }
    )
    console.log(receipt);
    return "토큰 거래 완료"
}

// console.log(transfer("0x5D934626c287Fd58EF634782B28CF1284Df2e1B6",100))

// 유저가 토큰 발행자에게 토큰을 보내는 함수(transction의 주체자가 발행자)
async function transfer_from(_private, _amount){
    // 발행한 토큰을 wallet 추가
    const token_info = require('./kip7.json')
    const kip7 = await new caver.kct.kip7(token_info.address)
    kip7.setWallet(keyringContainer)

    // 토큰 발행자의 지갑 주소
    const owner =keyring.address
    console.log(owner);

    // 유저의 지갑 주소를 keyringCOntainer에 등록
    const keyring2 = keyringContainer.keyring.createFromPrivateKey(
        _private
    )
    keyringContainer.add(keyring2)

    // approve() : 내 지갑에 있는 일정 토큰을 다른 사람이 이동 시킬 수 있게 권한을 주는 함수
    // approve(권한을 받을 지갑의 주소, 토큰의 양 , from)
    await kip7.
    (owner,_amount,{
        from : keyring2.address
    })


    const receipt = await kip7.transferFrom(
        keyring2.address,
        owner,
        _amount,
        {
            from : owner
        }
    )
    console.log(receipt);

    return "토큰 이동 완료"
}

//transfer_from('0x051e738e3c463a37a5ac3921f20a8c80dc53379f7a6852cfc1761b83735ffc8f',10)

async function balance_of(_address){
    const token_info = require('./kip7.json')
    const kip7 = await new caver.kct.kip7(token_info.address);
    kip7.setWall
    et(keyringContainer);

    const balance = await kip7.balanceOf(_address)

    console.log(balance)
    return balance
}
// balance_of('0x5D934626c287Fd58EF634782B28CF1284Df2e1B6');

// 지갑을 생성하는 함수 선언
async function create_wallet(){
    const wallet = await caver.kas.wallet.createAccount()
    console.log(wallet)
    return wallet.address
}


module.exports = {
    create_token,transfer,transfer_from,balance_of,create_wallet
}