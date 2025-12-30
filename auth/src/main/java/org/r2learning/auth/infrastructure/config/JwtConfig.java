package org.r2learning.auth.infrastructure.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Configuration
public class JwtConfig {

    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtProperties jwtProperties) throws Exception {
        RSAPublicKey publicKey = null;
        RSAPrivateKey privateKey = null;

        if (jwtProperties.getPublicKeyLocation() != null && !jwtProperties.getPublicKeyLocation().isEmpty()) {
            publicKey = loadPublicKeyFromFile(jwtProperties.getPublicKeyLocation());
        }

        if (jwtProperties.getPrivateKeyLocation() != null && !jwtProperties.getPrivateKeyLocation().isEmpty()) {
            privateKey = loadPrivateKeyFromFile(jwtProperties.getPrivateKeyLocation());
        }

        if (publicKey != null && privateKey != null) {
            JWK jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();
            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
            return new NimbusJwtEncoder(jwkSource);
        }

        throw new IllegalArgumentException("JWT configuration is missing. Please configure jwt.public-key-location and jwt.private-key-location");
    }

    private RSAPublicKey loadPublicKeyFromFile(String location) throws Exception {
        File file = ResourceUtils.getFile(location);
        String key = new String(Files.readAllBytes(file.toPath()));
        return loadPublicKey(key);
    }

    private RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        String publicKeyContent = publicKeyStr
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    private RSAPrivateKey loadPrivateKeyFromFile(String location) throws Exception {
        File file = ResourceUtils.getFile(location);
        String key = new String(Files.readAllBytes(file.toPath()));
        return loadPrivateKey(key);
    }

    private RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        String privateKeyContent = privateKeyStr
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    @Data
    public static class JwtProperties {
        private String publicKeyLocation;
        private String privateKeyLocation;
        private String issuer;
        private String audience;
    }
}