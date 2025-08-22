package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.AuthenticationRequest;
import com.dungcode.demo.dto.request.IntrospectRequest;
import com.dungcode.demo.dto.request.LoginGoogleRequest;
import com.dungcode.demo.dto.response.AuthenticationResponse;
import com.dungcode.demo.enums.Role;
import com.dungcode.demo.posgresql.entity.User;
import com.dungcode.demo.exception.GlobalExceptionHandler;
import com.dungcode.demo.posgresql.repository.UserRepository;
import com.dungcode.demo.util.EnvHelper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    GoogleTokenVerifier googleTokenVerifier;

    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        Optional<User> userOptional = this.userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            throw new GlobalExceptionHandler.NotFoundException("user not found");
        }
        User user = userOptional.get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid information");
        }

        String token = generateTokenJWT(user);

        return new SuccessResponse<>(AuthenticationResponse.builder()
                .authenticated(true)
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(token)
                .build());
    }

    private String generateTokenJWT(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("api-spring-dev")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli()))
                .claim("id", user.getId())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(EnvHelper.getEnv("JWT_SECRET_KEY").getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.print("Can not create token");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }

    public ApiResponse<?> introspectToken(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier verifier = new MACVerifier(EnvHelper.getEnv("JWT_SECRET_KEY").getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        HashMap<String, Object> result = new HashMap<>();
        result.put("verified", verified);
        result.put("expiredTime", expiredTime);
        result.put("payload", signedJWT.getPayload().toJSONObject());

        return new SuccessResponse<>(result);

    }

    public ApiResponse<AuthenticationResponse> loginGoogle(LoginGoogleRequest request) {
        try {
            // Verify Google ID Token
            GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(request.getId_token());

            if (payload == null) {
                throw new GlobalExceptionHandler.BadRequestCustomException("Invalid Google ID Token");
            }

            if (!(boolean) payload.get("aud").equals(EnvHelper.getEnv("GOOGLE_CLIENT_ID"))) {
                throw new GlobalExceptionHandler.BadRequestCustomException("Invalid Google ID Token (aud)");
            }

            String email = payload.getEmail();
            Optional<User> userOptional = this.userRepository.findByUsername(email);

            User user = userOptional.orElseGet(() -> createNewUser(email, payload));

            String token = generateTokenJWT(user);

            return new SuccessResponse<>(AuthenticationResponse.builder()
                    .authenticated(true)
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .token(token)
                    .build());
        } catch (Exception e) {
            throw new GlobalExceptionHandler.BadRequestCustomException("Authentication failed: " + e.getMessage());
        }
    }

    private User createNewUser(String email, GoogleIdToken.Payload payload) {
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        User newUser = User.builder()
                .username(email)
                .firstName((String) payload.get("given_name"))
                .lastName((String) payload.get("family_name"))
                .roles(roles)
                .build();

        return userRepository.save(newUser);
    }

}
