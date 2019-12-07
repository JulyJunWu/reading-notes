package com.ws.mybatis.util;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.ws.mybatis.model.User;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author hession序列化工具
 */
@Slf4j
public class HessianUtils {

    /**
     * 将对象序列化成字节数组
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        try {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            Hessian2Output hessian2Output = new Hessian2Output(arrayOutputStream);
            hessian2Output.writeObject(object);
            hessian2Output.flush();
            return arrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.info("序列化异常,{}", e.getMessage());
            return null;
        }
    }

    /**
     * 将字节数组反序列化为对象
     *
     * @param array
     * @return
     */
    public static Object unSerialize(byte[] array) {
        try {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(array);
            Hessian2Input hessian2Input = new Hessian2Input(arrayInputStream);
            return hessian2Input.readObject();
        } catch (Exception e) {
            log.error("反序列化异常,{}", e.getMessage());
            return null;
        }
    }

}
