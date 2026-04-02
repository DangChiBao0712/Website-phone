package poly.edu.service;

import java.util.Collection;
import poly.edu.model.CartItem;

public interface ShoppingCartService {
    void add(Integer id);           // Thêm hàng
    void remove(Integer id);        // Xóa hàng
    CartItem update(Integer id, int qty); // Cập nhật số lượng
    void clear();                   // Xóa sạch
    Collection<CartItem> getItems(); // Lấy danh sách hàng
    int getCount();                 // Đếm tổng số lượng
    double getAmount();             // Tính tổng tiền
}