package poly.edu.service;

public interface SessionService {
    // Lấy giá trị session theo tên (Generic)
    <T> T get(String name);
    
    // Lưu giá trị vào session
    void set(String name, Object value);
    
    // Xóa session
    void remove(String name);
}