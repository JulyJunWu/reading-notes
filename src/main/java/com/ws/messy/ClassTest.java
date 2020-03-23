package com.ws.messy;

import com.ws.mybatis.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author JunWu
 * @since 2020/3/23 20:57
 * 反射的测试
 */
@Slf4j
public class ClassTest {


    /**
     * 反射为什么会比 set/get 慢 ??
     *
     * 普通方法快 是因为不需要去询址可以直接复制
     * 反射是因为:
     *  1.首次获取Field/Method/Constructor 等需要调用JNI获取对应的 属性
     *  2.将JNI返回数据进行缓存(后续就不要需要第一步了,直接从缓存里查找数据)
     *  3.遍历Field/Method/Constructor 获取我们 所需要的字段/方法/构造
     *  也就是说字段越多/方法越多/构造器越多,那么速度越慢
     *
     *
     * 结论:
     *  1.一般而言, 两者性能差距忽略不计, 一般在几毫秒内
     *  2.若上百万/千万的调用,那么性能确实很大
     *
     */
    @Test
    public void testField() throws Exception {
        int loop = 10000000;
        int size = 0;
        User user = new User();
        long start = System.currentTimeMillis();
        while (size++ < loop) {
            user.setId("8888");
        }
        log.info("普通方法测试 | 次数:{} | 耗时:{}", loop, System.currentTimeMillis() - start);

        // 主要耗时的地方
        Field field = User.class.getDeclaredField("id");
        field.setAccessible(true);
        start = System.currentTimeMillis();
        size = 0;
        while (size++ < loop) {
            field.set(user, "6666");
        }
        log.info("普通方法测试 | 次数:{} | 耗时:{}", loop, System.currentTimeMillis() - start);
    }


}
