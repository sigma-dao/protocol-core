package com.sigma.dao.grpc;

import com.sigma.dao.blockchain.TendermintBlockchain;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
public class GrpcServer {

    private Server server;

    @Value("${grpc.port}")
    private Integer port;

    private final TendermintBlockchain tendermintBlockchain;

    public GrpcServer(TendermintBlockchain tendermintBlockchain) {
        this.tendermintBlockchain = tendermintBlockchain;
    }

    @PostConstruct
    private void init() {
        this.server = ServerBuilder.forPort(port)
                .addService(tendermintBlockchain)
                .build();
    }

    public void start() throws IOException {
        server.start();
        log.info("gRPC server started, listening on {}", this.port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            log.info("gRPC server shut down");
        }));
    }

    private void stop() {
        server.shutdown();
    }

    public void blockUntilShutdown() throws InterruptedException {
        server.awaitTermination();
    }
}