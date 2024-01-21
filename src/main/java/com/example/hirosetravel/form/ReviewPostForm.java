package com.example.hirosetravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewPostForm {
    @NotNull(message = "評価を入力して下さい")
    @Size(min=0, max=5)
    private Integer score; 
    
    @NotBlank(message = "コメントを入力して下さい")
    private String coment;
}