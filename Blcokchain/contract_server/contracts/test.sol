// SPDX-License-Identifier: MIT
pragma solidity ^0.8.15;

contract test{
 
   /* address internal owner;
    uint public count;
    // 구조체 선언
    struct user_info {
        string id;
        string password;
        string name;
        uint8 age;
    }

    // 생성자 함수
    // 컨트렉트 배포가 될 때 최초로 한번만 실행되는 함수
    constructor(){
        //  msg.sender는 deploy를 하는 지갑 주소
        owner = msg.sender;
        count = 0;
    }

    // mapping 데이터를 생성
    mapping (string => user_info) internal users;

    // 구조체를 이용하여 배열을 생성
    string[] public user_list;

    // Modifier
    modifier onlyOwner {
        require(msg.sender == owner, "Only owner can call function");
        _;
    }

    modifier increament{
        _;
        count++;
    }

    // 유저의 정보를 users mapping에 대입
    function add_user(string memory _id, string memory _pass, string memory _name, uint8 _age) public onlyOwner increament {
        //reqire(msg.sender == owner, "only owner can call function")
        // mapping 데이터에 매개변수의 값들은 대입
        users[_id].password = _pass; 
        users[_id].name = _name; 
        users[_id].age = _age; 
        //배열데이터에 id 값을 추가
        user_list.push(_id);
    }

    // 유저정보 출력하는 함수
    function view_user(string memory _id) public view returns(string memory, string memory, uint) {
        return (users[_id].password,users[_id].name,users[_id].age);
    }

    // count 값을 출력하는 함수
    function view_count(  ) public view returns(uint){
        return count;
    }

    // 배열 데이터를 출력하는 함수
    function view_list() public view returns(string[] memory){
        return user_list;
    }

    // 아이디가 존재하는가? 함수 생성
    function check_id(string memory _id) public view returns(bool){
        if(users[_id].age != 0){
            return false;
        } else {
            return true;
        }
    } 

}

// mapping data
// key : value
// mapping (string => user_info)

    {
        key1(string) : {
            password : string,
            name : string,
            age : uint
        },
        key2(string) : {
            password : string,
            name : string,
            age : uint
        }, ......
    }

    구조체가 배열이면
구조체[]
    [
        {
            password : string,
            name : string,
            age : uint
        },
        {
            password : string,
            name : string,
            age : uint
        }
    ]
 
    json 형태 데이터
    data = {
        '1' : 'test'
    }
    data에 새로운 '2' 키값과 'test2' value 를 추가하려면?
    data['2'] = 'test2'
    data['1'] = 'test3'

*/


// 변수 선언
    address internal owner;
    uint internal count;

    // 구조체 선언
    struct user_info{
        string password;
        string name;
        uint8 age;
    }

    // 생성자 함수
    // 컨트렉트 배포가 될 때 최초로 한번만 실행되는 함수
    constructor(){
        //  msg.sender는 deploy를 하는 지갑의 주소
        owner = msg.sender;
        count = 0;
    }

    // mapping 데이터를 생성 
    mapping (string => user_info) internal users;

    // 배열 데이터를 생성 
    string[] internal user_list;

    // modifier
    modifier onlyOwner{
        require(msg.sender == owner, "Only owner can call function");
        _;
    }

    modifier increament{
        _;
        count++;
    }

    // 유저의 정보를 users mapping에 대입
    function add_user(
        string memory _id, 
        string memory _pass, 
        string memory _name, 
        uint8 _age
    ) public onlyOwner increament {
        // require(msg.sender == owner, "only owner can call function");
        // mapping 데이터에 매개변수의 값들은 대입
        users[_id].password = _pass;
        users[_id].name = _name;
        users[_id].age = _age;
        // 배열 데이터에 id값을 추가 
        user_list.push(_id);
    }

    // 유저의 정보를 출력하는 함수 
    function view_user(
        string memory _id
    ) public view returns (
        string memory, 
        string memory, 
        uint8
    ){
        return (
            users[_id].password, 
            users[_id].name, 
            users[_id].age
        );
    }

    // count 값을 출력하는 함수
    function view_count() public view returns(uint){
        return count;
    }

    // 배열 데이터를 출력하는 함수
    function view_list() public view returns(string[] memory){
        return user_list;
    }

    // 아이디가 존재하는가? 함수 생성
    function check_id(
        string memory _id
    ) public view returns (bool){
        if (users[_id].age != 0){
            return false;
        }else{
            return true;
        }
    }
} 