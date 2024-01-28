package com.example.hirosetravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
    @NotNull
    private Integer id;

    private Integer score; 
    
    @NotBlank(message = "コメントを入力して下さい")
    private String coment;
}
