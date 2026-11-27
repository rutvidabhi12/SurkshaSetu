
package com.surakshasetu.complaints.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FacultyAuthenticationFilter extends OncePerRequestFilter {

    private final FacultyRequestVerifier verifier;

    public FacultyAuthenticationFilter(FacultyRequestVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI()
                .matches(".*/api/complaints/faculty/[^/]+");
    }

    
@Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

    String facultyId = request.getHeader("X-Faculty-Id");
    String timestamp = request.getHeader("X-Timestamp");
    String signature = request.getHeader("X-Signature");

    boolean valid = verifier.verify(
            facultyId,
            timestamp,
            request.getMethod(),
            request.getRequestURI(),
            signature
    );

    if (!valid) {
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Invalid faculty authentication"
        );
        return;
    }

    try {
        Long headerFacultyId = Long.parseLong(facultyId);

        String[] pathParts = request.getRequestURI().split("/");
        Long pathFacultyId = Long.parseLong(
                pathParts[pathParts.length - 1]
        );

        // Header ID and URL ID must match
        if (!headerFacultyId.equals(pathFacultyId)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Faculty ID mismatch"
            );
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        headerFacultyId,
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_FACULTY")
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);

    } catch (NumberFormatException e) {
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Invalid faculty ID"
        );
    } finally {
        SecurityContextHolder.clearContext();
    }
}

}
