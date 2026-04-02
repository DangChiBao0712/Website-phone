package poly.edu.controller.site;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.dao.ProductDAO;
import poly.edu.entity.Product;

@Controller
public class ProductController {
    
    @Autowired
    ProductDAO pdao;

    @RequestMapping("/product/list")
    public String list(Model model, 
            @RequestParam("cid") Optional<String> cid,
            @RequestParam("keywords") Optional<String> kw) { // 1. Thêm tham số nhận từ khóa
        
        if(cid.isPresent()) {
            // Trường hợp 1: Có mã loại -> Lọc theo loại
            List<Product> list = pdao.findByCategoryId(cid.get());
            model.addAttribute("items", list);
        } 
        else if(kw.isPresent()) {
            // Trường hợp 2: Có từ khóa -> Tìm kiếm (MỚI THÊM)
            String keywords = "%" + kw.get() + "%"; // Thêm dấu % để tìm gần đúng trong SQL
            List<Product> list = pdao.findByKeywords(keywords);
            model.addAttribute("items", list);
            model.addAttribute("keywords", kw.get()); // Gửi lại từ khóa ra View để giữ trong ô input
        } 
        else {
            // Trường hợp 3: Không có gì -> Lấy tất cả
            List<Product> list = pdao.findAll();
            model.addAttribute("items", list);
        }
        
        return "product/list";
    }

    @RequestMapping("/product/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Product item = pdao.findById(id).get();
        model.addAttribute("item", item);
        // Tăng số lượt xem lên 1 mỗi khi click vào xem chi tiết (Optional)
        // item.setViewCount(item.getViewCount() + 1);
        // pdao.save(item);
        return "product/detail";
    }
}