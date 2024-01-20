package com.example.hirosetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hirosetravel.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    public Role findByName(String name);
}
