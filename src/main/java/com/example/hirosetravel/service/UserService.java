package com.example.hirosetravel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hirosetravel.entity.Role;
import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.form.SignupForm;
import com.example.hirosetravel.form.UserEditForm;
import com.example.hirosetravel.repository.RoleRepository;
import com.example.hirosetravel.repository.UserRepository;


@Service
public class UserService {
    private final UserRepository USER_REPOSITORY;
    private final RoleRepository ROLE_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;
    
    public UserService(UserRepository USER_REPOSITORY,RoleRepository ROLE_REPOSITORY,PasswordEncoder PASSWORD_ENCODER) {
    	this.USER_REPOSITORY = USER_REPOSITORY;
    	this.ROLE_REPOSITORY = ROLE_REPOSITORY;
    	this.PASSWORD_ENCODER = PASSWORD_ENCODER;
    }
    
    @Transactional
    public User create(SignupForm signupForm) {
    	User user = new User();
    	Role role = ROLE_REPOSITORY.findByName("ROLE_GENERAL");
    	
    	user.setName(signupForm.getName());
    	user.setFurigana(signupForm.getFurigana());
    	user.setPostalCode(signupForm.getPostalCode());
    	user.setAddress(signupForm.getAddress());
    	user.setPhoneNumber(signupForm.getPhoneNumber());
    	user.setEmail(signupForm.getEmail());
    	user.setPassword(PASSWORD_ENCODER.encode(signupForm.getPassword()));
    	user.setRole(role);
    	user.setEnabled(false);
    	
    	return USER_REPOSITORY.save(user);
    }
    
    @Transactional
    public void update(UserEditForm userEditForm) {
    	User user = USER_REPOSITORY.getReferenceById(userEditForm.getId());
    	
    	user.setName(userEditForm.getName());
    	user.setFurigana(userEditForm.getFurigana());
    	user.setPostalCode(userEditForm.getPostalCode());
    	user.setAddress(userEditForm.getAddress());
    	user.setPhoneNumber(userEditForm.getPhoneNumber());
    	user.setEmail(userEditForm.getEmail());
    	
    	USER_REPOSITORY.save(user);
    }
    
    //メールアドレスが登録済みかどうかチェックする
    public boolean isEmailRegistered(String email) {
    	User user = USER_REPOSITORY.findByEmail(email);
    	return user != null;
    }
    
    // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
    	return password.equals(passwordConfirmation);
    }
    
    // ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
    	user.setEnabled(true);
    	USER_REPOSITORY.save(user);
    }
    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = USER_REPOSITORY.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());      
    }  
}
