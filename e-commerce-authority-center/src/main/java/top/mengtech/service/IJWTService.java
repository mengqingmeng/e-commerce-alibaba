package top.mengtech.service;

import top.mengtech.vo.UsernameAndPassword;

public interface IJWTService {

    /**
     * 生成JWT token，适用默认的超时时间
     * @param username 用户名
     * @param password 密码
     * @return token
     * @throws Exception
     */
    String generateToken(String username,String password) throws Exception;

    /**
     * 生成JWT token
     * @param username 用户名
     * @param password 密码
     * @param expire   到期
     * @return  token
     * @throws Exception
     */
    String generateToken(String username,String password,int expire) throws Exception;

    /**
     * 注册并且返回token
     * @param usernameAndPassword 用户名、密码对象
     * @return token
     * @throws Exception
     */
    String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception;
}
