package com.example.hirosetravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hirosetravel.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findTop10ByOrderByCreatedAtDesc();

	public List<Review> findByHouseId(Integer id);
	
	public boolean existsByHouseIdAndUserId(Integer houseId,Integer userId);

	public Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
