package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;


@Controller
public class ExampleController {
	
//	@GetMapping		: Get		search
//	@PostMapping	: Post		insert
//	
//	@PutMapping		: Put		modify		(form, a tag X)
//	@DeleteMapping	: Delete	deletion	(form, a tag X)
	
	@GetMapping("example")
	public String testMethod() {		
		return "example";
	}
	
}
