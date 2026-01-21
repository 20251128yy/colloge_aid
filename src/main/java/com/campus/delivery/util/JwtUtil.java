package com.campus.delivery.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // 非静态字段，通过@Value注入（留空，不指定默认值）
    @Value("${jwt.secret:}")
    private String secret;

    @Value("${jwt.expiration:86400000}")  // 默认24小时
    private Long expiration;

    // 静态字段
    private static SecretKey staticSecretKey;
    private static Long staticExpiration;

    @PostConstruct
    public void init() {
        try {
            // 关键修复：自动生成符合要求的256位密钥（优先用配置，无配置则自动生成）
            if (secret == null || secret.trim().isEmpty()) {
                // 自动生成256位（32字节）的安全密钥
                staticSecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
            } else {
                // 如果配置了密钥，先检查长度，不足则自动补全
                byte[] keyBytes = secret.getBytes();
                if (keyBytes.length < 32) {
                    // 补全到32字节（256位）
                    byte[] newKeyBytes = new byte[32];
                    System.arraycopy(keyBytes, 0, newKeyBytes, 0, Math.min(keyBytes.length, 32));
                    staticSecretKey = Keys.hmacShaKeyFor(newKeyBytes);
                } else {
                    staticSecretKey = Keys.hmacShaKeyFor(keyBytes);
                }
            }
            staticExpiration = this.expiration;
        } catch (Exception e) {
            e.printStackTrace();
            // 兜底：如果以上逻辑失败，直接生成安全密钥
            staticSecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
            staticExpiration = 86400000L;
        }
    }

    /**
     * 生成Token（兼容静态调用）
     */
    public static String generateToken(Long userId, Integer identityType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + staticExpiration);

        try {
            return Jwts.builder()
                    .setSubject(Long.toString(userId))
                    .claim("identityType", identityType)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(staticSecretKey) // 自动匹配HS256算法
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成JWT Token失败：" + e.getMessage());
        }
    }

    /**
     * 从Token中获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(staticSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析JWT Token失败：" + e.getMessage());
        }
    }

    /**
     * 从Token中获取用户身份类型
     */
    public static Integer getIdentityTypeFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("解析JWT身份类型失败：Token为空");
            }
            if (token.split("\\.").length != 3) {
                throw new RuntimeException("解析JWT身份类型失败：Token格式错误");
            }
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(staticSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("identityType", Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析JWT身份类型失败：" + e.getMessage());
        }
    }

    /**
     * 验证Token
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(staticSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}