package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import edu.kh.project.board.model.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper mapper;
	
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
	}
	
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {

		int listCount = mapper.getListCount(boardCode);
		
		Pagination pagination = new Pagination(cp, listCount);

		int limit = pagination.getLimit();
		int offset = (cp- 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);

		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
//		log.debug("boardList : {}", boardList);

		Map<String, Object> map = new HashMap<>();
		map.put("boardList", boardList);
		map.put("pagination", pagination);

		return map;
	}

	@Override
	public Board selectOne(Map<String, Integer> map) {
		return mapper.selectOne(map);
	}

	
	
}
