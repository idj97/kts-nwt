package com.mbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.Layout;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    List<Layout> findByNameContaining(String name);
}
