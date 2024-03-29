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

import com.example.hirosetravel.entity.House;
import com.example.hirosetravel.form.ReservationInputForm;
import com.example.hirosetravel.repository.HouseRepository;

@Controller
@RequestMapping("/houses")
public class HouseController {
    private final HouseRepository HOUSE_REPOSITORY;
    
    public HouseController(HouseRepository HOUSE_REPOSITORY) {
    	this.HOUSE_REPOSITORY = HOUSE_REPOSITORY;
    }
    
    @GetMapping
    public  String index(@RequestParam(name = "keyword", required = false) String keyword,
    		             @RequestParam(name = "area", required = false) String area,
    		             @RequestParam(name = "price", required = false) Integer price,
    		             @RequestParam(name = "order", required = false) String order,
    		             @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
    		             Model model) 
    {
    	Page<House> housePage;
    	
    	if (keyword != null && !keyword.isEmpty()) {
    		if (order != null && order.equals("priceAsc")) {
    			housePage = HOUSE_REPOSITORY.findByNameLikeOrAddressLikeOrderByPriceAsc("%"+keyword+"%", "%"+keyword+"%", pageable);
    		}
    		else {
        		housePage = HOUSE_REPOSITORY.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%"+keyword+"%", "%"+keyword+"%", pageable);
        	}
    	}
    	
    	else if (area != null && !area.isEmpty()) {
    		if (order != null && order.equals("priceAsc")) {
    			housePage = HOUSE_REPOSITORY.findByAddressLikeOrderByPriceAsc("%"+area+"%",pageable);
    		}
    		else {
    			housePage = HOUSE_REPOSITORY.findByAddressLikeOrderByCreatedAtDesc("%"+area+"%", pageable);
    		}
    	}
    	
    	else if (price != null) {
    		if (order != null && order.equals("priceAsc")) {
    			housePage = HOUSE_REPOSITORY.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
    		}
    		else {
    			housePage = HOUSE_REPOSITORY.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
    		}
    	}
    	else {
    		if (order != null && order.equals("priceAsc")) {
    			housePage = HOUSE_REPOSITORY.findAllByOrderByPriceAsc(pageable);
    		}
    		else {
    			housePage = HOUSE_REPOSITORY.findAllByOrderByCreatedAtDesc(pageable);
    		}
    	}
    	
    	model.addAttribute("housePage",housePage);
    	model.addAttribute("keyword",keyword);
    	model.addAttribute("area",area);
    	model.addAttribute("price",price);
    	model.addAttribute("order",order);
    	
    	return "houses/index";
    }
    
    @GetMapping("{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
    	House house = HOUSE_REPOSITORY.getReferenceById(id);
    	
    	model.addAttribute("house",house);
    	model.addAttribute("reservationInputForm",new ReservationInputForm());
    	
    	return "houses/show";
    }
}
