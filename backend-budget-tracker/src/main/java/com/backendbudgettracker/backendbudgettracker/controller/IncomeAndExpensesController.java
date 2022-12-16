package com.backendbudgettracker.backendbudgettracker.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
import com.backendbudgettracker.backendbudgettracker.entity.IncomeAndExpenses;
import com.backendbudgettracker.backendbudgettracker.entity.IncomeAndExpenses.Category;
import com.backendbudgettracker.backendbudgettracker.repository.IncomeAndExpensesRepository;
import com.backendbudgettracker.backendbudgettracker.security.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/incomeAndExpenses")
@CrossOrigin
public class IncomeAndExpensesController {

    private IncomeAndExpensesRepository incomeAndExpensesRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public IncomeAndExpensesController(IncomeAndExpensesRepository incomeAndExpensesRepository) {
        this.incomeAndExpensesRepository = incomeAndExpensesRepository;
    }

    @GetMapping("")
    public List<IncomeAndExpenses> getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = jwtHelper.getUserIdFromRequest(request);
        if (userId != null) {
            List<IncomeAndExpenses> incomeAndExpenses = incomeAndExpensesRepository.findAll();
            List<IncomeAndExpenses> tempList = new ArrayList<IncomeAndExpenses>();
            for (IncomeAndExpenses incomeAndExpenses2 : incomeAndExpenses) {
                if (incomeAndExpenses2.getUserId() == userId) {
                    tempList.add(incomeAndExpenses2);
                }
            }
            return tempList;
        }
        response.sendError(401);
        return null;
    }

    /*
     * @PostMapping("")
     * public IncomeAndExpenses create(@RequestBody Map<String, String> body,
     * HttpServletRequest request,
     * HttpServletResponse response) throws IOException {
     * Long userId = jwtHelper.getUserIdFromRequest(request);
     * if (userId != null) {
     * String title = body.get("name");
     * Double amount = Double.parseDouble(body.get("amount"));
     * String date = body.get("date");
     * Category category = Category.OTHER;
     * IncomeAndExpenses incomeAndExpenses = new IncomeAndExpenses();
     * incomeAndExpenses.setUserId(userId);
     * incomeAndExpenses.setTitle(title);
     * incomeAndExpenses.setAmount(amount);
     * incomeAndExpenses.setDate(Timestamp.valueOf(date));
     * incomeAndExpenses.setCategory(category);
     * if(amount > 0) {
     * incomeAndExpenses.setType(IncomeAndExpenses.Type.INCOME);
     * } else {
     * incomeAndExpenses.setType(IncomeAndExpenses.Type.EXPENSE);
     * }
     * return incomeAndExpensesRepository.save(incomeAndExpenses);
     * }
     * response.sendError(401);
     * return null;
     * }
     */

    @PostMapping("")
    public List<IncomeAndExpenses> create(@RequestBody List<Map<String, String>> body, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ParseException {
        Long userId = jwtHelper.getUserIdFromRequest(request);
        if (userId != null) {
            List<IncomeAndExpenses> incomeAndExpensesList = new ArrayList<IncomeAndExpenses>();
            for (Map<String, String> map : body) {
                IncomeAndExpenses incomeAndExpenses = new IncomeAndExpenses();

                String title = map.get("title");

                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                Number number = format.parse(map.get("amount"));
                double d = number.doubleValue();
                if (d > 0) {
                    incomeAndExpenses.setType(IncomeAndExpenses.Type.INCOME);
                } else {
                    incomeAndExpenses.setType(IncomeAndExpenses.Type.EXPENSE);
                }
                if (d < 0) {
                    d = d * -1;
                }
                Double amount = d;
                Category category = Category.OTHER;
                incomeAndExpenses.setUserId(userId);
                incomeAndExpenses.setTitle(title);
                incomeAndExpenses.setAmount(amount);
                Timestamp date = Timestamp.valueOf(map.get("dateISO").substring(0, 10) + " 00:00:00");
                incomeAndExpenses.setDate(date);
                incomeAndExpenses.setCategory(category);
                
                incomeAndExpensesList.add(incomeAndExpenses);
            }
            return incomeAndExpensesRepository.saveAll(incomeAndExpensesList);
        }
        response.sendError(401);
        return null;
    }

    @PostMapping("/createSingle")
    public IncomeAndExpenses createSingle(@RequestBody Map<String, String> body, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ParseException {
        Long userId = jwtHelper.getUserIdFromRequest(request);
        if (userId != null) {
            IncomeAndExpenses incomeAndExpenses = new IncomeAndExpenses();
            String title = body.get("title");
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(body.get("amount"));
            double d = number.doubleValue();
            if (d > 0) {
                incomeAndExpenses.setType(IncomeAndExpenses.Type.INCOME);
            } else {
                incomeAndExpenses.setType(IncomeAndExpenses.Type.EXPENSE);
            }
            if (d < 0) {
                d = d * -1;
            }
            Double amount = d;
            String categoryString = body.get("category");
            Category category = Category.valueOf(categoryString);
            incomeAndExpenses.setUserId(userId);
            incomeAndExpenses.setTitle(title);
            incomeAndExpenses.setAmount(amount);
            Timestamp date = Timestamp.valueOf(body.get("date").substring(0, 10) + " 00:00:00");
            incomeAndExpenses.setDate(date);
            incomeAndExpenses.setCategory(category);

            incomeAndExpenses.setAbo(Boolean.parseBoolean(body.get("abo")));
            if (Boolean.parseBoolean(body.get("abo"))) {
                incomeAndExpenses.setAboInterval(Integer.parseInt(body.get("aboInterval")));
            } else {
                incomeAndExpenses.setAboInterval(0);
            }
            return incomeAndExpensesRepository.save(incomeAndExpenses);
        }
        response.sendError(401);
        return null;
    }
}