// console.log("ajax-main.js 실행");

// fetch API
// 브라우저에서 제공하는 네트워크 API
// 비동기 통신을 위한 인터페이스

// Promise
// 어떤 결과가 올지는 모르지만 결과를 보내주는 메서드

// pending : 대기
// fulfilled : 이행
// rejected : 거절

// 전역 변수 선언
const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");
const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");
const tbody = document.querySelector("#tbody");
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");
const changeComplete = document.querySelector("#changeComplete");
const updateView = document.querySelector("#updateView");
const deleteBtn = document.querySelector("#deleteBtn");
const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");

// 에러 처리 함수
const handleError = (error) => {
    console.error("Error:", error);
    alert("오류가 발생했습니다. 다시 시도해주세요.");
};

// 전체 할 일 개수 조회
const getTotalCount = () => {
    fetch("/ajax/totalCount")
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.text();
        })
        .then(result => totalCount.innerText = result)
        .catch(handleError);
};

// 완료된 할 일 개수 조회
const getCompleteCount = () => {
    fetch("/ajax/completeCount")
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.text();
        })
        .then(result => completeCount.innerText = result)
        .catch(handleError);
};

// 새로고침 버튼 클릭 이벤트
reloadBtn.addEventListener("click", () => {
    getTotalCount();
    getCompleteCount();
    getTodoList();
});

// 할 일 상세 조회
const getTodoDetail = (todoNo) => {
    fetch(`/ajax/todoDetail?todoNo=${encodeURIComponent(todoNo)}`)
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.json();
        })
        .then(result => {
            popupLayer.classList.remove("popup-hidden");
            popupTodoNo.innerText = result.todoNo;
            popupTodoTitle.innerText = result.todoTitle;
            popupComplete.innerText = result.complete;
            popupRegDate.innerText = result.regDate;
            popupTodoContent.innerText = result.todoContent;
            
            // 완료 여부 변경 버튼에 todoNo 설정
            changeComplete.dataset.todoNo = result.todoNo;
        })
        .catch(handleError);
};

// 팝업 닫기
popupClose.addEventListener("click", () => {
    popupLayer.classList.add("popup-hidden");
});

// 할 일 목록 조회
const getTodoList = () => {
    fetch("/ajax/todoList")
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.json();
        })
        .then(result => {
            tbody.innerHTML = "";
            result.forEach(todo => {
                tbody.innerHTML += `
                <tr>
                    <td>${todo.todoNo}</td>
                    <td><a href="javascript:void(0)" onclick="getTodoDetail(${todo.todoNo})">${todo.todoTitle}</a></td>
                    <td>${todo.complete}</td>
                    <td>${todo.regDate}</td>
                </tr>
                `;
            });
        })
        .catch(handleError);
};

// 할 일 추가
addBtn.addEventListener("click", () => {
    const title = todoTitle.value.trim();
    const content = todoContent.value.trim();

    if (!title || !content) {
        alert("제목과 내용을 모두 입력해주세요.");
        return;
    }

    fetch("/ajax/addTodo", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            todoTitle: title,
            todoContent: content
        })
    })
    .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
    })
    .then(result => {
        if (result > 0) {
            alert("할 일이 추가되었습니다.");
            todoTitle.value = "";
            todoContent.value = "";
            getTodoList();
            getTotalCount();
        } else {
            alert("할 일 추가에 실패했습니다.");
        }
    })
    .catch(handleError);
});

// 완료 여부 변경
changeComplete.addEventListener("click", () => {
    const todoNo = changeComplete.dataset.todoNo;
    if (!todoNo) {
        alert("할 일을 선택해주세요.");
        return;
    }

    let Complete = popupComplete.innerText;
    Complete = Complete === 'Y' ? 'N' : 'Y';

    fetch("/ajax/changeComplete", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            todoNo: todoNo,
            complete: Complete
        })
    })
    .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
    })
    .then(result => {
        if (result > 0) {
            popupComplete.innerText = Complete;
            getTodoList();
            getCompleteCount();
        } else {
            alert("완료 여부 변경에 실패했습니다.");
        }
    })
    .catch(handleError);
});

// 수정 버튼 클릭 시 수정 레이어 표시
updateView.addEventListener("click", () => {
    updateLayer.classList.remove("popup-hidden");
    updateTitle.value = popupTodoTitle.innerText;
    updateContent.value = popupTodoContent.innerText;
});

// 수정 취소
updateCancel.addEventListener("click", () => {
    updateLayer.classList.add("popup-hidden");
});

// 할 일 수정
updateBtn.addEventListener("click", () => {
    const title = updateTitle.value.trim();
    const content = updateContent.value.trim();
    const todoNo = popupTodoNo.innerText;

    if (!title || !content) {
        alert("제목과 내용을 모두 입력해주세요.");
        return;
    }

    fetch("/ajax/updateTodo", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            todoNo: todoNo,
            todoTitle: title,
            todoContent: content
        })
    })
    .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
    })
    .then(result => {
        if (result > 0) {
            alert("할 일이 수정되었습니다.");
            popupTodoTitle.innerText = title;
            popupTodoContent.innerText = content;
            updateLayer.classList.add("popup-hidden");
            getTodoList();
        } else {
            alert("할 일 수정에 실패했습니다.");
        }
    })
    .catch(handleError);
});

// 할 일 삭제
deleteBtn.addEventListener("click", () => {
    const todoNo = popupTodoNo.innerText;
    
    if (!confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    fetch(`/ajax/deleteTodo?todoNo=${encodeURIComponent(todoNo)}`, {
        method: "DELETE"
    })
    .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
    })
    .then(result => {
        if (result > 0) {
            alert("할 일이 삭제되었습니다.");
            popupLayer.classList.add("popup-hidden");
            getTodoList();
            getTotalCount();
            getCompleteCount();
        } else {
            alert("할 일 삭제에 실패했습니다.");
        }
    })
    .catch(handleError);
});

// 초기 데이터 로드
document.addEventListener("DOMContentLoaded", () => {
    getTotalCount();
    getCompleteCount();
    getTodoList();
});



