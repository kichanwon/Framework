package edu.kh.project.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
	
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriteDate;
	private String boardUpdateDate;
	private int readCount;
	private String boardDelFl;
	private int memberNo;
	private int boardCode;
	
	private String memberNickname;
	
	private int commentCount;
	private int likeCount;
	
	private String profileImg;
	
	private String thumbnail;
	
	private List<BoardImg> imageList;
	private List<Comment> commentList;
	
	private int likeCheck;

}
