package yoru.example.rpc;

/**
 * @author YoruG
 * @date 2020/10/23-9:15
 **/
public interface ServiceProvider {
    <T> void addService(T service,String serviceName);
    Object getService(String serviceName);
}
