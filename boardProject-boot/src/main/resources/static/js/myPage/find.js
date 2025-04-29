
const findId = document.querySelector("#findId"); // form 태그
const findPw = document.querySelector("#findPw"); // form 태그

const findIdForm = document.querySelector("#findIdForm"); // form 태그
const findPwForm = document.querySelector("#findPwForm"); // form 태그
findId.addEventListener("click", () => {
    findIdForm.classList.remove("hidden");
    findPwForm.classList.add("hidden");
});

findPw.addEventListener("click", () => {
    findPwForm.classList.remove("hidden");
    findIdForm.classList.add("hidden");
});

const IdForm = document.getElementById("IdForm"); // form 태그
const PwForm = document.getElementById("PwForm"); // form 태그

// #updateInfo 요소가 존재 할 때만 수행
if(IdForm != null) {
    // form 제출 시
    IdForm.addEventListener("submit", async e => {
        e.preventDefault();

        const memberNickname = document.getElementById("memberNickname");
        const memberTel = document.getElementById("memberTel");

        // 닉네임 유효성 검사
        if(memberNickname.value.trim().length === 0) {
            alert("닉네임을 입력해주세요");
            //e.preventDefault(); // 제출 막기
            return;
        }

        // 닉네임 정규식에 맞지 않으면
        let regExp = /^[가-힣\w\d]{2,10}$/;
        if( !regExp.test(memberNickname.value)) {
            alert("닉네임이 유효하지 않습니다.");
            //e.preventDefault(); // 제출 막기
            return;
        }

        

        // 전화번호 유효성 검사
        if(memberTel.value.trim().length === 0) {
            alert("전화번호를 입력해 주세요");
            //e.preventDefault();
            return;
        }

        // 전화번호 정규식에 맞지 않으면
        regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;
        if( !regExp.test(memberTel.value)) {
            alert("전화번호가 유효하지 않습니다");
            //e.preventDefault();
            return;
        }

        fetch("/member/findId", {
            method: "POST",
            body: JSON.stringify({
                memberNickname: memberNickname.value,
                memberTel: memberTel.value
            })
        })
        .then(response => response.text())
        .then(data => {
            console.log(data);
        })
    });
}