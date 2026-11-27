
package com.surakshasetu.admin.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class FacultyRequestSigner {

    @Value("${surakshasetu.internal.secret}")
    private String secret;

    public HttpHeaders createHeaders(
            Long facultyId,
            String method,
            String requestUri) {

        String timestamp = String.valueOf(
                System.currentTimeMillis()
        );

        String message = facultyId + "\n"
                + timestamp + "\n"
                + method + "\n"
                + requestUri;

        String signature = generateSignature(message);

        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Faculty-Id", String.valueOf(facultyId));
        headers.set("X-Timestamp", timestamp);
        headers.set("X-Signature", signature);

        return headers;
    }

    private String generateSignature(String message) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec key = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(key);

            byte[] hash = mac.doFinal(
                    message.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to generate faculty signature", e
            );
        }
    }
}
  