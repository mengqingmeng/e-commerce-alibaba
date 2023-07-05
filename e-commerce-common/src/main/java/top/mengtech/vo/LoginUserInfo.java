package top.mengtech.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登陆用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserInfo {
    // 用户ID
    private Long id;

    // 用户名
    private String username;
}
