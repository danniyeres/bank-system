package org.example.transactionservice.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    @Autowired
    public FeignClientInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            requestTemplate.header("Authorization", token);
        }
    }
}
