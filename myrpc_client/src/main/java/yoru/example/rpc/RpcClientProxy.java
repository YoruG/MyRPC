package yoru.example.rpc;

import yoru.example.entity.RpcRequest;
import yoru.example.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author YoruG
 * @date 2020/10/22-21:50
 **/
public class RpcClientProxy implements InvocationHandler {
    private String localhost;
    private Integer port;
    private RpcClient client;
    public RpcClientProxy(String localhost, Integer port) {
        this.localhost = localhost;
        this.port = port;
    }

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    /**
     * 获得接口的代理对象
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }

    /**
     * 客户端通过动态代理在执行本地没有实现的方法时
     * 通过远程调用服务端的实现方法来得到最终结果
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .heartBeat(false)
                .paramTypes(method.getParameterTypes()).build();
        RpcResponse response = (RpcResponse)(client.sendRequest(rpcRequest));

        return response.getData();
    }
}
