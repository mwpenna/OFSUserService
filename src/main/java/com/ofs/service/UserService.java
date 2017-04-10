package com.ofs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ofs.models.BasicAuthUser;
import com.ofs.models.JWTSubject;
import com.ofs.models.TokenResponse;
import com.ofs.models.User;
import com.ofs.repository.UserRepository;
import com.ofs.server.errors.ForbiddenException;
import com.ofs.server.utils.Dates;
import com.ofs.utils.GlobalConfigs;
import com.ofs.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URI;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    GlobalConfigs globalConfigs;

    @Autowired
    @Qualifier("ofsObjectMapper")
    protected com.fasterxml.jackson.databind.ObjectMapper ofsObjectMapper;

    public TokenResponse getToken(BasicAuthUser basicAuthUser) throws Exception {
        User user = userRepository.getUserByUserName(basicAuthUser.getUserName()).get();
        String token = generateJWTToken(user);

        user.setToken(token);
        user.setTokenExpDate(Dates.now().plusMinutes(20));

        userRepository.updateUser(user);
        return TokenResponse.generateBearerTokenResponse(token);
    }

    public JWTSubject authenticateToken(String token) throws IOException {
        JWTSubject jwtSubject = parseToken(token);
        log.info("Parsed token for User: {}", StringUtils.getIdFromURI(jwtSubject.getUserHref()));

        Optional<User> optionalUser = userRepository.getUserById(StringUtils.getIdFromURI(jwtSubject.getUserHref()));

        if(!optionalUser.isPresent()) {
            throw new ForbiddenException();
        }

        return jwtSubject;
    }

    private JWTSubject parseToken(String token) throws IOException {
        log.debug("Attempting to parse token");
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(globalConfigs.getSharedSecret()))
                .parseClaimsJws(token).getBody();

        JWTSubject jwtSubject = ofsObjectMapper.readValue(claims.getSubject(), JWTSubject.class);
        log.info("Parsed token for User: {}", StringUtils.getIdFromURI(jwtSubject.getUserHref()));
        return jwtSubject;
    }

    private String generateJWTToken(User user) throws JsonProcessingException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        ZonedDateTime zonedDateTime = Dates.now();
        Date now = Dates.toDate(zonedDateTime);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(globalConfigs.getSharedSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId("JWTSubject")
                .setIssuedAt(now)
                .setSubject(ofsObjectMapper.writeValueAsString(JWTSubject.fromUser(user)))
                .setIssuer("OFSUserService")
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }
}
