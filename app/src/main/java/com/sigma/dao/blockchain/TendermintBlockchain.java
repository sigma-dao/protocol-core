package com.sigma.dao.blockchain;

import com.google.protobuf.ByteString;
import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.service.NetworkConfigService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import tendermint.abci.Types;

import java.util.Base64;

@Slf4j
@Component
public class TendermintBlockchain extends tendermint.abci.ABCIApplicationGrpc.ABCIApplicationImplBase {

    private final NetworkConfigService networkConfigService;
    private final TendermintTransactionHandler tendermintTransactionHandler;
    private final TendermintQueryHandler tendermintQueryHandler;

    public TendermintBlockchain(NetworkConfigService networkConfigService,
                                TendermintTransactionHandler tendermintTransactionHandler,
                                TendermintQueryHandler tendermintQueryHandler) {
        this.networkConfigService = networkConfigService;
        this.tendermintTransactionHandler = tendermintTransactionHandler;
        this.tendermintQueryHandler = tendermintQueryHandler;
    }

    @Override
    public void initChain(Types.RequestInitChain req, StreamObserver<Types.ResponseInitChain> responseObserver) {
        var resp = Types.ResponseInitChain.newBuilder().build();
        final JSONObject appState = new JSONObject(req.getAppStateBytes().toStringUtf8());
        networkConfigService.initializeNetworkConfig(appState);
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
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
        Types.ResponseDeliverTx.Builder builder = Types.ResponseDeliverTx.newBuilder();
        Types.ResponseDeliverTx resp;
        try {
            JSONObject jsonObject = new JSONObject(new String(Base64.getDecoder().decode(tx.toStringUtf8())));
            TendermintTransaction transaction = TendermintTransaction.valueOf(jsonObject.getString("tx"));
            ByteString data = tendermintTransactionHandler.process(transaction, jsonObject);
            resp = builder.setCode(0).setData(data).build();
        } catch(Exception e) {
            resp = builder.setCode(1).setData(ByteString.copyFromUtf8(
                    new JSONObject().put("error", e.getMessage()).toString())).build();
        }
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void commit(Types.RequestCommit req, StreamObserver<Types.ResponseCommit> responseObserver) {
        var resp = Types.ResponseCommit.newBuilder()
                .setData(ByteString.copyFrom(new byte[8])) // TODO - this should hash the entire app state (dump from all DB tables sorted by UUID)
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
        var data = req.getData();
        Types.ResponseQuery.Builder builder = Types.ResponseQuery.newBuilder();
        Types.ResponseQuery resp;
        try {
            JSONObject jsonObject = new JSONObject(new String(Base64.getDecoder().decode(data.toStringUtf8())));
            TendermintQuery query = TendermintQuery.valueOf(jsonObject.getString("query"));
            String result = tendermintQueryHandler.process(query, jsonObject);
            try {
                resp = builder.setCode(0).setLog(new JSONObject(result).toString()).build();
            } catch(Exception e) {
                resp = builder.setCode(0).setLog(new JSONArray(result).toString()).build();
            }
        } catch(Exception e) {
            resp = builder.setCode(1).setLog(new JSONObject().put("error", e.getMessage()).toString()).build();
        }
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}