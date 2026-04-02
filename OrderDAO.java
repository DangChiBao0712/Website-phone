package poly.edu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.edu.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Long>{
    
    // Tìm đơn hàng theo Username + Sắp xếp ngày tạo giảm dần (Mới nhất lên đầu)
    @Query("SELECT o FROM Order o WHERE o.account.username=?1 ORDER BY o.createDate DESC")
    List<Order> findByUsername(String username);
    
}