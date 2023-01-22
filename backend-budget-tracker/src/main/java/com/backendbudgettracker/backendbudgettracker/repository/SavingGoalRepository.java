package com.backendbudgettracker.backendbudgettracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backendbudgettracker.backendbudgettracker.entity.SavingGoal;

public interface SavingGoalRepository extends JpaRepository<SavingGoal, Long> {

}