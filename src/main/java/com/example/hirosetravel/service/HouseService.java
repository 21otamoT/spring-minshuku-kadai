package com.example.hirosetravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hirosetravel.entity.House;
import com.example.hirosetravel.entity.Review;
import com.example.hirosetravel.form.HouseEditForm;
import com.example.hirosetravel.form.HouseRegisterForm;
import com.example.hirosetravel.form.ReviewPostForm;
import com.example.hirosetravel.repository.HouseRepository;
import com.example.hirosetravel.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class HouseService {
    private final HouseRepository houseRepository;
    private final ReviewRepository reviewRepository;
    
    public HouseService(HouseRepository houseRepository,ReviewRepository reviewRepository) {
    	this.houseRepository = houseRepository;
    	this.reviewRepository = reviewRepository;
    }
    
    @Transactional
    public void create(HouseRegisterForm houseRegisterForm) {
    	House house = new House();
    	MultipartFile imageFile = houseRegisterForm.getImageFile();
    	
    	if(!imageFile.isEmpty()) {
    		String imageName = imageFile.getOriginalFilename();
    		String hashedImageName = generateNewFileName(imageName);
    		Path filePath = Paths.get("src/main/resources/static/storage/"+ hashedImageName);
    		copyImageFile(imageFile, filePath);
    		house.setImageName(hashedImageName);
    	}
    	
    	house.setName(houseRegisterForm.getName());
    	house.setDescription(houseRegisterForm.getDescription());
    	house.setPrice(houseRegisterForm.getPrice());
    	house.setCapacity(houseRegisterForm.getCapacity());
    	house.setPostalCode(houseRegisterForm.getPostalCode());
    	house.setAddress(houseRegisterForm.getAddress());
    	house.setPhoneNumber(houseRegisterForm.getPhoneNumber());
    	
    	houseRepository.save(house);
    }
    
    @Transactional
    public void update(HouseEditForm houseEditForm) {
    	House house = houseRepository.getReferenceById(houseEditForm.getId());
    	MultipartFile imageFile = houseEditForm.getImageFile();
    	
    	if (!imageFile.isEmpty()) {
    		String imageName = imageFile.getOriginalFilename();
    		String hashedImageName = generateNewFileName(imageName);
    		Path filePath = Paths.get("src/main/resources/static/storage/"+ hashedImageName);
    		copyImageFile(imageFile, filePath);
    		house.setImageName(hashedImageName);
    	}
    	
    	house.setName(houseEditForm.getName());
    	house.setDescription(houseEditForm.getDescription());
    	house.setPrice(houseEditForm.getPrice());
    	house.setCapacity(houseEditForm.getCapacity());
    	house.setPostalCode(houseEditForm.getPostalCode());
    	house.setAddress(houseEditForm.getAddress());
    	house.setPhoneNumber(houseEditForm.getPhoneNumber());
    	
    	houseRepository.save(house);
    }
    
    @Transactional
    public void createReview(ReviewPostForm reviewPostForm) {
    	Review review = new Review();
    	review.setScore(reviewPostForm.getScore());
    	review.setDescription(reviewPostForm.getComent());
    	
    	reviewRepository.save(review);
    }
    
	public String generateNewFileName(String imageName) {
		String[] imageNames = imageName.split("\\.");
		for (int i = 0;i < imageNames.length - 1; i++) {
			imageNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".",imageNames);
		return hashedFileName;
	}
	
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(),filePath);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}