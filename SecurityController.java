package poly.edu.controller.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.dao.AccountDAO;
import poly.edu.entity.Account;
import poly.edu.service.SessionService;

@Controller
public class SecurityController {
    @Autowired
    AccountDAO dao;
    
    @Autowired
    SessionService session;

    // 1. Hiển thị form đăng nhập
    @RequestMapping("/security/login/form")
    public String loginForm(Model model) {
        model.addAttribute("message", "Vui lòng đăng nhập!");
        return "security/login";
    }

    // 2. XỬ LÝ ĐĂNG NHẬP (Đây là đoạn code còn thiếu)
    @PostMapping("/security/login")
    public String login(Model model, 
            @RequestParam("username") String username, 
            @RequestParam("password") String password) {
        try {
            Account user = dao.findById(username).orElse(null);
            
            if(user == null) {
                model.addAttribute("message", "Tên đăng nhập không tồn tại!");
            } else if(!user.getPassword().equals(password)) {
                model.addAttribute("message", "Sai mật khẩu!");
            } else {
                session.set("user", user); // Lưu user vào session
                model.addAttribute("message", "Đăng nhập thành công!");
                
                // Nếu là Admin thì chuyển sang trang quản trị, ngược lại về trang chủ
                if(user.isAdmin()) { 
                    // return "redirect:/admin/index"; (Mở dòng này nếu muốn admin vào thẳng trang quản trị)
                    return "redirect:/"; 
                } else {
                    return "redirect:/";
                }
            }
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi đăng nhập!");
        }
        return "security/login";
    }

    // 3. Xử lý đăng xuất
    @RequestMapping("/security/logoff")
    public String logoff(Model model) {
        session.remove("user");
        model.addAttribute("message", "Đã đăng xuất!");
        return "redirect:/security/login/form";
    }
    
    @RequestMapping("/security/unauthoried")
    public String unauthoried(Model model) {
        model.addAttribute("message", "Bạn không có quyền truy cập!");
        return "security/login";
    }

    // --- XỬ LÝ ĐĂNG KÝ ---
    @GetMapping("/security/register")
    public String registerForm(Model model) {
        return "security/register";
    }
    
    @PostMapping("/security/register")
    public String register(Model model, 
            @RequestParam String username, 
            @RequestParam String password, 
            @RequestParam String fullname, 
            @RequestParam String email) {
        
        if(dao.existsById(username)) {
            model.addAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "security/register";
        }
        
        try {
            Account user = new Account();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setActivated(true);
            user.setAdmin(false); 
            user.setPhoto("user.png");
            
            dao.save(user);
            model.addAttribute("message", "Đăng ký thành công! Mời đăng nhập.");
            return "security/login";
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi đăng ký!");
            return "security/register";
        }
    }

    // --- XỬ LÝ QUÊN MẬT KHẨU ---
    @GetMapping("/security/forgot-password")
    public String forgotForm() {
        return "security/forgot-password";
    }
    
    @PostMapping("/security/forgot-password")
    public String forgot(Model model, 
            @RequestParam String username, 
            @RequestParam String email) {
        try {
            Account user = dao.findById(username).orElse(null);
            if(user == null) {
                model.addAttribute("message", "Không tìm thấy tài khoản!");
            } else if(!user.getEmail().equals(email)) {
                model.addAttribute("message", "Email không khớp với tài khoản!");
            } else {
                user.setPassword("123"); 
                dao.save(user);
                model.addAttribute("message", "Mật khẩu đã được reset về: 123");
                return "security/login";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi xử lý!");
        }
        return "security/forgot-password";
    }
}