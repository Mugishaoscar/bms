package com.bar.bms.controller;

import com.bar.bms.model.DayReport;
import com.bar.bms.model.Debt;
import com.bar.bms.model.User;
import com.bar.bms.repository.DayReportRepository;
import com.bar.bms.repository.DebtRepository;
import com.bar.bms.repository.SaleRepository;
import com.bar.bms.repository.UserRepository;
import com.bar.bms.service.ProductService;
import com.bar.bms.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String workerDashboard(Model model) {
        User worker = getLoggedInUser();

        if (worker != null) {
            model.addAttribute("products", productService.getProductsByBoss(worker.getBossId()));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "worker_dashboard";
    }

    @GetMapping("/product")
    public String workerProducts(Model model) {
        User worker = getLoggedInUser();

        if (worker != null) {
            model.addAttribute("products", productService.getProductsByBoss(worker.getBossId()));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "worker_product";
    }
    @Autowired
    private SaleRepository saleRepository;
    @GetMapping("/sales")
    public String workerSales(Model model) {
        model.addAttribute("sales", saleRepository.findAllByOrderByDateTimeDesc());
        return "worker_sales";
    }

    @GetMapping("/report")
    public String workerReport(Model model) {
        Double totalSales = saleRepository.getTotalSalesAmount();

        if (totalSales == null) {
            totalSales = 0.0;
        }

        model.addAttribute("totalSales", totalSales);
        model.addAttribute("sales", saleRepository.findAllByOrderByDateTimeDesc());

        return "worker_report";
    }


    @PostMapping("/sell")
    public String sellProduct(@RequestParam Long productId) {
        productService.sellOneProduct(productId);
        return "redirect:/worker/dashboard";
    }
    @Autowired
    private SaleService saleService;

    @PostMapping("/sale/create")
    public String createSale(@RequestParam(required = false) String customerName,
                             @RequestParam(required = false) String customerPhone,
                             @RequestParam(required = false) String customerEmail,
                             @RequestParam Long[] productIds,
                             @RequestParam Integer[] quantities) {

        User worker = getLoggedInUser();

        if (worker != null) {
            saleService.createSale(
                    customerName,
                    customerPhone,
                    customerEmail,
                    productIds,
                    quantities,
                    worker.getId(),
                    worker.getBossId()
            );
        }

        return "redirect:/worker/dashboard";
    }
    @Autowired
    private DayReportRepository dayReportRepository;
    @PostMapping("/end-day")
    public String endDay() {

        Double totalSales = saleRepository.getTotalSalesAmount();

        if (totalSales == null) {
            totalSales = 0.0;
        }

        DayReport report = new DayReport();
        User worker = getLoggedInUser();

        if (worker != null) {
            report.setWorkerId(worker.getId());
            report.setBossId(worker.getBossId());
        }
        report.setReportDate(LocalDate.now());
        report.setTotalSales(totalSales);
        report.setSubmittedAt(LocalDateTime.now());
        report.setStatus("SUBMITTED");

        dayReportRepository.save(report);

        return "redirect:/worker/report";
    }
    @Autowired
    private DebtRepository debtRepository;
    @GetMapping("/debt")
    public String workerDebt(Model model) {
        model.addAttribute("debts", debtRepository.findAllByOrderByDateTimeDesc());
        return "worker_debt";
    }
    @PostMapping("/debt/create")
    public String createDebt(@RequestParam String customerName,
                             @RequestParam(required = false) String customerPhone,
                             @RequestParam(required = false) String customerEmail,
                             @RequestParam String items,
                             @RequestParam Double totalAmount) {

        Debt debt = new Debt();
        debt.setCustomerName(customerName);
        debt.setCustomerPhone(customerPhone);
        debt.setCustomerEmail(customerEmail);
        debt.setItems(items);
        debt.setTotalAmount(totalAmount);
        debt.setDateTime(LocalDateTime.now());
        User worker = getLoggedInUser();

        if (worker != null) {
            debt.setWorkerId(worker.getId());
            debt.setBossId(worker.getBossId());
        }
        debt.setStatus("UNPAID");

        debtRepository.save(debt);

        return "redirect:/worker/debt";
    }
    @PostMapping("/debt/update")
    public String updateDebtStatus(@RequestParam Long id,
                                   @RequestParam String status) {

        Debt debt = debtRepository.findById(id).orElse(null);

        if (debt != null) {
            debt.setStatus(status);
            debtRepository.save(debt);
        }

        return "redirect:/worker/debt";
    }@Autowired
    private UserRepository userRepository;

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByUsername(username).orElse(null);
    }

}