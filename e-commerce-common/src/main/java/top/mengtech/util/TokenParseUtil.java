package top.mengtech.util;

import cn.hutool.core.codec.Base64Decoder;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import top.mengtech.constant.CommonConstant;
import top.mengtech.vo.LoginUserInfo;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

/**
 * jwt token
 */
public class TokenParseUtil {
    public static LoginUserInfo parseUserInfoFromToken(String token) throws Exception{
        if(null == token){
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token,getPublicKey());
        Claims claims = claimsJws.getBody();

        // token过期
        if(claims.getExpiration().before(Calendar.getInstance().getTime())){
            return null;
        }
        return JSON.parseObject(claims.get(CommonConstant.JWT_USER_INFO_KEY).toString(), LoginUserInfo.class);
    }

    private static Jws<Claims> parseToken(String token,PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }
    private static PublicKey getPublicKey() throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new Base64Decoder().decode(CommonConstant.PUBLIC_KEY));
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
