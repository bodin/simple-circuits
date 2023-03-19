/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.github.bodin;

import io.github.bodin.annotation.CircuitDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Configuration
    public static class Circuits {
        @CircuitDefinition("test-circuit")
        interface TestCircuit extends Circuit {}
    }

    @Autowired @Lazy
    Circuits.TestCircuit TestCircuit;

    @Bean
    public RouterFunction<ServerResponse> endpoints() {
        return RouterFunctions.route()
                .GET("/hello", req ->
                        ok().body(TestCircuit
                                .supply(() -> "Test Circuit Is: Closed")
                                .orElse("Test Circuit Is: Open"))
                )
            .build();
    }
}