package poly.edu.controller.site;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import poly.edu.dao.AccountDAO;
import poly.edu.entity.Account;
import poly.edu.service.SessionService;

@Controller
public class AccountController {
    
    @Autowired AccountDAO dao;
    @Autowired SessionService session;
    @Autowired ServletContext app;

    // --- 1. CHỨC NĂNG ĐỔI MẬT KHẨU (Code cũ) ---
    @GetMapping("/account/change-password")
    public String changeForm() {
        return "account/change-password";
    }

    @PostMapping("/account/change-password")
    public String change(Model model, 
            @RequestParam("password") String password,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {
        
        Account user = session.get("user");
        if(user == null) return "redirect:/security/login/form";

        try {
            if(!user.getPassword().equals(password)) {
                model.addAttribute("message", "Mật khẩu hiện tại không đúng!");
            } else if(!newPassword.equals(confirmPassword)) {
                model.addAttribute("message", "Xác nhận mật khẩu mới không khớp!");
            } else {
                user.setPassword(newPassword);
                dao.save(user);
                session.set("user", user); 
                model.addAttribute("message", "Đổi mật khẩu thành công!");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi đổi mật khẩu!");
        }
        return "account/change-password";
    }
    
    // --- 2. CHỨC NĂNG CẬP NHẬT HỒ SƠ (Code MỚI) ---
    @RequestMapping("/account/edit-profile")
    public String editProfile(Model model) {
        Account user = session.get("user");
        if(user == null) return "redirect:/security/login/form";
        
        model.addAttribute("user", user);
        return "account/edit-profile"; 
    }

    @PostMapping("/account/edit-profile")
    public String updateProfile(Model model,
                                @RequestParam("fullname") String fullname,
                                @RequestParam("email") String email,
                                @RequestParam("photoFile") MultipartFile file) {

        Account user = session.get("user");
        if (user == null) return "redirect:/security/login/form";

        try {
            // cập nhật text
            user.setFullname(fullname);
            user.setEmail(email);

            if (!file.isEmpty()) {

                String uploadDir = app.getRealPath("/images/");
                File dir = new File(uploadDir);

                // nếu chưa có folder thì tạo
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // đổi tên tránh trùng
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                File saveFile = new File(dir, filename);
                file.transferTo(saveFile);

                user.setPhoto(filename);
            }

            dao.save(user);
            session.set("user", user);

            model.addAttribute("message", "Cập nhật hồ sơ thành công!");
            model.addAttribute("user", user);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Lỗi cập nhật hồ sơ!");
        }

        return "account/edit-profile";
    }
}