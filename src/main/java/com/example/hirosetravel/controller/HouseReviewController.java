package com.example.hirosetravel.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hirosetravel.entity.House;
import com.example.hirosetravel.entity.Review;
import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.form.ReviewEditForm;
import com.example.hirosetravel.form.ReviewPostForm;
import com.example.hirosetravel.repository.HouseRepository;
import com.example.hirosetravel.repository.ReviewRepository;
import com.example.hirosetravel.security.UserDetailslmpl;
import com.example.hirosetravel.service.ReviewService;

@Controller
public class HouseReviewController {
	private final HouseRepository HOUSE_REPOSITORY;
	private final ReviewRepository REVIEW_REPOSITORY;
	private final ReviewService REVIEW_SERVICE;
	
    public HouseReviewController(HouseRepository HOUSE_REPOSITORY, ReviewRepository REVIEW_REPOSITORY,
    		                     ReviewService REVIEW_SERVICE)
    {
    	this.HOUSE_REPOSITORY = HOUSE_REPOSITORY;
    	this.REVIEW_REPOSITORY = REVIEW_REPOSITORY;
    	this.REVIEW_SERVICE = REVIEW_SERVICE;
    }
	
	@GetMapping("{id}/review")
    public String review(@PathVariable(name = "id") Integer id,
    		             @PageableDefault(page=0, size=10, sort="id", direction=Direction.ASC) Pageable pageable,
    		             Model model) 
	{
		Page<Review> reviewPage = REVIEW_REPOSITORY.findAllByOrderByCreatedAtDesc(pageable);
		
		House house = HOUSE_REPOSITORY.getReferenceById(id);
    	List<Review> reviews = REVIEW_REPOSITORY.findByHouseId(id);
    	
    	model.addAttribute("reviewPage",reviewPage);
    	model.addAttribute("house", house);
    	model.addAttribute("reviews",reviews);
    	return "houses/review" ;
    }
    
    @GetMapping("/{id}/postReview")
    public String createReview(@PathVariable(name = "id") Integer id,Model model) {
    	House house = HOUSE_REPOSITORY.getReferenceById(id);
    	model.addAttribute("house", house);
    	model.addAttribute("reviewPostForm",new ReviewPostForm());
    	return "houses/postReview";
    }
    
    @PostMapping("/{id}/createReview")
    public String create(@PathVariable(name = "id") Integer id,@ModelAttribute @Validated ReviewPostForm reviewPostForm,BindingResult bindingResult,
    		             @AuthenticationPrincipal UserDetailslmpl userDetails, RedirectAttributes redirectAttributes,Model model) 
    {	
    	try {
    		User user = userDetails.getUser();
	        House house = HOUSE_REPOSITORY.getReferenceById(id);
	    	Integer userId = user.getId();
	    	String userName = user.getName();
	    	    	
	        // Review エンティティを保存
	    	REVIEW_SERVICE.createReview(reviewPostForm,id,userId,userName);
	    	
	    	if (bindingResult.hasErrors()) {
	    		model.addAttribute("house", house);
    			return "houses/postReview";
    		}
	    	
	    	redirectAttributes.addFlashAttribute("successMessage", "評価を登録しました");
    	}
    	catch(Exception e) {
    		System.err.println("user"+e);
    	}
    	
    	return "redirect:/houses/{id}" ;
    }
    
    @GetMapping("/{id}/edit/{reviewId}")
    public String edit(@PathVariable(name = "id") Integer id, Model model,
    		           @PathVariable(name = "reviewId") Integer reviewId
    		           )throws Exception 
    {
    	House house = HOUSE_REPOSITORY.getReferenceById(id);
    	Review review = REVIEW_REPOSITORY.getReferenceById(reviewId);
    	ReviewEditForm reviewEditForm = new ReviewEditForm(reviewId, review.getScore(), review.getDescription());
    	
    	model.addAttribute("house", house);
    	model.addAttribute("reviewEditForm",reviewEditForm);
    	
    	return "houses/edit" ;
    }
    
    @PostMapping("{id}/update")
    public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult,
    		             @PathVariable(name = "id") Integer id,RedirectAttributes redirectAttributes)
    {
    	try {
    	if(bindingResult.hasErrors()) {
    		System.out.println(id);
    		return "houses/edit";
    	}
    	
    	REVIEW_SERVICE.update(reviewEditForm);
    	redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました");
    	
    	
    	}
    	catch(Exception e) {
    		System.out.println("エラーです");
    	}
    	return "redirect:/houses/{id}";
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) 
    {
    	String houseId = REVIEW_REPOSITORY.getReferenceById(id).getHouseId().toString();
    	REVIEW_REPOSITORY.deleteById(id);
    	redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました！");
    	
        return "redirect:/houses/"+houseId ;
    }
}
