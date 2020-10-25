package yoru.example.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcRequest;

import java.io.IOException;

/**
 * @author YoruG
 * @date 2020/10/23-10:46
 **/
public class JsonSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            logger.error("JsonSerializer在序列化时出现错误");
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest)
                obj = handleRequest(obj);
            return obj;
        } catch (IOException e) {
            logger.error("JsonSerializer在反序列化时出现错误",e);
            return null;
        }
    }

    /**
     * 将request反序列化时如果遇到Object类型，则由于未知其字段类型，所以会出现
     * 反序列化失败的情况，此时就需要手动序列化
     * @param object
     * @return
     * @throws IOException
     */
    private Object handleRequest(Object object) throws IOException{
        RpcRequest request = (RpcRequest) object;
        for (int i = 0; i < request.getParamTypes().length; i++) {
            Class<?> clazz = request.getParamTypes()[i];
            if (!clazz.isAssignableFrom(request.getParameters()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(request.getParameters()[i]);
                request.getParameters()[i] = objectMapper.readValue(bytes,clazz);
            }
        }
        return request;
    }

    @Override
    public int getCode() {
        return 1;
    }
}
