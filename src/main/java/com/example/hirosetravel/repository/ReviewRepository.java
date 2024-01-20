package com.example.hirosetravel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hirosetravel.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findTop10ByOrderByCreatedAtDesc();
}
