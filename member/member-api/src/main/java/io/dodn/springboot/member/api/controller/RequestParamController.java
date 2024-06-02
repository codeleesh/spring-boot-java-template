package io.dodn.springboot.member.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Get 쿼리 파라미터 전송 또는 Post HTML Form 전송은 같은 형식
 */
@Controller
public class RequestParamController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") final String username, @RequestParam("age") final int age) throws IOException {

        log.info("username={}, age={}", username, age);

        return "ok";
    }
}
