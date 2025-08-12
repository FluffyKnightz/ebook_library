package com.fluffyknightz.ebook_library.config.security.jwt;

import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey = "";

    public JwtService() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGenerator.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(MyUserDetails myUserDetails) {
        return generateToken(new HashMap<>(), myUserDetails);
    }

    public String generateRefreshToken(MyUserDetails myUserDetails) {
        return generateRefreshToken(new HashMap<>(), myUserDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, MyUserDetails myUserDetails) {

        return Jwts.builder().claims().add(extraClaims).subject(myUserDetails.getUsername()).issuedAt(
                           new Date(System.currentTimeMillis())).expiration(
                           new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000))).and().signWith(getSignInKey())
                   .compact();
    }

    public boolean isTokenValid(String token, MyUserDetails myUserDetails) {
        final String username = extractUsername(token);
        return (username.equals(myUserDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

    }

    private String generateRefreshToken(Map<String, Object> extraClaims, MyUserDetails myUserDetails) {

        return Jwts.builder().claims().add(extraClaims).subject(myUserDetails.getUsername()).issuedAt(
                           new Date(System.currentTimeMillis())).expiration(
                           new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000))).and().signWith(getSignInKey())
                   .compact();
    }


    private SecretKey getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
