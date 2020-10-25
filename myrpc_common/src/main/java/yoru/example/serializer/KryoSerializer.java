package yoru.example.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yoru.example.entity.RpcRequest;
import yoru.example.entity.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.function.Supplier;

/**
 * @author YoruG
 * @date 2020/10/23-13:53
 **/
public class KryoSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);
    public static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(new Supplier<Kryo>() {
        @Override
        public Kryo get() {
            Kryo kryo = new Kryo();
            kryo.register(RpcRequest.class);
            kryo.register(RpcResponse.class);
            kryo.setReferences(true);
            kryo.setRegistrationRequired(false);
            kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    });


    @Override
    public byte[] serialize(Object object) {
        Output output = null;
        try {
            Kryo kryo = kryoThreadLocal.get();
            output = new Output(new ByteArrayOutputStream());
            kryo.writeObject(output,object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            logger.error("KryoSerializer在序列化时出现错误",e);
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        Input input = null;
        try {
            Kryo kryo = kryoThreadLocal.get();
            input = new Input(new ByteArrayInputStream(bytes));
            Object object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return object;
        } catch (Exception e) {
            logger.error("KryoSerializer在反序列化时出现错误",e);
            return null;
        }
    }

    @Override
    public int getCode() {
        return 0;
    }


}
