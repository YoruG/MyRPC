package yoru.example.enums;

import lombok.Getter;

/**
 * @author YoruG
 * @date 2020/10/23-14:44
 **/
@Getter
public enum SerializerType {
    KRYO_SERIALIZER(0),JSON_SERIALIZER(1),HESSIAN_SERIALIZER(2);

    private int code;
    SerializerType(int code) {
        this.code = code;
    }

}
