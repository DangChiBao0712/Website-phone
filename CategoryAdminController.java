package poly.edu.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.dao.CategoryDAO;
import poly.edu.entity.Category;

@Controller
public class CategoryAdminController {
    @Autowired
    CategoryDAO dao;

    // 0. Redirect tự động để tránh lỗi 404 khi gõ thiếu /index
    @RequestMapping("/admin/categories")
    public String redirect() {
        return "redirect:/admin/categories/index";
    }

    // 1. Hiển thị danh sách
    @RequestMapping("/admin/categories/index")
    public String index(Model model) {
        Category item = new Category();
        model.addAttribute("item", item);
        
        List<Category> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/categories/index";
    }

    // 2. Click nút Edit trên bảng
    @RequestMapping("/admin/categories/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        Category item = dao.findById(id).get();
        model.addAttribute("item", item);
        
        List<Category> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/categories/index";
    }

    // 3. Thêm mới (Có kiểm tra trùng mã)
    @RequestMapping("/admin/categories/create")
    public String create(Model model, @ModelAttribute("item") Category item) {
        if(dao.existsById(item.getId())) {
            model.addAttribute("message", "Mã danh mục đã tồn tại!");
        } else {
            dao.save(item);
            model.addAttribute("message", "Thêm mới thành công!");
        }
        return "redirect:/admin/categories/index";
    }

    // 4. Cập nhật
    @RequestMapping("/admin/categories/update")
    public String update(Model model, @ModelAttribute("item") Category item) {
        dao.save(item);
        model.addAttribute("message", "Cập nhật thành công!");
        return "redirect:/admin/categories/edit/" + item.getId();
    }

    // 5. Xóa (Có bắt lỗi ràng buộc khóa ngoại)
    @RequestMapping("/admin/categories/delete")
    public String delete(Model model, @RequestParam("id") String id) {
        try {
            dao.deleteById(id);
            model.addAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Không thể xóa danh mục đang chứa sản phẩm!");
        }
        return "redirect:/admin/categories/index";
    }
}