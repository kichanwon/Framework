package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class TestController {
	
//	@GetMapping
//	@PostMapping
//	@RequestMapping(value="/test", method = RequestMethod.GET)
	
	@RequestMapping("/test")
	public String testMethod() {
		System.out.println("/test requested");
		
//		src/main/resources/templates/ + test + .html
		return "test";
	}
	
	
	
/*
	@RequestMapping("/hello")
	public class method{
		@RequestMapping("/world")
		public String method1() {
			return "/hello/world";
		}
		@RequestMapping("/friend")
		public String method2() {
			return "/hello/friend";
		}
		@RequestMapping("/user")
		public String method3() {
			return "/hello/user";
		}
	}
*/
	
	
}
