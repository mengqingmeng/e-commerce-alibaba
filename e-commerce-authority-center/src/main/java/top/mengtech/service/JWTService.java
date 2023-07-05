package top.mengtech.service;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import top.mengtech.constant.AuthorityConstant;
import top.mengtech.constant.CommonConstant;
import top.mengtech.dao.EcommerceUserDao;
import top.mengtech.entity.EcommerceUser;
import top.mengtech.vo.LoginUserInfo;
import top.mengtech.vo.UsernameAndPassword;

import javax.transaction.Transactional;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class JWTService implements IJWTService{
    private final EcommerceUserDao ecommerceUserDao;

    public JWTService(EcommerceUserDao ecommerceUserDao) {
        this.ecommerceUserDao = ecommerceUserDao;
    }

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username,password,0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {
        // 验证用户能否通过授权校验：用户名、密码是否正确。
        EcommerceUser ecommerceUser = ecommerceUserDao.findByUsernameAndPassword(username,password);
        if(null == ecommerceUser){
            log.error("can not find user:[{}],[{}]",username,password);
            return null;
        }

        LoginUserInfo loginUserInfo = new LoginUserInfo(ecommerceUser.getId(),username);
        if(expire <=0){
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        // 计算超时时间
        ZonedDateTime zdt = LocalDate.now().plus(expire, ChronoUnit.DAYS)
                .atStartOfDay(ZoneId.systemDefault());

        Date expireDate = Date.from(zdt.toInstant());

        return Jwts.builder()
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo)) // payload,k-v
                .setId(UUID.randomUUID().toString()) // jwt id
                .setExpiration(expireDate) // 过期时间
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256) // 签名
                .compact(); // 生成jwt
    }

    @Override
    public String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception {
        // 校验用户是否存在。如果存在，不能注册。
        EcommerceUser oldUser = ecommerceUserDao.findByUsername(usernameAndPassword.getUsername());
        if(null != oldUser){
            log.error("username is registered:[{}]",oldUser.getUsername());
            return null;
        }

        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername(usernameAndPassword.getUsername());
        ecommerceUser.setPassword(usernameAndPassword.getPassword()); // MD5编码后的
        ecommerceUser.setExtraInfo("{}");

        // 注册新用户
        ecommerceUser = ecommerceUserDao.save(ecommerceUser);
        log.info("注册用户成功：[{}]",ecommerceUser.getId());

        return generateToken(ecommerceUser.getUsername(),ecommerceUser.getPassword());
    }

    /**
     * 根据本地存储的私钥生成PrivateKey
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey()throws Exception{
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY)
        );

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
