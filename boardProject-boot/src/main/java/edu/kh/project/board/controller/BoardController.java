package edu.kh.project.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {

	@Autowired
    private BoardService service;

	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
								  @RequestParam(value="cp", required = false, defaultValue = "1") int cp,
								  Model model) {
		
		Map<String, Object> map = null;
		
		map = service.selectBoardList(boardCode,cp);
        
        model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList";
	}


	/**
	 * 
	 * @PathVariable 이용시 변수갑이 request scope에 저장되어 이용 가능
	 * @param boardCode		: 주소에 포함된 게시판 종류 (1,2,3)
	 * @param boardNo		: 주소에 포함된 게시글 번호 (1997, 2000)
	 * 
	 * @param model			: 
	 * @param loginMember	:
	 * @param ra			:
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
							  @PathVariable("boardNo") int boardNo,
							  Model model,
							  @SessionAttribute(value="loginMember", required=false) Member loginMember,
							  RedirectAttributes ra){

		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		if(loginMember!=null) map.put("memberNo", loginMember.getMemberNo());

		Board board = service.selectOne(map);

		String path=null;
		if(board == null){
			path = "redirect:/board/"+boardCode;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		}else {
			path = "board/boardDetail";
			model.addAttribute("board",board);
			
			if(!board.getImageList().isEmpty()){
				BoardImg thumbnail = null;
				if(board.getImageList().get(0).getImgOrder()==0) thumbnail=board.getImageList().get(0);
				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1: 0);
			}
		}

		return path;
	}

}
