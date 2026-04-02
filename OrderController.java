package poly.edu.controller.site;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import poly.edu.dao.AccountDAO;
import poly.edu.dao.OrderDAO;
import poly.edu.dao.OrderDetailDAO;
import poly.edu.entity.Account;
import poly.edu.entity.Order;
import poly.edu.entity.OrderDetail;
import poly.edu.entity.Product;
import poly.edu.model.CartItem;
import poly.edu.service.ShoppingCartService;

@Controller
public class OrderController {


    @Autowired ShoppingCartService cart;
    @Autowired OrderDAO orderdao;
    @Autowired OrderDetailDAO ddao;
    @Autowired AccountDAO adao;
    
    @Autowired HttpSession session; // Dùng để lấy user đang đăng nhập

    // 1. Trang thanh toán (Checkout)
    @RequestMapping("/order/checkout")
    public String checkout(Model model) {
        // Nếu giỏ hàng rỗng -> Đá về trang xem giỏ
        if(cart.getCount() == 0) {
            return "redirect:/cart/view";
        }
        
        // QUAN TRỌNG: Phải gửi cart sang view để hiển thị số tiền
        model.addAttribute("cart", cart); 
        
        return "order/checkout";
    }

    // 2. Thực hiện đặt hàng (Nút Purchase)
    @PostMapping("/order/purchase")
    public String purchase(Model model, 
                           @RequestParam("address") String address) {
        
        // Lấy user từ session (người đang đăng nhập)
        Account user = (Account) session.getAttribute("user");
        
        // Nếu chưa đăng nhập thì bắt đăng nhập
        if(user == null) {
            return "redirect:/security/login/form";
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setAddress(address);
        order.setAccount(user); // Gán người mua là user đang login
        
        orderdao.save(order); // Lưu Order trước để có ID
        
        // Lưu từng chi tiết đơn hàng (OrderDetail)
        for(CartItem item : cart.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQty());
            
            // Chỉ cần set ID sản phẩm là đủ để JPA hiểu
            Product p = new Product();
            p.setId(item.getId());
            detail.setProduct(p);
            
            ddao.save(detail);
        }
        
        // Xóa giỏ hàng sau khi mua xong
        cart.clear();
        
        // Chuyển hướng đến trang chi tiết đơn hàng vừa tạo
        return "redirect:/order/detail/" + order.getId();
    }

    // 3. Xem chi tiết đơn hàng (Bill)
    @RequestMapping("/order/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Order order = orderdao.findById(id).get();
        model.addAttribute("order", order);
        return "order/detail";
    }
    
    // 4. Danh sách đơn hàng đã mua của User
    @RequestMapping("/order/list")
    public String list(Model model) {
        Account user = (Account) session.getAttribute("user");
        
        // Nếu chưa đăng nhập -> login
        if(user == null) {
            return "redirect:/security/login/form";
        }
        
        // Gọi hàm tìm kiếm theo Username (Cần thêm vào OrderDAO)
        model.addAttribute("orders", orderdao.findByUsername(user.getUsername()));
        
        return "order/list";
    }
}