package poly.edu.service;

import jakarta.servlet.http.Cookie;

public interface CookieService {
    // Lấy cookie theo tên
    Cookie get(String name);
    
    // Lấy giá trị cookie (trả về "" nếu không có)
    String getValue(String name);
    
    // Tạo và lưu cookie
    Cookie add(String name, String value, int hours);
    
    // Xóa cookie
    void remove(String name);
}