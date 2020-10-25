package yoru.example.entity;

/**
 * @author YoruG
 * @date 2020/10/23-11:18
 **/
public class RpcException extends Exception {
    public static final String UNKNOWN_PACKAGE_ERROR = "无法识别的数据包";
    public static final String NO_EXIST_SERIALIZER = "不存在序列化器";
    public static final String FAIL_SERIALIZER = "序列化失败";
    public static final String SERVER_REGISTRY_ERROR = "注册中心创建失败";
    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }
}
