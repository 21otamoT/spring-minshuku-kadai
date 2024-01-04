package com.example.hirosetravel.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {
	
    public String handleError(HttpServletRequest request, Model model) {
    	// エラー処理のロジックを実装する
    	return "houses/error";
    }
}