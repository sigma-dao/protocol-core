package com.sigma.dao;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.grpc.GrpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class App implements CommandLineRunner {

    private final GrpcServer grpcServer;

    public App(GrpcServer grpcServer) {
        this.grpcServer = grpcServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        grpcServer.start();
        grpcServer.blockUntilShutdown();
    }
}
