package com.ws.netty.nio.chatroom;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author JunWu
 */
public class ServerTest {

    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(8888));

        while (true) {
            int select = selector.select();
            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    int ops = getInterestingOps(next);
                    SocketChannel socketChannel = null;
                    switch (ops) {
                        case SelectionKey.OP_ACCEPT:
                            ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                            socketChannel = channel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("<<<注册事件>>>");
                            break;
                        case SelectionKey.OP_READ:
                            try {
                                socketChannel = (SocketChannel) next.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                StringBuilder sb = new StringBuilder();
                                while (socketChannel.read(byteBuffer) > 0) {
                                    byteBuffer.flip();
                                    sb.append(Charset.defaultCharset().decode(byteBuffer));
                                    byteBuffer.clear();
                                }
                                String message = sb.toString();
                                System.out.println("来自客户端消息:" + message);
                                broadMessage(selector, message, socketChannel);
                            } catch (Exception e) {
                                e.printStackTrace();
                                socketChannel.close();
                                next.cancel();
                            }
                            break;
                        default:
                            System.out.println("<<<<default>>>>");
                            break;
                    }
                }
            }
        }
    }

    public static int getInterestingOps(SelectionKey selectionKey) {
        int ops = -1;
        if (selectionKey.isReadable()) {
            ops = SelectionKey.OP_READ;
        } else if (selectionKey.isAcceptable()) {
            ops = SelectionKey.OP_ACCEPT;
        }
        return ops;
    }

    public static void broadMessage(Selector selector, String message, SocketChannel socketChannel) {
        Set<SelectionKey> keys = selector.keys();

        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            try {
                SelectionKey next = iterator.next();
                if (next.channel() instanceof SocketChannel && socketChannel != next.channel()) {
                    ((SocketChannel) next.channel()).write(Charset.defaultCharset().encode(message));
                }
            } catch (Exception e) {
                System.out.println("未知错误!");
            }
        }
    }
}
