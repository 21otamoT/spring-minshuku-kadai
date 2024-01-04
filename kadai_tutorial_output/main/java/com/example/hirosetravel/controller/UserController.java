package com.example.hirosetravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.form.UserEditForm;
import com.example.hirosetravel.repository.UserRepository;
import com.example.hirosetravel.security.UserDetailslmpl;
import com.example.hirosetravel.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository USER_REPOSITORY;
    private final UserService USER_SERVICE;
    
    public UserController(UserRepository USER_REPOSITORY,UserService USER_SERVICE) {
    	this.USER_REPOSITORY = USER_REPOSITORY;
    	this.USER_SERVICE = USER_SERVICE;
    }
    
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailslmpl userDetailsInpl, Model model) {
    	User user = USER_REPOSITORY.getReferenceById(userDetailsInpl.getUser().getId());
    	
    	model.addAttribute("user",user);
    	
    	return "user/index";
    }
    
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailslmpl userDetailsInpl, Model model) {
    	User user = USER_REPOSITORY.getReferenceById(userDetailsInpl.getUser().getId());
    	UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(), user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
    	
    	model.addAttribute("userEditForm",userEditForm);
    	
    	return "user/edit";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm,BindingResult bindingresult,RedirectAttributes redirectAttributes) {
    	// メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
    	if (USER_SERVICE.isEmailChanged(userEditForm) && USER_SERVICE.isEmailRegistered(userEditForm.getEmail())) {
    		FieldError fieldError = new FieldError(bindingresult.getObjectName(),"email", "すでに登録済みのメールアドレスです。");
    		bindingresult.addError(fieldError);
    	}
    	
    	if (bindingresult.hasErrors()) {
    		return "user/edit";
    	}
    	
    	USER_SERVICE.update(userEditForm);
    	redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
    	
    	return "redirect:/user";
    }
}
