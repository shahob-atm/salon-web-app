package com.sh32bit.service.impl;

import com.sh32bit.enums.Role;
import com.sh32bit.keycloak.*;
import com.sh32bit.request.RegisterRequest;
import com.sh32bit.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserService {
    private static final String KEYCLOAK_BASE_URL="http://localhost:8080";

    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL+"/admin/realms/master/users";

    private static final String TOKEN_URL = KEYCLOAK_BASE_URL+"/realms/master/protocol/openid-connect/token";
    private static final String CLIENT_ID = "salon-booking-client";
    private static final String CLIENT_SECRET = "UImc4oSsvKmhKFSy5KloU1aDULfMxkYi";
    private static final String GRANT_TYPE = "password";
    private static final String scope = "openid email profile";
    private static final String username = "admin";
    private static final String password = "admin";
    private static  final String clientId = "0be6a396-3976-42cd-bd2a-9691107260be";

    private final RestTemplate restTemplate;

    public void createUser(RegisterRequest req) throws Exception {
        String accessToken = getAdminAccessToken(username, password, GRANT_TYPE, null).getAccessToken();
        log.info("Access Token: {}", accessToken);

        Credential credential = new Credential(
                "password",
                req.password(),
                false
        );

        UserRequest userRequest = new UserRequest(
                req.username(),
                true,
                req.firstName(),
                req.lastName(),
                req.email(),
                List.of(credential),
                List.of()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        try {
            restTemplate.exchange(KEYCLOAK_ADMIN_API, HttpMethod.POST, requestEntity, Void.class);
            log.info("User created: {}", req.username());

            KeycloakUser user = fetchFirstUserByUsername(req.username(), accessToken);

            KeycloakRole role = getRoleByName(clientId, accessToken, Role.ROLE_CUSTOMER.name());

            assignRoleToUser(user.id(), clientId, List.of(role), accessToken);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Keycloak error: {}", e.getResponseBodyAsString());
            throw new Exception("Failed to create user: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new Exception("Unexpected error during user creation", e);
        }
    }

    public TokenResponse getAdminAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("grant_type", grantType);

        if ("password".equals(grantType)) {
            requestBody.add("username", username);
            requestBody.add("password", password);
            requestBody.add("scope", scope);
        } else if ("refresh_token".equals(grantType)) {
            requestBody.add("refresh_token", refreshToken);
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    requestEntity,
                    TokenResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new Exception("Failed to retrieve token: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new Exception("Unexpected error occurred while getting token");
        }
    }

    public KeycloakRole getRoleByName(String clientId, String token, String role) throws Exception {
        String url = String.format("%s/admin/realms/master/clients/{clientId}/roles/{role}", KEYCLOAK_BASE_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KeycloakRole> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KeycloakRole.class,
                    clientId,
                    role
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception("Failed to get role: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new Exception("Unexpected error while getting role", e);
        }
    }

    public KeycloakUser fetchFirstUserByUsername(String username, String token) throws Exception {
        String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KeycloakUser[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KeycloakUser[].class
            );

            KeycloakUser[] users = response.getBody();
            if (users != null && users.length > 0) {
                return users[0];
            }

            throw new Exception("User not found: " + username);

        } catch (HttpClientErrorException e) {
            throw new Exception("Keycloak API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new Exception("Unexpected error while fetching user: " + e.getMessage(), e);
        }
    }

    public void assignRoleToUser(String userId,
                                 String clientId,
                                 List<KeycloakRole> roles,
                                 String token) throws Exception {
        String url = String.format("%s/admin/realms/master/users/%s/role-mappings/clients/%s",
                KEYCLOAK_BASE_URL, userId, clientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<List<KeycloakRole>> entity = new HttpEntity<>(roles, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Roles assigned successfully to userId {}", userId);
            } else {
                throw new Exception("Failed with HTTP status: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception("Keycloak API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new Exception("Unexpected error assigning roles", e);
        }
    }

    public KeycloakUserInfo fetchUserProfileByJwt(String token) throws Exception {
        String url = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token);
        log.info("token -> {}", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KeycloakUserInfo> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KeycloakUserInfo.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception("Keycloak API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new Exception("Unexpected error while fetching user info", e);
        }
    }
}
