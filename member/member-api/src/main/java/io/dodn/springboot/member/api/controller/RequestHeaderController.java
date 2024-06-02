package io.dodn.springboot.member.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class RequestHeaderController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * MultiValueMap
     *  Map과 유사한데, 하나의 키에 여러 값을 받을 수 있음
     *  HTTP Header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용
     * @param request
     * @param response
     * @param httpMethod
     * @param locale
     * @param headerMap
     * @param host
     * @param cookie
     * @return
     */
    @RequestMapping("/headers")
    public String headers(final HttpServletRequest request
            , final HttpServletResponse response
            , final HttpMethod httpMethod
            , final Locale locale
            , final @RequestHeader MultiValueMap<String, String> headerMap
            , final @RequestHeader("host") String host
            , @CookieValue(value = "myCookie", required = false) String cookie) {

        log.info("request = {}", request);
        log.info("response = {}", response);
        log.info("httpMethod = {}", httpMethod);
        log.info("locale = {}", locale);
        log.info("headerMap = {}", headerMap);
        log.info("host = {}", host);
        log.info("cookie = {}", cookie);

        return "ok";
    }
}
