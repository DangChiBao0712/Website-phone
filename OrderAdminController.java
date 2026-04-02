package poly.edu.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.dao.OrderDAO;
import poly.edu.dao.OrderDetailDAO;
import poly.edu.entity.Order;

@Controller
public class OrderAdminController {
    @Autowired
    OrderDAO dao;
    
    @Autowired
    OrderDetailDAO ddao;
    
    // Redirect nếu người dùng chỉ gõ /admin/orders
    @RequestMapping("/admin/orders")
    public String redirect() {
        return "redirect:/admin/orders/index";
    }

    // 1. Hiển thị danh sách đơn hàng
    @RequestMapping("/admin/orders/index")
    public String index(Model model) {
        Order item = new Order();
        model.addAttribute("item", item);
        
        // Lấy danh sách đơn hàng giảm dần theo ngày (mới nhất lên đầu)
        // Nếu anh chưa biết sort thì dùng findAll() thường cũng được
        List<Order> items = dao.findAll(); 
        model.addAttribute("items", items);
        
        return "admin/orders/index";
    }

    // 2. Xem chi tiết / Sửa đơn hàng
    @RequestMapping("/admin/orders/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        Order item = dao.findById(id).get();
        model.addAttribute("item", item);
        
        List<Order> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/orders/index";
    }

    // 3. Cập nhật thông tin đơn hàng
    @RequestMapping("/admin/orders/update")
    public String update(Model model, @ModelAttribute("item") Order item) {
        dao.save(item);
        model.addAttribute("message", "Cập nhật đơn hàng thành công!");
        return "redirect:/admin/orders/edit/" + item.getId();
    }

    // 4. Xóa đơn hàng
    @RequestMapping("/admin/orders/delete")
    public String delete(Model model, @RequestParam("id") Long id) {
        try {
            dao.deleteById(id);
            model.addAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Không thể xóa đơn hàng đã có chi tiết!");
        }
        return "redirect:/admin/orders/index";
    }
}