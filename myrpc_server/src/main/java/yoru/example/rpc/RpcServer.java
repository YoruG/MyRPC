package yoru.example.rpc;

import yoru.example.entity.RpcException;

/**
 * @author YoruG
 * @date 2020/10/23-10:06
 **/
public interface RpcServer {
    void start();
    <T> void publishService(Object service,Class<T> serviceClass) ;
}
