package org.example.weneedbe.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.ErrorResponse;
import org.example.weneedbe.global.error.exception.ServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandleFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServiceException e) {
            sendErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        Map<String, ErrorResponse> result = new HashMap<>();
        result.put("result", errorResponse);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
