package com.ws.netty.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Jun
 * data  2019-08-22 21:58
 * 客户端  代码有问题,客户端收不到服务端发出的数据!!!
 */
public class TestNio12 {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();

        Selector selector = Selector.open();

        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8888));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {

            if (selector.select() > 0) {

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    SocketChannel channel = null;
                    if (selectionKey.isConnectable()) {
                        channel = (SocketChannel) selectionKey.channel();
                        //channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {

                        channel = (SocketChannel) selectionKey.channel();

                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        channel.read(allocate);

                        StringBuilder builder = new StringBuilder();

                        while (channel.read(allocate) > 0) {
                            allocate.flip();
                            CharBuffer decode = Charset.defaultCharset().decode(allocate);
                            builder.append(decode);
                            allocate.clear();
                        }
                        System.out.println(builder.toString());
                    }
                    iterator.remove();
                }

            }
        }
    }
}
