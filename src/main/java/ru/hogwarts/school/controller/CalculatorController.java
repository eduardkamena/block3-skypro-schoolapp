package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "mod_integer_calculating")
public class CalculatorController {

    private final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    public CalculatorController() {
    }

    @GetMapping
    public void modifyIntegerCalculating() {
        // base method starts
        logger.info("Was invoked method for calculating integer");
        long startTime = System.currentTimeMillis();

        int sum;

        sum = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b); // Integer::sum

        long finishBaseMethodTime = System.currentTimeMillis() - startTime;
        logger.info("Time {} millis", finishBaseMethodTime);

        // mod method starts with parallel stream
        startTime = System.currentTimeMillis();
        sum = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);

        long finishModMethodTime = System.currentTimeMillis() - startTime;
        logger.info("Time {} millis", finishModMethodTime);

        // cycle method starts
        startTime = System.currentTimeMillis();
        sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;
        }

        long finishCycleMethodTime = System.currentTimeMillis() - startTime;
        logger.info("Time {} millis", finishCycleMethodTime);
    }

}
