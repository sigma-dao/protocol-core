package com.sigma.dao;

import com.sigma.dao.grpc.GrpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SigmaDAO implements CommandLineRunner {

    private final GrpcServer grpcServer;

    public SigmaDAO(GrpcServer grpcServer) {
        this.grpcServer = grpcServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(SigmaDAO.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        grpcServer.start();
        grpcServer.blockUntilShutdown();
    }
}