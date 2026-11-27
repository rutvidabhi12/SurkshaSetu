
package com.surakshasetu.complaints.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class FacultyRequestVerifier {

    @Value("${surakshasetu.internal.secret}")
    private String secret;

    public boolean verify(
            String facultyId,
            String timestamp,
            String method,
            String requestUri,
            String signature) {

        try {
            if (facultyId == null || timestamp == null
                    || method == null || requestUri == null
                    || signature == null) {
                return false;
            }

            long requestTime = Long.parseLong(timestamp);
            long currentTime = System.currentTimeMillis();

            // Request valid only for 5 minutes
            if (Math.abs(currentTime - requestTime) > 300000) {
                return false;
            }

            String message = facultyId + "\n"
                    + timestamp + "\n"
                    + method + "\n"
                    + requestUri;

            String expectedSignature = generateSignature(message);

            return MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8)
            );

        } catch (Exception e) {
            return false;
        }
    }

    private String generateSignature(String message) throws Exception {

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
    }
}
