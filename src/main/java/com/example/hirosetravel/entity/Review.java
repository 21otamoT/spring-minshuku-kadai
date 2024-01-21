package com.example.hirosetravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Integer id;
   
   @ManyToOne
   @JoinColumn(name = "house_id")
   private House house;
   
   @OneToOne
   @JoinColumn(name = "user_id")
   private User user;
   
   @Column(name = "name")
   private String name;
   
   @Column(name = "description")
   private String description;
   
   @Column(name = "score")
   private int score;
   
   @Column(name = "created_at", insertable = false, updatable = false)
   private Timestamp createdAt;
	
   @Column(name = "updated_at", insertable = false, updatable = false)
   private Timestamp updatedAt;
}