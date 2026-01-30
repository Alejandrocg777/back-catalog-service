package com.asb.backCompanyService.repository;


import com.asb.backCompanyService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
