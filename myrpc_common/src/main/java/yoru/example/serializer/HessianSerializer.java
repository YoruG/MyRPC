package yoru.example.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author YoruG
 * @date 2020/10/24-20:50
 **/
public class HessianSerializer implements CommonSerializer{
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    @Override
    public byte[] serialize(Object object) {
        HessianOutput hessianOutput = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            hessianOutput = new HessianOutput(outputStream);
            hessianOutput.writeObject(object);
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("HessianSerializer在序列化时出现错误",e);
            return null;
        }finally {
            try {
                hessianOutput.close();
            } catch (IOException e) {
                logger.error("HessianSerializer关闭流时",e);
            }
        }

    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        HessianInput hessianInput = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            hessianInput = new HessianInput(inputStream);
            Object object = hessianInput.readObject();
            return object;
        } catch (IOException e) {
            logger.error("HessianSerializer在反序列化时出现错误",e);
            return null;
        }finally {
            hessianInput.close();
        }
    }

    @Override
    public int getCode() {
        return 2;
    }
}
