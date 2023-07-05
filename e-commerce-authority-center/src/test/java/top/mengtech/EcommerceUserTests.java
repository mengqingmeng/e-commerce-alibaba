package top.mengtech;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.mengtech.dao.EcommerceUserDao;
import top.mengtech.entity.EcommerceUser;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class EcommerceUserTests {
    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    @Test
    public void createUserRecord(){
        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("test");
        ecommerceUser.setPassword(MD5.create().digestHex("123456"));

        log.info("save user:[{}]",ecommerceUserDao.save(ecommerceUser));
    }

}
