package com.ws.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author Jun
 * data  2019-08-21 21:03
 * Scattering 与 Gathering
 *
 */
public class TestNio10 {

    public static void main(String[] args)throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(8888));

        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 9;

        ByteBuffer [] byteBuffers = new ByteBuffer[3];
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        while (true){

            int readBytes = 0;

            while (readBytes < messageLength){

                long read = socketChannel.read(byteBuffers);

                if (read == -1)break;

                readBytes += read;

                Arrays.asList(byteBuffers).stream().map(buffer->"position=" + buffer.position()  + "  limit=" + buffer.limit())
                        .forEach(System.out::println);
            }
            Arrays.asList(byteBuffers).stream().forEach(buffer->buffer.flip());

            int writeBytes = 0;
            while (writeBytes < messageLength){
                writeBytes += socketChannel.write(byteBuffers);
            }

            Arrays.asList(byteBuffers).stream().forEach(b->b.clear());

            System.out.println("readBytes=" + readBytes + " writeBytes=" + writeBytes);
        }


    }

}
