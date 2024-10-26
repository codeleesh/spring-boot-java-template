package io.dodn.springboot.member.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Log level TRACE > DEBUG > INFO > WARN > ERROR 기본 Log level info logging.leve.info=root
 */
@RestController
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("log-test")
    public String logTest() {

        final String name = "Spring";

        // 연산을 먼저 진행 -> 메모리 사용 그 후 trace로 넘김
        log.trace("trace log=" + name);
        // 파라미터만 치환해서 trace로 넘김
        log.trace("trace log={}", name);

        log.debug("race log={}", name);
        log.info("race log={}", name);
        log.warn("race log={}", name);
        log.error("race log={}", name);

        return "ok";
    }

}
