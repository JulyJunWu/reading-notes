package com.ws.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Jun
 * data  2019-08-22 20:00
 * selector
 */
public class TestNio11 {

    public static void main(String[] args) throws Exception {

        Map<SocketChannel, String> map = new HashMap<>();

        //选择器
        Selector selector = Selector.open();

        ServerSocketChannel open = ServerSocketChannel.open();
        open.configureBlocking(false);
        open.bind(new InetSocketAddress(8888));
        open.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            int select = selector.select();

            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {

                    SelectionKey selectionKey = iterator.next();

                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        String uuid = UUID.randomUUID().toString();
                        map.put(socketChannel, uuid);
                        System.out.println("玩家注册->" + uuid);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        StringBuilder sb = new StringBuilder();
                        try {
                            while (socketChannel.read(allocate) > 0) {
                                allocate.flip();
                                sb.append(Charset.forName("gbk").decode(allocate));
                                allocate.clear();
                            }
                        } catch (IOException e) {
                            map.remove(socketChannel);
                            continue;
                        }

                        Iterator<SocketChannel> channelIterator = map.keySet().iterator();

                        String message = sb.toString();
                        while (channelIterator.hasNext()) {

                            SocketChannel next = channelIterator.next();
                            if (!next.isConnected()) {
                                map.remove(next);
                                channelIterator.remove();
                                continue;
                            }

                            if (next == socketChannel) continue;
                            next.write(Charset.defaultCharset().encode(message));
                        }

                        System.out.println(message);
                    }
                    iterator.remove();
                }
            }
        }
    }
}
