package poly.edu.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
    // 1. TRANG CHỦ (Sửa lại chỗ này)
    @RequestMapping({"/", "/home/index"})
    public String home() {
        // Cũ: return "redirect:/product/list";  <-- Xóa dòng này
        
        // Mới: Trả về file giao diện home/index.html (Nơi có Banner Quonix Dev)
        return "home/index"; 
    }
    
    // 2. TRANG QUẢN TRỊ (Giữ nguyên)
    @RequestMapping("/admin/index")
    public String admin() {
        return "admin/index"; 
    }
}