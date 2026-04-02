package poly.edu.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.dao.ProductDAO;

@Controller
public class ReportAdminController {
    @Autowired
    ProductDAO dao;

    // 1. Tự động chuyển hướng nếu gõ ngắn /admin/reports
    @RequestMapping("/admin/reports")
    public String redirect() {
        return "redirect:/admin/reports/inventory";
    }

    // 2. Trang báo cáo tồn kho
    @RequestMapping("/admin/reports/inventory")
    public String inventory(Model model) {
        // Gọi hàm custom từ DAO
        List<Object[]> items = dao.getInventory();
        model.addAttribute("items", items);
        
        return "admin/reports/inventory"; 
    }
}