package com.bar.bms.controller;

import com.bar.bms.model.Sale;
import com.bar.bms.model.Debt; // 1. Added this import
import com.bar.bms.service.SaleService;
import com.bar.bms.service.DebtService; // 2. Added this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private DebtService debtService; // 3. Added this to talk to the Debt table

    @GetMapping("/reports")
    public String showReport(Model model) {
        List<Sale> sales = saleService.getAllDailySales();
        double total = saleService.calculateTotalRevenue(sales);
        model.addAttribute("salesItems", sales);
        model.addAttribute("totalAmount", total);
        return "reports";
    }

    @GetMapping("/debts")
    public String showDebts(Model model) { // 4. Added 'Model model' here
        // 5. Fetch the real data from the database
        List<Debt> debts = debtService.getAllDebts();
        double totalDebt = debtService.calculateTotalDebt(debts);

        // 6. Send the data to your debts.html
        model.addAttribute("debtItems", debts);
        model.addAttribute("totalDebt", totalDebt);

        return "debts";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/products")
    public String showProducts() {
        return "products";
    }
}