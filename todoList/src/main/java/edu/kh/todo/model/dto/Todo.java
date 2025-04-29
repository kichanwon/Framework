package edu.kh.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
	private int todoNo;			// 할일 번호
	private String todoTitle;	// 할일 제목
	private String todoContent;	// 할일 내용
	private String complete;	// 완료 여부 (Y/N)
	private String regDate;		// 작성일
}
