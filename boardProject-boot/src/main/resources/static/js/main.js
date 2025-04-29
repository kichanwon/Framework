

const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");

const getCookie = (key) => {
    const cookies = document.cookie;
    const cookieList = cookies.split("; ").map(el => el.split("="));

    const obj = {};
    
    for(let i = 0; i < cookieList.length; i++) {
        const key = cookieList[i][0];
        const value = cookieList[i][1];

        obj[key] = value;
    }

    return obj[key];
}

if(loginEmail != null) {
    const saveId = getCookie("saveId");

    if(saveId != null) {
        loginEmail.value = saveId;
        document.querySelector("input[name='saveId']").checked = true;
    }
}

// login 잘못되면 서버에 요청 안보내게 하기 ← 숙제
const loginForm = document.querySelector("#loginForm");

// null 처리추가
// if(loginForm != null) {
if(loginForm) {
loginForm.addEventListener("submit", (e) => {
    const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");
    const loginPw = document.querySelector("#loginForm input[name='memberPw']");

    if(loginEmail.value.trim() === "") {
        e.preventDefault();
        alert("이메일을 입력해주세요.");
        return;
    }
    if(loginPw.value.trim() === "") {
        e.preventDefault();
        alert("비밀번호를 입력해주세요.");
        return;
    }

        loginForm.submit();
    });
}
