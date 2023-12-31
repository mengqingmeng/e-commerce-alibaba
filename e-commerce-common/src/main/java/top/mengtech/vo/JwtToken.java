package top.mengtech.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权中心给客户端的token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    private  String token;
}
