// package com.examly.springapp.userspecifications;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// import org.springframework.stereotype.Component;

// import java.util.Date;
// import java.util.function.Function;

// @Component
// public class JwtUtil {

//     private final String jwtSecret = "yourSecretKeyHere";  // Use a strong secret key in production
//     private final long jwtExpirationMs = 86400000; // 1 day validity

//     public String generateToken(String username, String role) {

//         return Jwts.builder()
//                 .setSubject(username)
//                 .claim("role", role)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                 .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                 .compact();
//     }

//     public String getUsernameFromToken(String token) {
//         return getClaimFromToken(token, Claims::getSubject);
//     }

//     public String getRoleFromToken(String token) {
//         Claims claims = getAllClaimsFromToken(token);
//         return claims.get("role", String.class);
//     }

//     public Date getExpirationDateFromToken(String token) {
//         return getClaimFromToken(token, Claims::getExpiration);
//     }

//     public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = getAllClaimsFromToken(token);
//         return claimsResolver.apply(claims);
//     }

//     private Claims getAllClaimsFromToken(String token) {
//         return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//     }

//     public boolean validateToken(String token, String username) {
//         final String tokenUsername = getUsernameFromToken(token);
//         return (tokenUsername.equals(username) && !isTokenExpired(token));
//     }

//     private boolean isTokenExpired(String token) {
//         final Date expiration = getExpirationDateFromToken(token);
//         return expiration.before(new Date());
//     }
// }
