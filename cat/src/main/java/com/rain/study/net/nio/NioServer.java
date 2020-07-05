package com.rain.study.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;

/**
 * nio server
 *
 * @author lazy cat
 * 2020-07-05
 **/
public class NioServer {

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            serverSocket.bind(new InetSocketAddress("localhost", 9999));

            Selector selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            //java的日历对象
            Calendar ca = Calendar.getInstance();
            System.out.println("服务端开启了");
            System.out.println("=========================================================");
            while (true) {
                selector.select();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socket = serverSocket.accept();
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ);
                        String message = "连接成功 你是第" + (selector.keys().size() - 1) + "个用户";
                        socket.write(ByteBuffer.wrap(message.getBytes()));
                        InetSocketAddress address = (InetSocketAddress) socket.getRemoteAddress();
                        System.out.println(ca.getTime() + "\t" + address.getHostString() +
                                ":" + address.getPort() + "\t");
                        System.out.println("客戶端已连接");
                        System.out.println("=========================================================");
                    }

                    if (key.isReadable()) {
                        SocketChannel socket = (SocketChannel) key.channel();
                        InetSocketAddress address = (InetSocketAddress) socket.getRemoteAddress();
                        System.out.println(ca.getTime() + "\t" + address.getHostString() +
                                ":" + address.getPort() + "\t");
                        ByteBuffer bf = ByteBuffer.allocate(1024 * 4);
                        int len;
                        byte[] res = new byte[1024 * 4];
                        try {
                            while ((len = socket.read(bf)) != 0) {
                                bf.flip();
                                bf.get(res, 0, len);
                                System.out.println(new String(res, 0, len));
                                bf.clear();
                            }
                            System.out.println("=========================================================");
                        } catch (IOException e) {
                            key.cancel();
                            socket.close();
                            System.out.println("客戶端已断开");
                            System.out.println("=========================================================");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器异常，即将关闭..........");
            System.out.println("=========================================================");
        }
    }
}
