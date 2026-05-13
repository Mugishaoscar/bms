package com.bar.bms.controller;

import com.bar.bms.model.DayReport;
import com.bar.bms.model.Debt;
import com.bar.bms.model.Notification;
import com.bar.bms.model.User;
import com.bar.bms.repository.*;
import com.bar.bms.service.DebtService;
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
import org.springframework.web.bind.annotation.ResponseBody;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private ProductService productService;
    @Autowired
    private NotificationRepository notificationRepository;



    @GetMapping("/dashboard")
    public String workerDashboard(Model model) {
        User worker = getLoggedInUser();

        model.addAttribute("products",
                productService.getProductsByBoss(worker.getBossId()));

        model.addAttribute("notifications",
                notificationRepository.findByBossIdAndTargetRoleOrderByCreatedAtDesc(
                        worker.getBossId(), "WORKER"));

        model.addAttribute("unreadCount",
                notificationRepository.countByBossIdAndTargetRoleAndSeenFalse(
                        worker.getBossId(), "WORKER"));

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
        User worker = getLoggedInUser();

        model.addAttribute("sales",
                saleRepository.findByWorkerIdOrderByDateTimeDesc(worker.getId()));

        return "worker_sales";
    }

    @GetMapping("/report")
    public String workerReport(Model model) {
        User worker = getLoggedInUser();

        Double totalSales = saleRepository.getTotalSalesAmountByWorkerId(worker.getId());

        if (totalSales == null) {
            totalSales = 0.0;
        }

        model.addAttribute("totalSales", totalSales);
        model.addAttribute("sales",
                saleRepository.findByWorkerIdOrderByDateTimeDesc(worker.getId()));

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
        Notification notification = new Notification();
        notification.setBossId(worker.getBossId());
        notification.setTargetRole("BOSS");
        notification.setMessage("Worker " + worker.getUsername()
                + " ended the day. Total earned: "
                + totalSales + " RWF");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSeen(false);

        notificationRepository.save(notification);

        return "redirect:/worker/report";
    }
    @Autowired
    private DebtRepository debtRepository;
    @GetMapping("/debt")
    public String workerDebt(Model model) {
        User worker = getLoggedInUser();

        model.addAttribute("debts",
                debtRepository.findByWorkerIdOrderByDateTimeDesc(worker.getId()));

        return "worker_debt";
    }
    @Autowired
    private DebtService debtService;
    @PostMapping("/debt/create")
    public String createDebt(@RequestParam String customerName,
                             @RequestParam(required = false) String customerPhone,
                             @RequestParam(required = false) String customerEmail,
                             @RequestParam Long[] productIds,
                             @RequestParam Integer[] quantities) {

        User worker = getLoggedInUser();

        if (worker != null) {
            debtService.createDebt(
                    customerName,
                    customerPhone,
                    customerEmail,
                    productIds,
                    quantities,
                    worker.getId(),
                    worker.getBossId()
            );
        }

        return "redirect:/worker/debt";
    }

    @PostMapping("/debt/update")
    public String updateDebtStatus(@RequestParam Long id,
                                   @RequestParam String status) {
        User worker = getLoggedInUser();

        Debt debt = debtRepository.findById(id).orElse(null);

        if (debt != null) {
            debt.setStatus(status);
            debtRepository.save(debt);
        }
        if ("PAID".equals(status)) {
            Notification notification = new Notification();
            notification.setBossId(worker.getBossId());
            notification.setMessage("Debt paid by "
                    + debt.getCustomerName()
                    + ". Amount: "
                    + debt.getTotalAmount()
                    + " RWF. Updated by worker "
                    + worker.getUsername());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setSeen(false);

            notificationRepository.save(notification);
            if ("PAID".equals(status) && debt != null) {

                notification.setBossId(worker.getBossId());
                notification.setTargetRole("BOSS");
                notification.setMessage("Debt paid by "
                        + debt.getCustomerName()
                        + ". Amount: "
                        + debt.getTotalAmount()
                        + " RWF. Updated by worker "
                        + worker.getUsername());
                notification.setCreatedAt(LocalDateTime.now());
                notification.setSeen(false);

                notificationRepository.save(notification);
            }
        }

        return "redirect:/worker/debt";
    }@Autowired
    private UserRepository userRepository;

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByUsername(username).orElse(null);
    }
    @PostMapping("/notifications/read")
    @ResponseBody
    public String markNotificationsRead() {

        User worker = getLoggedInUser();

        List<Notification> notifications =
                notificationRepository
                        .findByBossIdAndTargetRoleOrderByCreatedAtDesc(
                                worker.getBossId(),
                                "WORKER");

        for (Notification n : notifications) {
            n.setSeen(true);
        }

        notificationRepository.saveAll(notifications);

        return "success";
    }


}