package com.example.hirosetravel.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewPostForm {
	
    @NotNull(message = "評価を入力して下さい")
    @Min(value=0)
    private Integer score; 
    
    @NotBlank(message = "コメントを入力して下さい")
    private String coment;
}