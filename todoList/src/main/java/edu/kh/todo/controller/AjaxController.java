package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    private TodoService service;

    @RequestMapping("/main")
    public String ajaxMain() {
        return "ajax/main";
    }

    @ResponseBody
    @GetMapping("/todoList")
    public ResponseEntity<List<Todo>> getTodoList() {
        try {
            Map<String, Object> map = service.completeCount();
            List<Todo> todoList = (List<Todo>) map.get("todoList");
            return new ResponseEntity<>(todoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 목록 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @GetMapping("/totalCount")
    public ResponseEntity<Integer> getTotalCount() {
        try {
            int count = service.getTotalCount();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            log.error("전체 할 일 개수 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @GetMapping("/completeCount")
    public ResponseEntity<Integer> getCompleteCount() {
        try {
            Map<String, Object> map = service.completeCount();
            int completeCount = (int) map.get("completeCount");
            return new ResponseEntity<>(completeCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("완료된 할 일 개수 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/addTodo")
    public ResponseEntity<Integer> addTodo(@RequestBody Todo todo) {
        try {
            if (todo.getTodoTitle() == null || todo.getTodoTitle().trim().isEmpty() ||
                todo.getTodoContent() == null || todo.getTodoContent().trim().isEmpty()) {
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
            }

            int result = service.addTodo(todo.getTodoTitle(), todo.getTodoContent());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 추가 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @GetMapping("/todoDetail")
    public ResponseEntity<Todo> getTodoDetail(@RequestParam("todoNo") int todoNo) {
        try {
            if (todoNo <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Todo todo = service.todoDetail(todoNo);
            if (todo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 상세 조회 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PutMapping("/changeComplete")
    public ResponseEntity<Integer> changeComplete(@RequestBody Todo todo) {
        try {
            if (todo.getTodoNo() <= 0 || todo.getComplete() == null) {
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
            }

            int result = service.changeComplete(todo);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("완료 여부 변경 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PutMapping("/updateTodo")
    public ResponseEntity<Integer> updateTodo(@RequestBody Todo todo) {
        try {
            if (todo.getTodoNo() <= 0 || 
                todo.getTodoTitle() == null || todo.getTodoTitle().trim().isEmpty() ||
                todo.getTodoContent() == null || todo.getTodoContent().trim().isEmpty()) {
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
            }

            int result = service.updateTodo(todo);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 수정 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteTodo")
    public ResponseEntity<Integer> deleteTodo(@RequestParam("todoNo") int todoNo) {
        try {
            if (todoNo <= 0) {
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
            }

            int result = service.deleteTodo(todoNo);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("할 일 삭제 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}