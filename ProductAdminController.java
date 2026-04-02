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
import poly.edu.dao.CategoryDAO;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Category;
import poly.edu.entity.Product;

@Controller
public class ProductAdminController {
    @Autowired
    ProductDAO dao;
    
    @Autowired
    CategoryDAO cdao;
    
    @Autowired
    ServletContext app;

    // --- BỔ SUNG: Tự động chuyển hướng nếu gõ thiếu /index ---
    @RequestMapping("/admin/products")
    public String redirect() {
        return "redirect:/admin/products/index";
    }

    // 1. Hiển thị danh sách
    @RequestMapping("/admin/products/index")
    public String index(Model model) {
        Product item = new Product();
        model.addAttribute("item", item);
        
        List<Product> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/products/index";
    }

    // 2. Edit
    @RequestMapping("/admin/products/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Product item = dao.findById(id).get();
        model.addAttribute("item", item);
        
        List<Product> items = dao.findAll();
        model.addAttribute("items", items);
        
        return "admin/products/index";
    }

    // 3. Create
    @RequestMapping("/admin/products/create")
    public String create(Model model, 
                         @ModelAttribute("item") Product item,
                         @RequestParam("imageFile") MultipartFile file) {
        
        if(!file.isEmpty()) {
            item.setImage(file.getOriginalFilename());
            try {
                String path = app.getRealPath("/images/" + item.getImage());
                file.transferTo(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            item.setImage("cloud-upload.jpg");
        }
        
        dao.save(item);
        model.addAttribute("message", "Thêm mới thành công!");
        
        return "redirect:/admin/products/index";
    }

    // 4. Update
    @RequestMapping("/admin/products/update")
    public String update(Model model, 
                         @ModelAttribute("item") Product item,
                         @RequestParam("imageFile") MultipartFile file) {
        
        if(!file.isEmpty()) {
            item.setImage(file.getOriginalFilename());
            try {
                String path = app.getRealPath("/images/" + item.getImage());
                file.transferTo(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        dao.save(item);
        model.addAttribute("message", "Cập nhật thành công!");
        
        return "redirect:/admin/products/edit/" + item.getId();
    }

    // 5. Delete
    @RequestMapping("/admin/products/delete")
    public String delete(Model model, @RequestParam("id") Integer id) {
        try {
            dao.deleteById(id);
            model.addAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Không thể xóa sản phẩm đang có trong đơn hàng!");
        }
        return "redirect:/admin/products/index";
    }
    
    @ModelAttribute("cates")
    public List<Category> getCategories(){
        return cdao.findAll();
    }
}