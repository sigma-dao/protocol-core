package com.sigma.dao;

import com.sigma.dao.grpc.GrpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
public class SigmaDAO implements CommandLineRunner {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final GrpcServer grpcServer;
    @Value("${grpc.enabled}")
    private Boolean grpcEnabled;

    public SigmaDAO(GrpcServer grpcServer) {
        this.grpcServer = grpcServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(SigmaDAO.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(grpcEnabled) {
            grpcServer.start();
            grpcServer.blockUntilShutdown();
        }
    }
}