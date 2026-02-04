package com.msquare.user.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleTokenVerifier {

    private final String clientId;
    private volatile GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifier(@Value("${app.oauth.google-client-id}") String clientId) {
        this.clientId = clientId;
    }

    public GoogleIdToken verify(String idTokenString) {
        if (idTokenString == null || idTokenString.isBlank()) {
            return null;
        }
        try {
            GoogleIdTokenVerifier localVerifier = verifier;
            if (localVerifier == null) {
                localVerifier = new GoogleIdTokenVerifier.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance()
                ).setAudience(Collections.singletonList(clientId)).build();
                verifier = localVerifier;
            }
            return localVerifier.verify(idTokenString);
        } catch (Exception ex) {
            return null;
        }
    }
}
