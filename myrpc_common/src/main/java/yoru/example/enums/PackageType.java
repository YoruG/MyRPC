package yoru.example.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author YoruG
 * @date 2020/10/23-11:10
 **/

@Getter
public enum PackageType {
    RPC_REQUEST(0),RPC_RESPONSE(1);

    private int code;
    PackageType(int code) {
        this.code = code;
    }

}
