package poly.edu.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import poly.edu.entity.Account;
import poly.edu.service.SessionService;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    SessionService session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        Account user = session.get("user"); // Lấy user từ session

        String error = "";
        
        if (user == null) { 
            // 1. Chưa đăng nhập
            error = "Vui lòng đăng nhập!";
        } else if (!user.isAdmin() && uri.startsWith("/admin/")) { 
            // 2. Đã đăng nhập nhưng không phải Admin mà đòi vào trang Admin
            error = "Truy cập bị từ chối!";
        }

        if (error.length() > 0) { // Có lỗi
            session.set("security-uri", uri); // Lưu lại trang đang muốn vào
            response.sendRedirect("/security/login/form?message=" + error);
            return false; // Ngăn không cho vào
        }
        
        return true; // Cho phép đi tiếp
    }
}