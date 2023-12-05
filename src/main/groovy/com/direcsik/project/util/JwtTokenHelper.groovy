package com.direcsik.project.util

import com.direcsik.project.exception.JwtTokenException
import com.direcsik.project.security.UserPrinciple
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

import javax.crypto.SecretKey
import java.security.SignatureException

@Component
class JwtTokenHelper {

    @Value('${jwt.token.sign.key}')
    private String encodedSecretKey

    @Value('${jwt.token.lifetime}')
    private int jwtLifetime

    String generateJwtToken(Authentication authentication) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal()
        SecretKey secretKey = getSecretKey()
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtLifetime))
                .signWith(secretKey, Jwts.SIG.HS384)
                .compact()
    }

    void validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(authToken)
        } catch (SignatureException exception) {
            throw new JwtTokenException('Invalid JWT signature', exception)
        } catch (MalformedJwtException exception) {
            throw new JwtTokenException('Invalid JWT token', exception)
        } catch (ExpiredJwtException exception) {
            throw new JwtTokenException('Expired JWT token', exception)
        } catch (UnsupportedJwtException exception) {
            throw new JwtTokenException('Unsupported JWT token', exception)
        } catch (IllegalArgumentException exception) {
            throw new JwtTokenException('JWT claims string is empty', exception)
        }
    }

    String getUserNameFromJwtToken(String authToken) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(authToken)
                .getPayload()
                .getSubject()
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(encodedSecretKey.getBytes())
    }
}
