package top.mengtech.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.mengtech.entity.EcommerceUser;

/**
 * EcommerceUserDao 接口定义
 */
public interface EcommerceUserDao extends JpaRepository<EcommerceUser,Long> {

    EcommerceUser findByUsername(String username);

    EcommerceUser findByUsernameAndPassword(String username,String password);
}
