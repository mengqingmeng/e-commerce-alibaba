package top.mengtech.controller;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mengtech.annotation.IgnoreResponseAdvice;
import top.mengtech.service.IJWTService;
import top.mengtech.vo.JwtToken;
import top.mengtech.vo.UsernameAndPassword;

@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController {
    private final IJWTService ijwtService;

    public AuthorityController(IJWTService ijwtService) {
        this.ijwtService = ijwtService;
    }

    /**
     * 登陆，返回信息不需要统一包装
     * @param usernameAndPassword
     * @return
     * @throws Exception
     */
    @IgnoreResponseAdvice
    @PostMapping("/token")
    public JwtToken token(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception{
        log.info("request to get token with param:[{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(ijwtService.generateToken(usernameAndPassword.getUsername(),usernameAndPassword.getPassword()));
    }

    /**
     * 注册
     * @param usernameAndPassword
     * @return
     * @throws Exception
     */
    @IgnoreResponseAdvice
    @PostMapping("/register")
    public JwtToken register(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception{
        return new JwtToken(ijwtService.registerUserAndGenerateToken(usernameAndPassword));
    }

}
