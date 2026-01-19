//package com.campus.delivery.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    @Value("${jwt.secret}")
//    private static String secret;
//
//    @Value("${jwt.expiration}")
//    private static Long expiration;
//
//    /**
//     * 生成Token
//     */
//    public static String generateToken(Long userId, Integer identityType) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expiration);
//
//        return Jwts.builder()
//                .setSubject(Long.toString(userId))
//                .claim("identityType", identityType)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    /**
//     * 从Token中获取用户ID
//     */
//    public static Long getUserIdFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Long.parseLong(claims.getSubject());
//    }
//
//    /**
//     * 从Token中获取用户身份类型
//     */
//    public static Integer getIdentityTypeFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return (Integer) claims.get("identityType");
//    }
//
//    /**
//     * 验证Token
//     */
//    public static boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
package com.campus.delivery.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Date;

@Component
public class JwtUtil {

    // 非静态字段，通过@Value注入
    @Value("${jwt.secret:campus-delivery-default-secret-key}")
    private String secret;

    @Value("${jwt.expiration:86400000}")  // 默认24小时
    private Long expiration;

    // 静态字段，用于静态方法访问
    private static String staticSecret;
    private static Long staticExpiration;

    @PostConstruct
    public void init() {
        // 将注入的值赋值给静态字段
        staticSecret = this.secret;
        staticExpiration = this.expiration;
    }

    /**
     * 生成Token
     */
    public static String generateToken(Long userId, Integer identityType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + staticExpiration);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .claim("identityType", identityType)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, staticSecret)
                .compact();
    }

    /**
     * 从Token中获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(staticSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 从Token中获取用户身份类型
     */
    public static Integer getIdentityTypeFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(staticSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("identityType", Integer.class);
    }

    /**
     * 验证Token
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(staticSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}