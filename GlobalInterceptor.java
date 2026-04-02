package poly.edu.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import poly.edu.dao.CategoryDAO;

@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Autowired
    CategoryDAO dao;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Lấy danh mục sản phẩm bỏ vào request để hiển thị lên Menu ở mọi trang
        request.setAttribute("cates", dao.findAll());
    }
}