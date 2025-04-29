// 유효성 검사
const checkObj = {
    memberEmail     : false,
    authKey         : false,
    memberPw        : false,
    memberPwConfirm : false,
    memberNickname  : false,
    memberTel       : false
}

// 인증번호 받기 버튼
const sendAuthKeyBtn = document.getElementById("sendAuthKeyBtn");
// 인증번호 입력 필드
const authKey = document.getElementById("authKey");
// 인증번호 확인 버튼
const checkAuthKeyBtn = document.getElementById("checkAuthKeyBtn");
// 인증번호 확인 메시지
const authKeyMessage = document.getElementById("authKeyMessage");

const memberEmail = document.getElementById("memberEmail");     // input
const emailMessage = document.getElementById("emailMessage");   // span

let authTimer;
const initMin = 4;
const initSec = 59;
const initTime = "05:00";

let min;
let sec;

/**
 * 이메일 입력
 */
memberEmail.addEventListener("input", (e) => {

    checkObj.authKey = false;
    authKeyMessage.innerText = "";
    clearInterval(authTimer);

    
    const inputValue = e.target.value;

    // 이메일 입력 여부 검사
    if (inputValue.trim() === "") {
        emailMessage.innerText = "이메일을 입력해주세요.";
        emailMessage.classList.remove('confirm','error');
        checkObj.memberEmail = false;
        memberEmail.value = "";

        return;
    }
    // 이메일 형식 검사
    const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!regExp.test(inputValue)) {
        emailMessage.innerText = "이메일 형식이 올바르지 않습니다.";
        emailMessage.classList.add('error');
        emailMessage.classList.remove('confirm');
        checkObj.memberEmail = false;
        return;
    }
    // 이메일 중복 검사
    fetch(`/member/checkEmail?memberEmail=${inputValue}`)
        .then(resp => resp.json())
        .then(count => {
            if (count > 0) {
                emailMessage.innerText = "이미 사용중인 이메일입니다.";
                emailMessage.classList.add('error');
                emailMessage.classList.remove('confirm');
                checkObj.memberEmail = false;
            } else {
                emailMessage.innerText = "사용 가능한 이메일입니다.";
                emailMessage.classList.add('confirm');
                emailMessage.classList.remove('error');
                checkObj.memberEmail = true;
            }
        })
        .catch(error => {
            console.error("이메일 중복 검사 오류:", error);
        });
});

/**
 * 인증번호 받기 버튼 클릭 
 */
sendAuthKeyBtn.addEventListener("click", () => {

    checkObj.authKey = false;
    authKeyMessage.innerText = "";

    if(!checkObj.memberEmail) {
        alert("유효한 이메일을 입력해주세요.");
        return;
    }

    min = initMin;
    sec = initSec;

    clearInterval(authTimer);

    fetch("/email/signup",{
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : memberEmail.value
    })
    .then(resp => resp.text())
    .then(result => {
        if(result>0) console.log("인증번호 발송 성공");
        else console.log("인증번호 발송 실패");
    })

    authKeyMessage.innerText = initTime;
    authKeyMessage.classList.remove("confirm","error");
    alert("인증번호가 발송되었습니다.");

    authTimer = setInterval(() => {
        if(min == 0 && sec == 0) {
            clearInterval(authTimer);
            checkObj.authKey = false;
            authKeyMessage.classList.add("error");
            authKeyMessage.classList.remove("confirm");
            authKeyMessage.innerText = "인증번호 시간이 만료되었습니다.";
            alert("인증번호 시간이 만료되었습니다.");
            return;
        }

        authKeyMessage.innerText = `${addZero(min)} : ${addZero(sec)}`;

        if(sec > 0) {
            sec--;
        } else {
            min--;
            sec = 59;
        }
    }, 1000);

});

function addZero(value) {
    return value < 10 ? "0" + value : value;
}

/**
 * 인증번호 확인 버튼 클릭 
 */
checkAuthKeyBtn.addEventListener("click", () => {

    if(authKey.value.trim() === "") {
        alert("인증번호를 입력해주세요.");
        return;
    }
    if(min == 0 && sec == 0) {
        alert("인증번호 시간이 만료되었습니다.");
        return;
    }
    if(authKey.value.length != 6) {
        alert("인증번호는 6자리로 입력해주세요.");
        return;
    }

    const obj = {
        memberEmail: memberEmail.value,
        authKey: authKey.value
    }

    fetch("/email/checkAuthKey", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: obj
    })
    .then(resp => resp.text())
    .then(result => {
        if(result == 0) {
            checkObj.authKey = false;
            alert("인증번호가 일치하지 않습니다.");
        } else {
            clearInterval(authTimer);
            alert("인증되었습니다.");
            checkObj.authKey = true;
            authKeyMessage.innerText = "인증되었습니다.";
            authKeyMessage.classList.add("confirm");
            authKeyMessage.classList.remove("error");
        }
    })
});

const memberPw = document.getElementById("memberPw");
const memberPwConfirm = document.getElementById("memberPwConfirm");
const pwMessage = document.getElementById("pwMessage");
const checkPw = ()=> {
    if(memberPw.value === memberPwConfirm.value) {
        pwMessage.innerText = "비밀번호가 일치합니다.";
        pwMessage.classList.add('confirm');
        pwMessage.classList.remove('error');
        checkObj.memberPwConfirm = true;
        return;
    }
    pwMessage.innerText = "비밀번호가 일치하지 않습니다.";
    pwMessage.classList.add('error');
    pwMessage.classList.remove('confirm');
    checkObj.memberPwConfirm = false;
    return;
}

