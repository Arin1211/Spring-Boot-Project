package com.demo.bankapp.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import static com.demo.bankapp.configuration.security.SecurityConstants.SECRET;

public class JwtTestUtils {

    public static String generateTestToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("test")
                .withClaim("role", "ROLE_USER")
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }
}
