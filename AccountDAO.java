package poly.edu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.entity.Account;


public interface AccountDAO extends JpaRepository<Account, String> {
    // JpaRepository đã có sẵn hàm findById để tìm user
}