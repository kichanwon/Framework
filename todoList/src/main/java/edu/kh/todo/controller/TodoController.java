package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService service;

    /**
     * 할 일 추가
     * @param todoTitle
     * @param todoContent
     * @param ra
     * @return
     */
    @RequestMapping("/add")
    public String addTodo(
            @RequestParam("todoTitle") String todoTitle,
            @RequestParam("todoContent") String todoContent,
            // @ModelAttribute Todo todo        // ← 두 방법 중 하나 선택 ← modelattribute이 더 좋음
            RedirectAttributes ra
        ) {
        
        int result = service.addTodo(todoTitle, todoContent);

        String message=null;
        if(result > 0)  message = "할 일 추가 성공!!!";
        else            message = "할 일 추가 실패...";
        ra.addFlashAttribute("message", message);

        return "redirect:/";
    }

    /**
     * 할 일 상세 조회
     * @param todoNo
     * @param model
     * @param ra
     * @return
     */
    @GetMapping("/detail")
    public String detail(
        @RequestParam("todoNo") int todoNo,
        Model model,
        RedirectAttributes ra
    ) {

        Todo todo = service.todoDetail(todoNo);
        
        String path = null;
        if(todo!=null){
            path = "todo/detail";
            model.addAttribute("todo", todo);
        }else{
            path = "redirect:/";
            ra.addFlashAttribute("message", "존재하지 않는 할 일입니다.");
        }
        
        return path;
    }

    /**
     * 완료 상태 변경
     * @param todo
     * @param ra
     * @return
     */
    @GetMapping("/changeComplete")
    public String changeComplete(
       /*@ModelAttribute 생략가능*/ Todo todo,
        RedirectAttributes ra
    ) {
        int result = service.changeComplete(todo);

        String message = null;
        if(result > 0) message = "완료 상태 변경 성공!!!";
        else            message = "완료 상태 변경 실패...";
        ra.addFlashAttribute("message", message);

        return "redirect:/todo/detail?todoNo=" + todo.getTodoNo();
    }

    /**
     * 할 일 삭제
     * @param todoNo
     * @param ra
     * @return
     */
    @GetMapping("/delete")
    public String delete(
        @RequestParam("todoNo") int todoNo,
        RedirectAttributes ra
    ) {
        int result = service.deleteTodo(todoNo);

        String message = null;
        if(result > 0) message = "할 일 삭제 성공!!!";
        else            message = "할 일 삭제 실패...";
        ra.addFlashAttribute("message", message);
        return "redirect:/";
    }

    /**
     * 할 일 수정
     * @param todoNo
     * @param model
     * @param ra
     * @return
     */
    @GetMapping("/update")
    public String update(
        @RequestParam("todoNo") int todoNo,
        Model model
    ) {
        Todo todo = service.todoDetail(todoNo);
        model.addAttribute("todo", todo);
        
        return "todo/update";
    }

    /**
     * 할 일 수정
     * @param todo
     * @param ra
     * @return
     */
    @PostMapping("/update")
    public String update(
        Todo todo,
        RedirectAttributes ra
    ) {
        int result = service.updateTodo(todo);
     
        String message = null;
        if(result > 0) message = "할 일 수정 완료!!!";
        else            message = "할 일 수정 실패...";
        ra.addFlashAttribute("message", message);

        return "redirect:/todo/detail?todoNo=" + todo.getTodoNo();
    }
}