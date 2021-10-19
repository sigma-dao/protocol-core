package com.sigma.dao.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.sigma.dao.blockchain.constant.TendermintTransaction;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Fund;
import com.sigma.dao.response.ErrorResponse;
import com.sigma.dao.service.FundService;
import com.sigma.dao.service.NetworkConfigService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import tendermint.abci.Types;

import java.util.Base64;

@Slf4j
@Component
public class TendermintBlockchain extends tendermint.abci.ABCIApplicationGrpc.ABCIApplicationImplBase {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final NetworkConfigService networkConfigService;
    private final FundService fundService;

    public TendermintBlockchain(NetworkConfigService networkConfigService, FundService fundService) {
        this.networkConfigService = networkConfigService;
        this.fundService = fundService;
    }

    @Override
    public void initChain(Types.RequestInitChain req, StreamObserver<Types.ResponseInitChain> responseObserver) {
        var resp = Types.ResponseInitChain.newBuilder().build();
        final JSONObject appState = new JSONObject(req.getAppStateBytes().toStringUtf8());
        try {
            networkConfigService.initializeNetworkConfig(appState);
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        } catch (JsonProcessingException e) {
            responseObserver.onError(new ProtocolException(e.getMessage()));
        }
    }

    @Override
    public void info(Types.RequestInfo request, StreamObserver<tendermint.abci.Types.ResponseInfo> responseObserver) {
        var resp = Types.ResponseInfo.newBuilder().build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void echo(Types.RequestEcho request, StreamObserver<tendermint.abci.Types.ResponseEcho> responseObserver) {
        var resp = Types.ResponseEcho.newBuilder().build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void checkTx(Types.RequestCheckTx req, StreamObserver<Types.ResponseCheckTx> responseObserver) {
        var resp = Types.ResponseCheckTx.newBuilder()
                .setCode(0)
                .setGasWanted(1)
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void beginBlock(Types.RequestBeginBlock req, StreamObserver<Types.ResponseBeginBlock> responseObserver) {
        var resp = Types.ResponseBeginBlock.newBuilder().build();
        networkConfigService.setTimestamp(req.getHeader().getTime().getSeconds());
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void deliverTx(Types.RequestDeliverTx req, StreamObserver<Types.ResponseDeliverTx> responseObserver) {
        var tx = req.getTx();
        String result;
        Types.ResponseDeliverTx.Builder builder = Types.ResponseDeliverTx.newBuilder();
        Types.ResponseDeliverTx resp;
        try {
            JSONObject jsonObject = new JSONObject(new String(Base64.getDecoder().decode(tx.toStringUtf8())));
            TendermintTransaction transaction = TendermintTransaction.valueOf(jsonObject.getString("tx"));
            if (transaction.equals(TendermintTransaction.CREATE_FUND)) {
                try {
                    Fund fund = objectMapper.readValue(jsonObject.getJSONObject("payload").toString(), Fund.class);
                    fund = fundService.create(fund);
                    result = objectMapper.writeValueAsString(fund);
                    resp = builder.setCode(0).setLog(result).build();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    String errorJson = objectMapper.writeValueAsString(new ErrorResponse().setCode(e.getMessage()));
                    resp = builder.setCode(1).setLog(errorJson).build();
                }
            } else {
                String errorJson = objectMapper.writeValueAsString(new ErrorResponse()
                        .setCode(String.format("Unsupported tx: %s", transaction)));
                resp = builder.setCode(1).setLog(errorJson).build();
            }
        } catch(Exception e) {
            String errorJson = "{}";
            try {
                errorJson = objectMapper.writeValueAsString(new ErrorResponse()
                        .setCode(String.format("Unknown error: %s", e.getMessage())));
            } catch(Exception e2) {
                log.error(e2.getMessage(), e2);
            }
            resp = builder.setCode(1).setLog(errorJson).build();
        }
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void commit(Types.RequestCommit req, StreamObserver<Types.ResponseCommit> responseObserver) {
        var resp = Types.ResponseCommit.newBuilder()
                .setData(ByteString.copyFrom(new byte[8]))
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void endBlock(Types.RequestEndBlock req, StreamObserver<Types.ResponseEndBlock> responseObserver) {
        var resp = Types.ResponseEndBlock.newBuilder().build();
        // TODO - scheduled stuff can happen here at the end of the block
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void query(Types.RequestQuery req, StreamObserver<Types.ResponseQuery> responseObserver) {
        var data = req.getData().toStringUtf8();
        var builder = Types.ResponseQuery.newBuilder();
        builder.setLog("TODO");
//        builder.setKey(ByteString.copyFrom(k));
//        builder.setValue(ByteString.copyFrom(v));
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}