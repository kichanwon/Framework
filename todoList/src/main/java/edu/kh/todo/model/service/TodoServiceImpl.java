package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dao.TodoDAO;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoDAO dao;

    @Override
    public String testTitle() {
        return dao.testTitle();
    }

//-----------------------------------------------------------------------------

    @Autowired
    private TodoMapper mapper;

    @Override
    public Map<String, Object> completeCount() {
        List<Todo> todoList = mapper.selectTodoList();
        int completeCount = mapper.getCompleteCount();

        Map<String, Object> map = new HashMap<>();
        map.put("todoList", todoList);
        map.put("completeCount", completeCount);

        return map;
    }

    @Override
    public int addTodo(String todoTitle, String todoContent) {
        Todo todo = Todo.builder()
            .todoTitle(todoTitle)
            .todoContent(todoContent)
            .build();

        return mapper.addTodo(todo);
    }

    @Override
    public Todo todoDetail(int todoNo) {
        return mapper.todoDetail(todoNo);
    }

    @Override
    public int changeComplete(Todo todo) {
        return mapper.changeComplete(todo);
    }

    @Override
    public int deleteTodo(int todoNo) {
        return mapper.deleteTodo(todoNo);
    }

    @Override
    public int updateTodo(Todo todo) {
        return mapper.updateTodo(todo);
    }

    @Override
    public int getTotalCount() {
        return mapper.getTotalCount();
    }

}
