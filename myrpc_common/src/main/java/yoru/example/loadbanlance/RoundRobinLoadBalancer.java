package yoru.example.loadbanlance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author YoruG
 * @date 2020/10/24-0:42
 **/
public class RoundRobinLoadBalancer implements LoadBalancer {
    public static int index = 0;
    @Override
    public Instance select(List<Instance> instanceList) {
        index %= instanceList.size();
        Instance result = instanceList.get(index++);
        return result;
    }
}
