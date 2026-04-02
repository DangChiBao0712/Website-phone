package poly.edu.service;

import java.io.File;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public interface ParamService {
    // Đọc chuỗi từ tham số request
    String getString(String name, String defaultValue);
    
    // Đọc số nguyên
    int getInt(String name, int defaultValue);
    
    // Đọc số thực
    double getDouble(String name, double defaultValue);
    
    // Đọc boolean
    boolean getBoolean(String name, boolean defaultValue);
    
    // Đọc ngày tháng
    Date getDate(String name, String pattern);
    
    // Lưu file upload vào thư mục
    File save(MultipartFile file, String path);
}