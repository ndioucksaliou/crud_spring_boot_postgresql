package org.sid.springboot.configuration;

// Cette classe va nous permettre de construire notre tokens.

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Cette classe va nous permettre de construire notre token.
@Component // Pour qu'il puisse être crée(inject avec spring
@Slf4j
public class JwtUtils {
    //Pour récuperer des valeurs que j'ai stocké dans application properties.
    //@Value("${app.secret-key}")
    private String secretKey="";

    //@Value("${app.expiration-time}")
    //private long expirationTime;
    public JwtUtils(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sKey = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sKey.getEncoded());
//            log.info("The secretKey is: "+secretKey);
        }catch (NoSuchAlgorithmException e){
            log.info("L'erreur est: "+e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    //Le Token a trois partis: l'entête, le payload et la signature

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder() //Le builder() va me permettre de construire mon Jwt
                .claims()
                .add(claims) //Pour mettre les caims qu'on a définit
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .and()
                .signWith(getSignKey()) // pour la signature
                .compact();
    }

    // Pour obtenir une clé secrete signée par rapport à la clé que j'ai déja obtenu(secretKey).
    private SecretKey getSignKey(){
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    private boolean isTokenExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }

    //Pour extraire le username de mon token.
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //Pour créer une méthode généraliste avec le type générique <T> T
    //Function<Claims,T> claimsResolver est une interface  ce fonctionnelle qui a une seule méthode: apply()
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); //c'est une interface fonctionnelle. Il prend un objet et retourne un objet de type T
    }

    //Pour récupérer tous les claims qu'on a définis dans createToken()
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())//Il faut la même clé pour décripter et vérifier qu'on a les mêmes infos
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}
