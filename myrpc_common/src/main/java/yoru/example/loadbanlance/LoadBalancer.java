package yoru.example.loadbanlance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author YoruG
 * @date 2020/10/24-0:39
 **/
public interface LoadBalancer {
    Instance select(List<Instance> instanceList);
}
