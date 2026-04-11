package com.bar.bms.controller;

import com.bar.bms.model.Employee;
import com.bar.bms.model.Product;
import com.bar.bms.model.Debt;
import com.bar.bms.repository.EmployeeRepository;
import com.bar.bms.service.ProductService;
import com.bar.bms.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("products", productService.getAllProducts());
        return "admin_dashboard";
    }

    // Handles saving products (Create and Update)
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product) {
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

    @GetMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepo.deleteById(id);
        return "redirect:/admin/employee";
    }
}