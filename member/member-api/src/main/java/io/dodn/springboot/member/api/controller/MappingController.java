package io.dodn.springboot.member.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/mapping-get-v1")
    public String mappingGetV1() {

        log.info("mapping-get-v1");
        return "ok";
    }

    /**
     * 편라힌 축약 어노테이션
     * @GetMapping
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {

        log.info("mapping-get-v2");
        return "ok";
    }

    /**
     * PathVariable 사용
     * 변수명이 같으면 생략 가능
     */
    @GetMapping("/mapping-get-v3/{userId}")
    public String mappingGetV3(final @PathVariable String userId) {

        log.info("mapping-get-v3 userId {}", userId);
        return "ok";
    }

    /**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping-get-v4/users/{userId}/orders/{orderId}")
    public String mappingGetV3(final @PathVariable("userId") String userId, final @PathVariable("orderId") Long orderId) {

        log.info("mapping-get-v3 userId {}, orderId {}", userId, orderId);
        return "ok";
    }

    /**
     * 파라미터로 추가 매핑
     * params="mode"
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" ( != )
     * params={"mode=debug", "data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode = debug")
    public String mappingParams() {

        log.info("mappingParams");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode"
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" ( != )
     */
    @GetMapping(value = "/mapping-param", headers = "mode = debug")
    public String mappingHeaders() {

        log.info("mappingHeaders");
        return "ok";
    }

    /**
     * 미디어타입 조건 매핑 - HTTP 요청 Content-Type, consume
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    public String mappingConsumes() {

        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * 미디어타입 조건 매핑 - HTTP 요청 Accept, produce
     * Accept 헤더 기반 추가 매핑 Media Type
     * produces="text/html"
     * produces="!text/html"
     * produces="text/*"
     * produces="*\/*"
     */
    @PostMapping(value = "/mapping-produce", consumes = "text/html")
    public String mappingProduces() {

        log.info("mappingProduces");
        return "ok";
    }
}
