package com.rain.study.net.bio_nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * bio - nio server
 *
 * @author lazy cat
 * 2020-07-05
 **/
public class BnServer {

    public static void main(String[] args) throws Exception {
        System.out.println("================" + "welcome to server" + "================");
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        List<SocketChannel> list = new ArrayList<>();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9876));
        serverSocketChannel.configureBlocking(false);
        while (true) {
            for (int i = 0; i < list.size(); i++) {
                int read = list.get(i).read(byteBuffer);
                if (read > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[read];
                    byteBuffer.get(bytes);
                    String content = new String(bytes);
                    System.out.println(content);
                    byteBuffer.flip();
                    list.remove(list.get(i));
                }
            }
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                list.add(socketChannel);
                if (list.size() > 0) {
                    System.out.println("list size is " + list.size());
                }
            }
        }
    }
}
