package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "port")
public class InfoController {

    private final Logger logger = LoggerFactory.getLogger(InfoController.class);

    public InfoController() {
    }

    @Value("${server.port}")
    private Integer port;

    @GetMapping
    public int getPort() {
        logger.info("Was invoked controller method for getting server port");
        return port;
    }

}
