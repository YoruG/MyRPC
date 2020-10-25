package yoru.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YoruG
 * @date 2020/10/22-21:45
 **/
@Data
public class RpcResponse<T> implements Serializable {
    public static final int SUCCESS_CODE = 1;
    public static final int FAIL_CODE = 0;
    private Integer statusCode;
    private String message;
    private T data;

    /**
     * 返回成功标志的响应
     * @param data  数据
     * @param <T>   传输数据类型
     * @return
     */
    public static<T> RpcResponse<T> success(T data){
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setData(data);
        rpcResponse.setStatusCode(SUCCESS_CODE);
        return rpcResponse;
    }

    /**
     * 返回失败标志的响应
     * @param message   失败消息
     * @param <T>
     * @return
     */
    public static<T> RpcResponse<T> fail(String message){
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setMessage(message);
        rpcResponse.setStatusCode(FAIL_CODE);
        return rpcResponse;
    }
}
