package com.bar.bms.controller;

import com.bar.bms.model.*;
import com.bar.bms.repository.*;
import com.bar.bms.service.ProductService;
import com.bar.bms.service.DebtService;
//import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
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
    @Autowired
    private NotificationRepository notificationRepository;
    // Handles saving products (Create and Update)
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product) {

        User boss = getLoggedInUser();

        if (boss != null) {
            product.setBossId(boss.getId());
        }

        productService.saveProduct(product);

        if (boss != null) {
            Notification notification = new Notification();
            notification.setBossId(boss.getId());
            notification.setMessage("Boss updated stock/product: " + product.getName()
                    + " | Quantity: " + product.getStockQuantity()
                    + " | Price: " + product.getPrice() + " RWF");
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }

        return "redirect:/admin/dashboard";
    }

    // Handles the URL: http://localhost:8080/admin/debt
    @Autowired
    private DebtRepository debtRepository;
    @GetMapping("/debt")
    public String showDebtPage(Model model) {
        User boss = getLoggedInUser();

        model.addAttribute("debts",
                debtRepository.findByBossIdOrderByDateTimeDesc(boss.getId()));

        return "debt_page";
    }
    @GetMapping("/employee")
    public String showEmployeePage(Model model) {
        User boss = getLoggedInUser();

        model.addAttribute("employees",
                employeeRepo.findByBossId(boss.getId()));

        return "employee_page";
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute Employee employee) {

        User boss = getLoggedInUser();

        if (boss == null) {
            return "redirect:/login";
        }

        // Check if username already exists in users table
        if (userRepository.findByUsername(employee.getUsername()).isPresent()) {
            return "redirect:/admin/employee?error=username_exists";
        }

        // Save employee record
        employee.setBossId(boss.getId());
        employeeRepo.save(employee);

        // Create worker login account
        User workerUser = new User();
        workerUser.setUsername(employee.getUsername());
        workerUser.setPassword(passwordEncoder.encode(employee.getPassword()));
        workerUser.setRole("WORKER");
        workerUser.setBossId(boss.getId());

        userRepository.save(workerUser);

        return "redirect:/admin/employee?success=created";
    }


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
        User boss = getLoggedInUser();

        model.addAttribute("dayReports",
                dayReportRepository.findByBossIdOrderBySubmittedAtDesc(boss.getId()));

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
    @PostMapping("/debt/update-status")
    public String updateDebtStatus(@RequestParam Long id,
                                   @RequestParam String status) {

        User boss = getLoggedInUser();
        Debt debt = debtRepository.findById(id).orElse(null);

        if (debt != null && debt.getBossId().equals(boss.getId())) {
            debt.setStatus(status);
            debtRepository.save(debt);
        }

        return "redirect:/admin/debt";
    }

}