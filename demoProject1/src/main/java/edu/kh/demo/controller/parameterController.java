package edu.kh.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequestMapping("param")
public class parameterController {
	
	@GetMapping("main")
	public String paramMain() {
		System.out.println("/param/main requested");
		
		return "param/param_main";
	}
	
	@PostMapping("test1")
	public String paramTest1(HttpServletRequest req){
		System.out.println("/param/test1 post");

		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");
		
		log.debug("inputName	:" + inputName);
		log.debug("inputAge	:" + inputAge);
		log.debug("inputAddress	:" + inputAddress);

		return "redirect:main";
//		return "redirect:/param/main";
	}
	
	@PostMapping("test2")
	public String paramTest2(
				@RequestParam("bookTitle") String bookTitle,
				@RequestParam("bookAuthor") String bookAuthor,
				@RequestParam(value="bookPrice", required=false, defaultValue="0") int bookPrice,
				@RequestParam("bookPublisher") String bookPublisher
//				@RequestParam(value="key"[, required=false, defaultValue="1"]) Type name
				){
		
		System.out.println("/param/test2 post");
		
		log.debug("bookTitle : " + bookTitle);
		log.debug("bookAuthor : " + bookAuthor);
		log.debug("bookPrice : " + bookPrice);
		log.debug("bookPublisher : " + bookPublisher);

		return "redirect:main";
	}
	
	@PostMapping("test3")
	public String paramTest3(
				@RequestParam("color") String[] colorArr,
				@RequestParam("fruit") List<String> fruitList,
				@RequestParam Map<String, Object> paramMap
				){
				
		log.debug("colorArr : " + Arrays.toString(colorArr));
		log.debug("fruitList : " + fruitList);
		log.debug("paramMap : " + paramMap);

		return "redirect:main";
	}
	
	@PostMapping("test4")
	public String paramTest4(@ModelAttribute MemberDTO inputMember){
				
		log.debug("inputMember : " + inputMember);

		return "redirect:main";
	}
	
}