package edu.kh.todo.model.service;

import java.util.Map;

import edu.kh.todo.model.dto.Todo;

public interface TodoService {

    /** TestMethod
     * @testTitle
     */
    String testTitle();

    /** todo list / completeCount
     * 
     * @return 
     */
    Map<String, Object> completeCount();

    /** 할 일 추가
     * @param todoTitle
     * @param todoContent
     * @return result
     */
    int addTodo(String todoTitle, String todoContent);

    /** 할 일 상세 조회
     * @param todoNo
     * @return todo
     */
    Todo todoDetail(int todoNo);

    /** 완료 상태 변경
     * @param todo
     * @return result
     */
    int changeComplete(Todo todo);

    /** 할 일 삭제
     * @param todoNo
     * @return result
     */
    int deleteTodo(int todoNo);

    /** 할 일 수정
     * @param todo
     * @return result
     */
    int updateTodo(Todo todo);

    /** 전체 할 일 개수 조회
     * @return totalCount
     */
    int getTotalCount();

}