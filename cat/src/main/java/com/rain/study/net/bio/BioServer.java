package com.rain.study.net.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * bio server
 *
 * @author lazy cat
 * 2020-07-05
 **/
public class BioServer {

    public static void main(String[] args) throws IOException {
        byte[] bytes = new byte[1024];

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 9999));
        while (true) {
            System.out.println("wait connect...");
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress().getHostAddress() + " connect success...");
            new Thread(() -> {
                int read = 0;
                System.out.println("wait client write...");
                try {
                    read = socket.getInputStream().read(bytes);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("server read success...");
                if (read > 0) {
                    System.out.println("the client message is " +
                            new String(bytes, 0, read, Charset.forName("utf-8")));
                }
            }).start();
        }
    }
}
