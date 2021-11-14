package com.system.design.gateway.adapter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequestAdapter implements HttpRequest {

    private final HttpServletRequest request;

    public HttpRequestAdapter(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getMethodValue() {
        return request.getMethod();
    }

    @Override
    public URI getURI() {
        try {
            return new URI(request.getRequestURL().toString() + "?"
                    + request.getQueryString());
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Override
    public HttpHeaders getHeaders() {
        return new HttpHeaders();
    }
}
