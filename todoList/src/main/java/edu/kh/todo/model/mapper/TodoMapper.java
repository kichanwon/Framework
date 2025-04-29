package edu.kh.todo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.model.dto.Todo;

@Mapper
public interface TodoMapper {

    String testTitle();

    List<Todo> selectTodoList();

    int getCompleteCount();

    int addTodo(Todo todo);

    Todo todoDetail(int todoNo);

    int changeComplete(Todo todo);

    int deleteTodo(int todoNo);

    int updateTodo(Todo todo);

    int getTotalCount();

}
