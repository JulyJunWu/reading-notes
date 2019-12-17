package com.ws.book.netty权威指南.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * @author JunWu
 * 生成编解码工厂
 */
public class MarshallingFactory {

    /**
     * 创建解码器
     *
     * @return
     */
    public static MarshallingDecoder createMarshallingDecoder() {

        MarshallerFactory providedMarshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(providedMarshallerFactory, configuration);

        return new MarshallingDecoder(provider);
    }

    /**
     * 创建编码器
     *
     * @return
     */
    public static MarshallingEncoder createMarshallingEncoder() {

        MarshallerFactory providedMarshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(providedMarshallerFactory, configuration);

        return new MarshallingEncoder(provider);
    }

}
