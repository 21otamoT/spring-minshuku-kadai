package com.example.hirosetravel.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hirosetravel.entity.House;
import com.example.hirosetravel.entity.Reservation;
import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.form.ReservationInputForm;
import com.example.hirosetravel.form.ReservationRegisterForm;
import com.example.hirosetravel.repository.HouseRepository;
import com.example.hirosetravel.repository.ReservationRepository;
import com.example.hirosetravel.security.UserDetailslmpl;
import com.example.hirosetravel.service.ReservationService;
import com.example.hirosetravel.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservationController {
    private final ReservationRepository RESERVATION_REPOSITORY;
    private final HouseRepository HOUSE_REPOSITORY;
    private final ReservationService RESERVATION_SERVICE;
    private final StripeService STRIPE_SERVICE;
    
    public ReservationController(ReservationRepository RESERVATION_REPOSITORY,HouseRepository HOUSE_REPOSITORY,ReservationService RESERVATION_SERVICE,StripeService STRIPE_SERVICE) {
    	this.RESERVATION_REPOSITORY = RESERVATION_REPOSITORY;
    	this.HOUSE_REPOSITORY = HOUSE_REPOSITORY;
    	this.RESERVATION_SERVICE = RESERVATION_SERVICE;
    	this.STRIPE_SERVICE = STRIPE_SERVICE;
    }
    
    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailslmpl userDetailsImpl, @PageableDefault(page = 0,size = 10,sort = "id",direction = Direction.ASC) Pageable pageable,Model model) {
    	User user = userDetailsImpl.getUser();
    	Page<Reservation> reservationPage = RESERVATION_REPOSITORY.findByUserOrderByCreatedAtDesc(user, pageable);
    	
    	model.addAttribute("reservationPage",reservationPage);
    	
    	return "reservations/index";
    }
    
    @GetMapping("/houses/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
    		            @ModelAttribute @Validated ReservationInputForm reservationInputForm,
    		            BindingResult bindingResult,
    		            RedirectAttributes redirectAttributes,
    		            Model model) 
    {
    	House house = HOUSE_REPOSITORY.getReferenceById(id);
    	Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
    	Integer capacity = house.getCapacity();
    	
    	if(numberOfPeople != null) {
    		if(!RESERVATION_SERVICE.isWithinCapacity(numberOfPeople, capacity)) {
    			FieldError filedError = new FieldError(bindingResult.getObjectName(),"numberOfPeople","宿泊人数が定員を超えています。");
    			bindingResult.addError(filedError);
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("house",house);
    		model.addAttribute("errorMessage","予約内容に不備があります。");
    		return "houses/show";
    	}
    	
    	redirectAttributes.addFlashAttribute("reservationInputForm",reservationInputForm);
    	
    	return "redirect:/houses/{id}/reservations/confirm";
    }
    
    @GetMapping("/houses/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
    		              @ModelAttribute ReservationInputForm reservationInputForm,
    		              @AuthenticationPrincipal UserDetailslmpl userDetailslmpl,
    		              HttpServletRequest httpServleRequest,
    		              Model model)
    {
    	House house = HOUSE_REPOSITORY.getReferenceById(id);
    	User user = userDetailslmpl.getUser();
    	
    	//チェックイン日とチェックアウト日を取得する
    	LocalDate checkinDate = reservationInputForm.getCheckinDate();
    	LocalDate checkoutDate = reservationInputForm.getCheckoutDate();
    	
    	// 宿泊料金を計算する
    	Integer price = house.getPrice();
    	Integer amount = RESERVATION_SERVICE.calculateAmout(checkinDate, checkoutDate, price);
    	
    	ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(house.getId(),user.getId(),checkinDate.toString(),checkoutDate.toString(),reservationInputForm.getNumberOfPeople(),amount);
    	
    	String sessionId = STRIPE_SERVICE.createStripeSession(house.getName(), reservationRegisterForm, httpServleRequest);
    	
    	model.addAttribute("house",house);
    	model.addAttribute("reservationRegisterForm",reservationRegisterForm);
    	model.addAttribute("sessionId",sessionId);
    	
    	return "reservations/confirm";
    }
    /*
    @PostMapping("/houses/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {
    	RESERVATION_SERVICE.create(reservationRegisterForm);
    	
    	return "redirect:/reservations?reserved";
    }
    */
}