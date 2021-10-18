package com.sigma.dao.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.ByteString;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.service.NetworkConfigService;
import io.grpc.stub.StreamObserver;
import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import tendermint.abci.Types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TendermintBlockchain extends tendermint.abci.ABCIApplicationGrpc.ABCIApplicationImplBase {

    private final NetworkConfigService networkConfigService;

    private final Environment env = Environments.newInstance("tmp/storage");
    private Transaction txn = null;
    private Store store = null;

    public TendermintBlockchain(NetworkConfigService networkConfigService) {
        this.networkConfigService = networkConfigService;
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
        var tx = req.getTx();
        int code = validate(tx);
        var resp = Types.ResponseCheckTx.newBuilder()
                .setCode(code)
                .setGasWanted(1)
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void beginBlock(Types.RequestBeginBlock req, StreamObserver<Types.ResponseBeginBlock> responseObserver) {
        txn = env.beginTransaction();
        store = env.openStore("store", StoreConfig.WITHOUT_DUPLICATES, txn);
        var resp = Types.ResponseBeginBlock.newBuilder().build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void deliverTx(Types.RequestDeliverTx req, StreamObserver<Types.ResponseDeliverTx> responseObserver) {
        var tx = req.getTx();
        int code = validate(tx);
        if (code == 0) {
            List<byte[]> parts = split(tx, '=');
            var key = new ArrayByteIterable(parts.get(0));
            var value = new ArrayByteIterable(parts.get(1));
            store.put(txn, key, value);
        }
        var resp = Types.ResponseDeliverTx.newBuilder()
                .setCode(code)
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void commit(Types.RequestCommit req, StreamObserver<Types.ResponseCommit> responseObserver) {
        txn.commit();
        var resp = Types.ResponseCommit.newBuilder()
                .setData(ByteString.copyFrom(new byte[8]))
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void endBlock(Types.RequestEndBlock req, StreamObserver<Types.ResponseEndBlock> responseObserver) {
        var resp = Types.ResponseEndBlock.newBuilder().build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void query(Types.RequestQuery req, StreamObserver<Types.ResponseQuery> responseObserver) {
        var k = req.getData().toByteArray();
        var v = getPersistedValue(k);
        var builder = Types.ResponseQuery.newBuilder();
        if (v == null) {
            builder.setLog("does not exist");
        } else {
            builder.setLog("exists");
            builder.setKey(ByteString.copyFrom(k));
            builder.setValue(ByteString.copyFrom(v));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private int validate(ByteString tx) {
        List<byte[]> parts = split(tx, '=');
        if (parts.size() != 2) {
            return 1;
        }
        byte[] key = parts.get(0);
        byte[] value = parts.get(1);

        // check if the same key=value already exists
        var stored = getPersistedValue(key);
        if (stored != null && Arrays.equals(stored, value)) {
            return 2;
        }

        return 0;
    }

    private List<byte[]> split(ByteString tx, char separator) {
        var arr = tx.toByteArray();
        int i;
        for (i = 0; i < tx.size(); i++) {
            if (arr[i] == (byte)separator) {
                break;
            }
        }
        if (i == tx.size()) {
            return Collections.emptyList();
        }
        return List.of(
                tx.substring(0, i).toByteArray(),
                tx.substring(i + 1).toByteArray()
        );
    }

    private byte[] getPersistedValue(byte[] k) {
        return env.computeInReadonlyTransaction(txn -> {
            var store = env.openStore("store", StoreConfig.WITHOUT_DUPLICATES, txn);
            ByteIterable byteIterable = store.get(txn, new ArrayByteIterable(k));
            if (byteIterable == null) {
                return null;
            }
            return byteIterable.getBytesUnsafe();
        });
    }
}