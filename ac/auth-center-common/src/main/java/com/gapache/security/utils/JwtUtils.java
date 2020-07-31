package com.gapache.security.utils;

import com.gapache.commons.security.RSAUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @since 2020/7/31 3:27 下午
 */
public class JwtUtils {

    private static final String CONTENT = "content";

    public static String generateToken(String content, PrivateKey privateKey, Long timeout) {
        return Jwts.builder()
                .claim(CONTENT, content)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public static String parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token)
                .getBody().get(CONTENT, String.class);
    }
}
