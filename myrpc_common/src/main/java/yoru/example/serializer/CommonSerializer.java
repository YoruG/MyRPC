package yoru.example.serializer;

/**
 * @author YoruG
 * @date 2020/10/23-10:40
 **/
public interface CommonSerializer {
    byte[] serialize(Object object);
    Object deserialize(byte[] bytes,Class<?> clazz);
    int getCode();

    /**
     * 获取指定序列化器，当前版本有Kryo序列化器-0、JSON序列化器-1
     * @param code  序列化器对应序号
     * @return
     */
    static CommonSerializer getByCode(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            default:
                return null;
        }
    }
}
