package com.example.hirosetravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.entity.VarificationToken;
import com.example.hirosetravel.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository VERIFICATION_TOKEN_REPOSITORY;
    
    public VerificationTokenService(VerificationTokenRepository VERIFICATION_TOKEN_REPOSITORY) {
    	this.VERIFICATION_TOKEN_REPOSITORY = VERIFICATION_TOKEN_REPOSITORY;
    }
    
    @Transactional
    public void create(User user, String token) {
    	VarificationToken verificationToken = new VarificationToken();
    	
    	verificationToken.setUser(user);
    	verificationToken.setToken(token);
    	
    	VERIFICATION_TOKEN_REPOSITORY.save(verificationToken);
    }
    
    // トークンの文字列で検索した結果を返す
    public VarificationToken getVarificationToken(String token) {
    	return VERIFICATION_TOKEN_REPOSITORY.findByToken(token);
    }
}
