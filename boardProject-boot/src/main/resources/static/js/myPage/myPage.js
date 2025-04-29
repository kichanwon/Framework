/* 회원 정보 수정 페이지 */
const updateInfo = document.querySelector("#updateInfo"); // form 태그

// #updateInfo 요소가 존재 할 때만 수행
if(updateInfo != null) {

    // form 제출 시
    updateInfo.addEventListener("submit", async e => {
        e.preventDefault();

        const memberNickname = document.querySelector("#memberNickname");
        const memberTel = document.querySelector("#memberTel");
        const memberAddress = document.querySelectorAll("[name='memberAddress']");

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

        // 닉네임 중복검사
        const currentNickname = document.querySelector("input[name='currentNickname']").value;
        
        if(currentNickname !== memberNickname.value) {
            const resp = await fetch(`/myPage/checkNickname?nickname=${memberNickname.value}`);
            const result = await resp.text();

            if(result == "1") {
                alert("이미 사용중인 닉네임입니다.");
                //e.preventDefault();
                return;
            }
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


        // 주소 유효성 검사
        // 입력을 안하면 전부 안해야되고
        // 입력하면 전부 해야된다
        const addr0 = memberAddress[0].value.trim().length == 0; // t/f
        const addr1 = memberAddress[1].value.trim().length == 0; // t/f
        const addr2 = memberAddress[2].value.trim().length == 0; // t/f

        // 모두 true 인 경우만 true 저장
        const result1 = addr0 && addr1 && addr2; // 아무것도 입력 X

        // 모두 false 인 경우만 true 저장
        const result2 = !(addr0 || addr1 || addr2); // 모두 다 입력

        // 모두 입력 또는 모두 미입력이 아니면
        if( !(result1 || result2) ) {
            alert("주소를 모두 작성 또는 미작성 해주세요");
            //e.preventDefault();
            return;
        }

        // 제출 확인
        if(confirm("정보를 수정 하시겠습니까?")) {
            updateInfo.submit();
        }
    });
}



// ------------------------------------------

/* 비밀번호 수정 */

// 비밀번호 변경 form 태그
const changePw = document.querySelector("#changePw");

// changePw 요소가 존재 할 때만 수행
if(changePw) {
    // 제출 되었을 때
    changePw.addEventListener("submit", e => {

        const currentPw = document.querySelector("#currentPw");
        const newPw = document.querySelector("#newPw");
        const newPwConfirm = document.querySelector("#newPwConfirm");

        // - 값을 모두 입력했는가

        let str; // undefined 상태
        if( currentPw.value.trim().length == 0 ) str = "현재 비밀번호를 입력해주세요";
        else if( newPw.value.trim().length == 0 ) str = "새 비밀번호를 입력해주세요";
        else if( newPwConfirm.value.trim().length == 0 ) str = "새 비밀번호 확인을 입력해주세요";

        if(str != undefined) { // str에 값이 대입됨 == if 중 하나 실행됨
            alert(str);
            e.preventDefault();
            return;
        }

        // 새 비밀번호 정규식
        const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

        if( !regExp.test(newPw.value) ) {
            alert("새 비밀번호가 유효하지 않습니다");
            e.preventDefault();
            return;
        }

        // 새 비밀번호 == 새 비밀번호 확인
        if( newPw.value != newPwConfirm.value ) {
            alert("새 비밀번호가 일치하지 않습니다");
            e.preventDefault();
            return;
        } 
    });
};

// -------------------------------------
/* 탈퇴 유효성 검사 */



// 탈퇴 form 태그
const secession = document.querySelector("#secession");

if(secession != null) {

    secession.addEventListener("submit", e => {

        const memberPw = document.querySelector("#memberPw");
        const agree = document.querySelector("#agree");

        // - 비밀번호 입력 되었는지 확인
        if(memberPw.value.trim().length == 0) {
            alert("비밀번호를 입력해주세요.");
            e.preventDefault(); // 제출막기
            return;
        }

        // 약관 동의 체크 확인
        // checkbox 또는 radio checked 속성
        // - checked -> 체크 시 true, 미체크시 false 반환

        if(!agree.checked) { // 체크 안됐을 때
            alert("약관에 동의해주세요");
            e.preventDefault();
            return;
        }

        // 정말 탈퇴? 물어보기
        if( !confirm("정말 탈퇴 하시겠습니까?") ) {
            alert("취소 되었습니다.");
            e.preventDefault();
            return;
        }
    });
}

const box = document.querySelector('.agree-box');
let interval;
if(box != null){
    box.addEventListener('mouseenter', () => {
    interval = setInterval(() => {
        const x = Math.random() * 1200 - 600;
        const y = Math.random() * 100 - 50;
        box.style.transform = `translate(${x}px, ${y}px)`;
    }, 50); 
    });
    box.addEventListener('mouseleave', () => {
        clearInterval(interval);
    });
    if(agree.checked){
        clearInterval(interval);
        box.style.transform = 'translate(0, 0)';
    }
}



// -------------------------------------------------------
// 이미지 업로드 구간

/*  [input type="file" 사용 시 유의 사항]

  1. 파일 선택 후 취소를 누르면 
    선택한 파일이 사라진다  (value == '')

  2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다

  3. 선택된 파일 정보를 저장하는 속성은
    value가 아니라 files이다
*/

// 요소 참조
const profileForm = document.getElementById("profile");  // 프로필 form

if(profileForm != null) {
    const profileImg = document.getElementById("profileImg");  // 미리보기 이미지 img
    const imageInput = document.getElementById("imageInput");  // 이미지 파일 선택 input
    const deleteImage = document.getElementById("deleteImage");  // 이미지 삭제 버튼
    const MAX_SIZE = 1024 * 1024 * 5;  // 최대 파일 크기 설정 (5MB)

    const defaultImageUrl = `${window.location.origin}/images/user.png`;

    let statusCheck = -1;   // -1: default 0: delete 1: select
    let previousImg = profileImg.src;
    let previousFile = null;

    // 이미지 미리보기
    imageInput.addEventListener("change", () => {
        const file = imageInput.files[0];

        if(file) {
            if(file.size <= MAX_SIZE) {
                const newImg = URL.createObjectURL(file);
                profileImg.src = newImg;
                statusCheck = 1;
                previousImg = newImg;
                previousFile = file;
            } else {
                alert("최대 파일 크기는 5MB입니다.");
                imageInput.value = '';
                profileImg.src = previousImg;
                if(previousFile) {
                    const dataTransfer = new DataTransfer();
                    dataTransfer.items.add(previousFile);
                    imageInput.files = dataTransfer.files;
                }
            }
        } else{
            profileImg.src = previousImg;
            if(previousFile) {
                const dataTransfer = new DataTransfer();
                dataTransfer.items.add(previousFile);
                imageInput.files = dataTransfer.files;
            }
        }
    });

    // 이미지 삭제
    deleteImage.addEventListener("click", () => {
        if(previousImg != defaultImageUrl) {
            imageInput.value = '';
            profileImg.src = defaultImageUrl;
            statusCheck = 0;
            previousFile = null;
        } else {
            statusCheck = -1;
        }
    });

    // 이미지 업로드
    profileForm.addEventListener("submit", e => {
        if(statusCheck === -1) {
            e.preventDefault();
            alert("이미지를 선택해주세요.");
        }   
    });
}










/**
 * 주소 검색 버튼 클릭 
 */
const searchAddressBtn = document.getElementById("searchAddress");

if(searchAddressBtn) {
    searchAddressBtn.addEventListener("click", () => {
        execDaumPostcode();
    });
}

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
