package poly.edu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Product;

public interface ProductDAO extends JpaRepository<Product, Integer>{
    
    // 1. Tìm các sản phẩm thuộc về một danh mục cụ thể (Dùng cho trang Web bán hàng)
    @Query("SELECT p FROM Product p WHERE p.category.id=?1")
    List<Product> findByCategoryId(String cid);

    // 2. Thống kê hàng tồn kho theo loại (Dùng cho trang Admin Thống kê)
    @Query("SELECT p.category, sum(p.price), count(p) " +
           "FROM Product p " +
           "GROUP BY p.category")
    List<Object[]> getInventory();
    
    // 3. Tìm kiếm sản phẩm theo từ khóa (MỚI THÊM)
    // Hàm này sẽ tìm những sản phẩm mà tên có chứa từ khóa (keywords)
    @Query("SELECT p FROM Product p WHERE p.name LIKE ?1")
    List<Product> findByKeywords(String keywords);
}