/**
 * 비밀번호 입력 
 */
memberPw.addEventListener("input", (e) => {
    const inputValue = e.target.value;
    const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;
    
    if(inputValue.trim() === "") {
        pwMessage.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
        pwMessage.classList.remove('confirm','error');
        checkObj.memberPw = false;
        memberPw.value = "";
        return;
    }
    if(!regExp.test(inputValue)) {
        pwMessage.innerText = "비밀번호 형식이 올바르지 않습니다.";
        pwMessage.classList.add('error');
        pwMessage.classList.remove('confirm');
        checkObj.memberPw = false;
        return;
    }

    pwMessage.innerText = "사용 가능한 비밀번호입니다.";
    pwMessage.classList.add('confirm');
    pwMessage.classList.remove('error');
    checkObj.memberPw = true;

    // 비밀번호 확인 검사
    if(memberPwConfirm.value.length > 0) {
        checkPw();
    }
})

// 비밀번호 확인 입력 
memberPwConfirm.addEventListener("input", () => {
    if(checkObj.memberPw) checkPw();
})

/**
 * 닉네임 입력 
 */
const memberNickname = document.getElementById("memberNickname");
const nickMessage = document.getElementById("nickMessage");

memberNickname.addEventListener("input", (e) => {
    const inputNickname = e.target.value;
    const regExp = /^[가-힣\w\d]{2,10}$/;
    
    if(inputNickname.trim() === "") {
        nickMessage.innerText = "한글,영어,숫자로만 2~10글자";
        nickMessage.classList.remove('confirm','error');
        checkObj.memberNickname = false;
        memberNickname.value = "";
        return;
    }
    if(!regExp.test(inputNickname)) {
        nickMessage.innerText = "유효하지 않은 닉네임 형식입니다.";
        nickMessage.classList.add('error');
        nickMessage.classList.remove('confirm');
        checkObj.memberNickname = false;
        return;
    }
    fetch(`/member/checkNickname?memberNickname=${inputNickname}`)
        .then(resp => resp.text())
        .then(count => {
            if(count > 0) {
                nickMessage.innerText = "이미 사용중인 닉네임입니다.";
                nickMessage.classList.add('error');
                nickMessage.classList.remove('confirm');
                checkObj.memberNickname = false;
                return;
            }
            nickMessage.innerText = "사용 가능한 닉네임입니다.";
            nickMessage.classList.add('confirm');
            nickMessage.classList.remove('error');
            checkObj.memberNickname = true;
        })
        .catch(error => {
            console.error("닉네임 중복 검사 오류:", error);
        });
});


/**
 * 전화번호 입력 
 */
const memberTel = document.getElementById("memberTel");
const telMessage = document.getElementById("telMessage");
const telRegEx = /^01[0-9]{1}\d{3,4}\d{4}$/;
const replaceNotInt = /[^0-9]/g;

memberTel.addEventListener("input", (e) => {
    // 숫자만 남도록 처리
    e.target.value = e.target.value.replace(replaceNotInt, "");
    
    const inputTel = e.target.value;

    if (inputTel.trim() === "") {
        telMessage.innerText = "전화번호를 입력해주세요.";
        telMessage.classList.remove("confirm", "error");
        checkObj.memberTel = false;
        return;
    }

    if (!telRegEx.test(inputTel)) {
        telMessage.innerText = "유효하지 않은 전화번호 형식입니다.";
        telMessage.classList.add("error");
        telMessage.classList.remove("confirm");
        checkObj.memberTel = false;
        return;
    }

    telMessage.innerText = "사용 가능한 전화번호입니다.";
    telMessage.classList.add("confirm");
    telMessage.classList.remove("error");
    checkObj.memberTel = true;
});


/**
 * 주소 검색 버튼 클릭 
 */
const searchAddressBtn = document.getElementById("searchAddress");

searchAddressBtn.addEventListener("click", () => {
    execDaumPostcode();
});

const execDaumPostcode = () => {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

/**
 * 회원가입 버튼 클릭 
 */
const signUpForm = document.getElementById("signUpForm");

signUpForm.addEventListener("submit", (e) => {

    for(let key in checkObj) {
        if(!checkObj[key]) {
            switch(true) {
                case !checkObj.memberEmail:
                    str = "이메일이 유효하지 않습니다.";
                    break;
                case !checkObj.authKey:
                    str = "이메일이 인증되지 않았습니다.";
                    break;
                case !checkObj.memberPw:
                    str = "비밀번호가 유효하지 않습니다.";
                    break;
                case !checkObj.memberPwConfirm:
                    str = "비밀번호가 일치하지 않습니다.";
                    break;
                case !checkObj.memberNickname:
                    str = "닉네임이 유효하지 않습니다.";
                    break;
                case !checkObj.memberTel:
                    str = "전화번호가 유효하지 않습니다.";
                    break;
            }
            alert(str);
            document.getElementById(key).focus();

            e.preventDefault();
            return;
        }
    }
});
