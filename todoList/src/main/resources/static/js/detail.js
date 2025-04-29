// const goToList = document.getElementByIdquegoToList");
const goToList = document.querySelector("#goToList");
const completeBtn = document.querySelector(".complete-btn");
const updateBtn = document.querySelector("#updateBtn");
const deleteBtn = document.querySelector("#deleteBtn");

goToList.addEventListener("click", () => {
    location.href = "/";
});


completeBtn.addEventListener("click", (e) => {
    const todoNo = e.target.dataset.todoNo;
    
    let complete = e.target.innerText;
    complete = (complete === 'Y') ? 'N' : 'Y';
    
    location.href = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
});


deleteBtn.addEventListener("click", (e) => {
    const todoNo = e.target.dataset.todoNo;
    location.href = `/todo/delete?todoNo=${todoNo}`;
});

updateBtn.addEventListener("click", (e) => {
    const todoNo = e.target.dataset.todoNo;
    location.href = `/todo/update?todoNo=${todoNo}`;
});