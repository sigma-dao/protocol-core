package com.sigma.dao;

import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class App implements CommandLineRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try (Environment env = Environments.newInstance("tmp/storage")) {
            var app = new KVStoreApp(env);
            var server = new GrpcServer(app, 26658);
            server.start();
            server.blockUntilShutdown();
        }
    }
}
