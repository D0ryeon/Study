/**
 * 
 */
 function inputCheck() {
	var userinput = eval("document.regForm");
	if(userinput.id.value == null){
		alert("아이디를 입력하세요.");
		userinput.id.focus();
		return false;
	}
	if(userinput.passwd.value == null){
		alert("비밀번호를 입력하세요.");
		userinput.passwd.focus();
		return false;
	}
	if(userinput.repasswd.value == null){
		alert("비밀번호를 재입력하세요.");
		userinput.repasswd.focus();
		return false;
	}
	if (userinput.name.value == null) {
		alert("이름을 입력하세요.");
		userinput.name.focus();
		return false;
	}
	if (userinput.email.value == null) {
		alert("이메일을 입력하세요.");
		userinput.email.focus();
		return false;
	}
	if (userinput.tel.value == null) {
		alert("연락처를 입력하세요.");
		userinput.tel.focus();
		return false;
	}
	if (userinput.passwd.value !== userinput.repasswd.value) {
		alert("비밀번호가 일치하지 않습니다.");
		userinput.repasswd.focus();
		return false;
	}
}