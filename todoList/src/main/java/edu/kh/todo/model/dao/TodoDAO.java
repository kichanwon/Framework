package edu.kh.todo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.todo.model.mapper.TodoMapper;

@Repository
public class TodoDAO {

    @Autowired
    private TodoMapper mapper;

    public String testTitle() {
        return mapper.testTitle();
    }

}
