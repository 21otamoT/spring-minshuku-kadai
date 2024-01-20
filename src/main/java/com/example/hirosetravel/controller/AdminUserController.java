package com.example.hirosetravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository USER_REPOSITORY;
    
    public AdminUserController(UserRepository USER_REPOSITORY) {
    	this.USER_REPOSITORY = USER_REPOSITORY;
    }
    
    @GetMapping
    public String index(@RequestParam(name = "keyword",required = false) String keyword,@PageableDefault(page = 0,size=10,sort="id",direction=Direction.ASC)Pageable pageable,Model model) {
        Page<User> userPage;
        
        if(keyword != null && !keyword.isEmpty()) {
        	userPage = USER_REPOSITORY.findByNameLikeOrFuriganaLike("%"+keyword+"%", "%"+keyword+"%", pageable);
        }
        else {
        	userPage = USER_REPOSITORY.findAll(pageable);
        }
        
        model.addAttribute("userPage",userPage);
        model.addAttribute("keyword",keyword);
        
    	return "admin/users/index";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
    	User user = USER_REPOSITORY.getReferenceById(id);
    	
    	model.addAttribute("user", user);
    	
    	return "admin/users/show";
    }
    
}