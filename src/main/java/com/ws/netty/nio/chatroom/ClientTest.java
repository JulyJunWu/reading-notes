package com.ws.netty.nio.chatroom;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 */
public class ClientTest {

    public static ThreadPoolExecutor threadPoolExecutor;

    public static void main(String[] args) throws Exception {
        LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("ws-").build();
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, blockingQueue, build);

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

        threadPoolExecutor.execute(new ReceiveDataTask(selector));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            boolean connected = socketChannel.isConnected();
            boolean pending = socketChannel.isConnectionPending() && socketChannel.finishConnect();
            boolean isOk = (connected || pending) && scanner.hasNext();
            if (isOk) {
                String nextLine = scanner.nextLine();
                socketChannel.write(Charset.defaultCharset().encode(nextLine));
            }
        }
    }
}

class ReceiveDataTask implements Runnable {

    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    StringBuilder sb = new StringBuilder();
    private Selector selector;

    public ReceiveDataTask(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int select = selector.select();
                if (select > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        if (next.isReadable()) {
                            sb.delete(0, sb.length());
                            SocketChannel channel = (SocketChannel) next.channel();
                            while (channel.read(byteBuffer) > 0) {
                                byteBuffer.flip();
                                sb.append(Charset.defaultCharset().decode(byteBuffer));
                                byteBuffer.clear();
                            }
                            System.out.println("来自服务端消息:" + sb.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
