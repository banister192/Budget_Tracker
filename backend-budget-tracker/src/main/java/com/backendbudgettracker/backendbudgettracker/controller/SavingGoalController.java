package com.backendbudgettracker.backendbudgettracker.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendbudgettracker.backendbudgettracker.entity.SavingGoal;
import com.backendbudgettracker.backendbudgettracker.repository.SavingGoalRepository;
import com.backendbudgettracker.backendbudgettracker.security.JwtHelper;

@RestController
@RequestMapping("/savinggoal")
@CrossOrigin
public class SavingGoalController {

    private SavingGoalRepository savinggoalRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public SavingGoalController(SavingGoalRepository savinggoalRepository) {
        this.savinggoalRepository = savinggoalRepository;
    }

    @GetMapping("/getSavingGoal")
    public SavingGoal getSavingGoal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = jwtHelper.getUserIdFromRequest(request);
        if (userId != null) {
            Optional<SavingGoal> savinggoal = savinggoalRepository.findById(userId);
            if (savinggoal.isPresent()) {
                return savinggoal.get();
            }
        }
        response.sendError(401);
        return null;
    }

    @PostMapping("/setSavingGoal")
    public SavingGoal setSavingGoal(@RequestBody Map<String, Object> payload, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        Long userId = jwtHelper.getUserIdFromRequest(request);
        if (userId != null) {
            Optional<SavingGoal> savinggoal = savinggoalRepository.findById(userId);
            if (savinggoal.isPresent()) {
                SavingGoal savinggoalEntity = savinggoal.get();
                String amount = (String) payload.get("amount");
                double amountDouble = Double.parseDouble(amount);
                savinggoalEntity.setAmount(amountDouble);
                savinggoalEntity.setTitle((String) payload.get("title"));
                savinggoalRepository.save(savinggoalEntity);
                return savinggoalEntity;
            } else {
                SavingGoal savinggoalEntity = new SavingGoal();
                savinggoalEntity.setUserId(userId);
                String amount = (String) payload.get("amount");
                double amountDouble = Double.parseDouble(amount);
                savinggoalEntity.setAmount(amountDouble);
                savinggoalEntity.setTitle((String) payload.get("title"));
                savinggoalRepository.save(savinggoalEntity);
                return savinggoalEntity;
            }
        }
        response.sendError(401);
        return null;
    }
}