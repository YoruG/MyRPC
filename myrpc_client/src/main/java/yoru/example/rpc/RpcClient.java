package yoru.example.rpc;

import yoru.example.entity.RpcRequest;

/**
 * @author YoruG
 * @date 2020/10/23-10:00
 **/
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
