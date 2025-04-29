package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.model.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("example")
@Slf4j
public class ExampleController {
	
	@RequestMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
//												org.springframework.ui.Model
		req.setAttribute("test1", "httpservletrequest");
		req.setAttribute("test2", "model");

		model.addAttribute("productName", "cup");
		model.addAttribute("price", 2000);

		List<String> fruitList = new ArrayList<>();
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList", fruitList);
		
		Student std = new Student("1", "홍길동", 20);

		model.addAttribute("std", std);

		List<Student> stdList = new ArrayList<>();
		stdList.add(new Student("1", "일", 20));
		stdList.add(new Student("2", "이", 21));
		stdList.add(new Student("3", "삼", 22));

		model.addAttribute("stdList", stdList);

		return "example/ex1";
	}
	
	@PostMapping("ex2")
	public String ex2(Model model) {
		model.addAttribute("str","<h1>test &times;</h1>");
		
		return "example/ex2";
	}
	
	@GetMapping("ex3")
	public String ex3(Model model	) {
		model.addAttribute("key", "title");
		model.addAttribute("query", "query	");
		model.addAttribute("boardNo", 10);
				
		return "example/ex3";
	}

	@GetMapping("ex3/{path:[0-9]+}")
	public String pathVariableTest(@PathVariable("path") int path) {
		log.debug("path : {}", path);

		return "example/testResult";
	}

	
	@GetMapping("ex4")
	public String ex4(Model model) {
		Student std = new Student("41446", "→", 20);
		model.addAttribute("std", std);
		model.addAttribute("num", 100);
		
		return "example/ex4";
	}
	
	@GetMapping	("ex5")
	public String ex5(Model model) {
		model.addAttribute("message", "Thymeleaf + Javascript");
		model.addAttribute("num1", 12345);

		Student std = new Student();
		std.setStudentNo("41446");

		model.addAttribute("std", std);

		return "example/ex5";
	}
}
