package com.sigma.dao;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GrpcServer {

    private final Server server;
    private final int port;

    GrpcServer(BindableService service, int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    void start() throws IOException {
        server.start();
        log.info("gROC server started, listening on {}", this.port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            log.info("gRPC server shut down");
        }));
    }

    private void stop() {
        server.shutdown();
    }

    void blockUntilShutdown() throws InterruptedException {
        server.awaitTermination();
    }
}