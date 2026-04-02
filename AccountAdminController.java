package poly.edu.controller.admin;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import poly.edu.dao.*;
import poly.edu.entity.*;
import poly.edu.service.*;
import poly.edu.model.*;
import poly.edu.config.*;
import poly.edu.service.impl.*;

@Controller
public class AccountAdminController {
    @Autowired
    AccountDAO dao;

    @Autowired
    ServletContext app; // Dùng để lấy đường dẫn lưu ảnh

    // --- BỔ SUNG: Tự động chuyển hướng nếu gõ thiếu /index ---
    @RequestMapping("/admin/accounts")
    public String redirect() {
        return "redirect:/admin/accounts/index";
    }

    // 1. Hiển thị trang quản lý (Reset form)
    @RequestMapping("/admin/accounts/index")
    public String index(Model model) {
        Account item = new Account();
        model.addAttribute("item", item);
        
        List<Account> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/accounts/index";
    }

    // 2. Click nút "Edit" trên bảng -> Đổ dữ liệu lên form
    @RequestMapping("/admin/accounts/edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        Account item = dao.findById(username).get();
        model.addAttribute("item", item);
        
        List<Account> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/accounts/index";
    }

    // 3. Nút "Thêm mới" (Có xử lý upload ảnh)
    @RequestMapping("/admin/accounts/create")
    public String create(Model model, 
                         @ModelAttribute("item") Account item, 
                         @RequestParam("photoFile") MultipartFile file) { // Hứng file từ form
        
        // Kiểm tra trùng lặp Username
        if(dao.existsById(item.getUsername())) {
            model.addAttribute("message", "Username đã tồn tại!");
        } else {
            // Xử lý lưu ảnh
            if(!file.isEmpty()) {
                item.setPhoto(file.getOriginalFilename());
                try {
                    String path = app.getRealPath("/images/" + item.getPhoto());
                    file.transferTo(new File(path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                item.setPhoto("user.png"); // Ảnh mặc định nếu không chọn
            }
            
            dao.save(item);
            model.addAttribute("message", "Thêm mới thành công!");
        }
        
        // Load lại bảng
        return "redirect:/admin/accounts/index";
    }

    // 4. Nút "Cập nhật"
    @RequestMapping("/admin/accounts/update")
    public String update(Model model, 
                         @ModelAttribute("item") Account item,
                         @RequestParam("photoFile") MultipartFile file) {
        
        if(!dao.existsById(item.getUsername())) {
             model.addAttribute("message", "Tài khoản không tồn tại!");
        } else {
            // Nếu có chọn ảnh mới thì lưu, không thì giữ nguyên ảnh cũ (đã có trong hidden input)
            if(!file.isEmpty()) {
                item.setPhoto(file.getOriginalFilename());
                try {
                    String path = app.getRealPath("/images/" + item.getPhoto());
                    file.transferTo(new File(path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            dao.save(item);
            model.addAttribute("message", "Cập nhật thành công!");
        }
        
        // Redirect về edit để người dùng thấy ngay thay đổi
        return "redirect:/admin/accounts/edit/" + item.getUsername();
    }

    // 5. Nút "Xóa"
    @RequestMapping("/admin/accounts/delete")
    public String delete(Model model, @RequestParam("username") String username) {
        try {
            dao.deleteById(username);
            model.addAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Không thể xóa tài khoản đang có đơn hàng!");
        }
        return "redirect:/admin/accounts/index";
    }
}