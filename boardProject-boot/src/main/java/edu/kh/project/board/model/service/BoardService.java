package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.board.model.dto.Board;

public interface BoardService {

	List<Map<String, Object>> selectBoardTypeList();

	Map<String, Object> selectBoardList(int boardCode, int cp);

    Board selectOne(Map<String, Integer> map);

}
