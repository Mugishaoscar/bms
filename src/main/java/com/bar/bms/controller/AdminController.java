package com.bar.bms.controller;

import com.bar.bms.model.Employee;
import com.bar.bms.model.Product;
import com.bar.bms.model.Debt;
import com.bar.bms.model.User;
import com.bar.bms.repository.DayReportRepository;
import com.bar.bms.repository.EmployeeRepository;
import com.bar.bms.service.ProductService;
import com.bar.bms.service.DebtService;
//import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bar.bms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private DebtService debtService;

    @Autowired
    private EmployeeRepository employeeRepo;

    // Handles the URL: http://localhost:8080/admin/dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        User boss = getLoggedInUser();

        if (boss != null) {
            model.addAttribute("products", productService.getProductsByBoss(boss.getId()));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "admin_dashboard";
    }

    // Handles saving products (Create and Update)
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product) {


        User boss = getLoggedInUser();

        if (boss != null) {
            product.setBossId(boss.getId());
        }
        productService.saveProduct(product);

        return "redirect:/admin/dashboard";
    }

    // Handles the URL: http://localhost:8080/admin/debt
    @GetMapping("/debt")
    public String showDebtPage(Model model) {
        model.addAttribute("debts", debtService.getAllDebts());
        return "debt_page";
    }
    @GetMapping("/employee")
    public String showEmployeePage(Model model) {
        // 1. Get the list from the database
        List<Employee> employeeList = employeeRepo.findAll();

        // 2. YOU MUST NAME IT "employees" (plural) to match th:each="emp : ${employees}"
        model.addAttribute("employees", employeeList);

        return "employee_page";
    }
    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        // Manually setting the Boss ID to 1 for now (Oscar Solutions Logic)
        employee.setBossId(1L);
        employeeRepo.save(employee);
        return "redirect:/admin/employee";
    }
    // Inside AdminController.java

    @Autowired
    private UserRepository userRepository; // This connects the controller to the users table
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/register-boss")
    public String registerBoss(@ModelAttribute User boss) {
        boss.setRole("BOSS");
        boss.setPassword(passwordEncoder.encode(boss.getPassword()));
        userRepository.save(boss);
        return "redirect:/login";
    }

    @GetMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepo.deleteById(id);
        return "redirect:/admin/employee";
    }
//    @PostMapping("/register-boss")
//    public String registerBoss(@ModelAttribute User boss) {
//        boss.setRole("BOSS");
//        userRepository.save(boss);
//        return "redirect:/login";
//    }
     @Autowired
     private DayReportRepository dayReportRepository;
    @GetMapping("/report")
    public String showReportPage(Model model) {
        model.addAttribute("dayReports", dayReportRepository.findAllByOrderBySubmittedAtDesc());
        return "report_page";
    }


    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return null;
        }

        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}