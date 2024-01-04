package com.example.hirosetravel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.hirosetravel.entity.House;
import com.example.hirosetravel.repository.HouseRepository;

@Controller
public class HomeController {
	private final HouseRepository HOUSE_REPOSITORY;
	
	public HomeController(HouseRepository HOUSE_REPOSITORY) {
		this.HOUSE_REPOSITORY = HOUSE_REPOSITORY;
	}
	
	@GetMapping("/")
    public String index(Model model) {
		List<House> newHouses = HOUSE_REPOSITORY.findTop10ByOrderByCreatedAtDesc();
		model.addAttribute("newHouses",newHouses);
		
    	return "index";
    }
}
