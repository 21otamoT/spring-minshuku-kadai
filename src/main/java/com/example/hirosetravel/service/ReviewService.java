package com.example.hirosetravel.service;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Service;

import com.example.hirosetravel.entity.Review;
import com.example.hirosetravel.form.ReviewEditForm;
import com.example.hirosetravel.form.ReviewPostForm;
import com.example.hirosetravel.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	private final ReviewRepository REVIEW_REPOSITORY;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.REVIEW_REPOSITORY = reviewRepository;
	}
	
	@Transactional
    public void createReview(ReviewPostForm reviewPostForm,Integer houseId,Integer userId,
    		                 String userName) 
	{
    	Review review = new Review();
    	
    	review.setHouseId(houseId);
    	review.setUserId(userId);
    	review.setName(userName);
    	review.setScore(reviewPostForm.getScore());
    	review.setDescription(reviewPostForm.getComent());
    	
    	REVIEW_REPOSITORY.save(review);
    }
	
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = REVIEW_REPOSITORY.getReferenceById(reviewEditForm.getId());
		
		try {
			review.setScore(reviewEditForm.getScore());
			review.setDescription(reviewEditForm.getComent());
			
			REVIEW_REPOSITORY.save(review);
		}
		catch(BeanCreationException e) {
			System.out.println(e);
		}
	}
	
	public boolean hasUserReviewd(Integer houseId, Integer userId) {
		return REVIEW_REPOSITORY.existsByHouseIdAndUserId(houseId, userId);
	}
}
