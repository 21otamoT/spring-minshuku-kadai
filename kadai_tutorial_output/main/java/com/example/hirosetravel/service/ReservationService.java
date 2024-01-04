package com.example.hirosetravel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hirosetravel.entity.House;
import com.example.hirosetravel.entity.Reservation;
import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.repository.HouseRepository;
import com.example.hirosetravel.repository.ReservationRepository;
import com.example.hirosetravel.repository.UserRepository;


@Service
public class ReservationService {
	private final ReservationRepository RESERVATION_REPOSITORY;
	private final HouseRepository HOUSE_REPOSITORY;
	private final UserRepository USER_REPOSITORY;
	
	public ReservationService(ReservationRepository RESERVATION_REPOSITORY,HouseRepository HOUSE_REPOSITORY,UserRepository USER_REPOSITORY) {
		this.RESERVATION_REPOSITORY = RESERVATION_REPOSITORY;
		this.HOUSE_REPOSITORY = HOUSE_REPOSITORY;
		this.USER_REPOSITORY = USER_REPOSITORY;
	}
	
	@Transactional
	public void create(Map<String,String> paymentIntentObject) {
		Reservation reservation = new Reservation();
		
		Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
		Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));
		
		House house = HOUSE_REPOSITORY.getReferenceById(houseId);
		User user = USER_REPOSITORY.getReferenceById(userId);
		LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
		LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
		Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
		Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));
		
		reservation.setHouse(house);
		reservation.setUser(user);
		reservation.setCheckinDate(checkinDate);
		reservation.setCheckoutDate(checkoutDate);
		reservation.setNumberOfPeople(numberOfPeople);
		reservation.setAmount(amount);

		RESERVATION_REPOSITORY.save(reservation);
	}
	
	// 宿泊人数が定員以下かどうかをチェックする
	public boolean isWithinCapacity(Integer people, Integer capacity) {
		return people <= capacity;
	}
	
	// 宿泊料金を計算する
	public Integer calculateAmout(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
		long numberOfNights = ChronoUnit.DAYS.between(checkinDate,checkoutDate);
		int amount = price * (int)numberOfNights;
		return amount;
	}
}