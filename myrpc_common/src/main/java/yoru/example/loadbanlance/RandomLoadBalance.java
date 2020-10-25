package yoru.example.loadbanlance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * @author YoruG
 * @date 2020/10/24-0:40
 **/
public class RandomLoadBalance implements LoadBalancer {
    @Override
    public Instance select(List<Instance> instanceList) {
        int index = new Random().nextInt(instanceList.size());
        Instance result = instanceList.get(index);
        return result;
    }
}
