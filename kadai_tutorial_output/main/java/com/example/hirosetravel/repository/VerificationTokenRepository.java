package com.example.hirosetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hirosetravel.entity.VarificationToken;

public interface VerificationTokenRepository extends JpaRepository<VarificationToken, Integer> {
    public VarificationToken findByToken(String token);
}
