package com.mbooking.repository;

import com.mbooking.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Page<Admin> findByFirstnameContainingAndLastnameContainingAndEmailContaining(String firstname, String lastname, String email, Pageable pageable);
}
