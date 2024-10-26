package io.dodn.springboot.member.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

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

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") final String username, @RequestParam("age") final int age) {

        log.info("username={}, age={}", username, age);

        return "ok";
    }

    @Deprecated
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam final String username, @RequestParam final int age) {

        log.info("username={}, age={}", username, age);

        return "ok";
    }

    @Deprecated
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(final String username, final int age) {

        log.info("username={}, age={}", username, age);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(value = "username", required = true) final String username,
            @RequestParam(value = "age", required = false) final int age) {

        log.info("username={}, age={}", username, age);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(value = "username", defaultValue = "guest") final String username,
            @RequestParam(value = "age", defaultValue = "-1") final int age) {

        log.info("username={}, age={}", username, age);

        return "ok";
    }

    @Deprecated
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam("paramMap") Map<String, Object> paramMap) {

        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));

        return "ok";
    }

    /**
     * 스프링MVC는 `@ModelAttribute` 가 있으면 다음을 실행 `HelloData` 객체 생성 요청 파라미터의 이름으로 `HelloData`
     * 객체의 프로퍼티를 찾음 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩)
     * @param helloData
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * `@ModelAttribute`, `@RequestParam` 생략 가능 스프링은 해당 생략시 다음과 같은 규칙을 적용 `String`, `int`,
     * `Integer` 같은 단순 타입 - `@RequestParam` 나머지 - `@ModelAttribute (argument resolver로
     * 지정해둔 타입 외)
     * @param helloData
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

}
