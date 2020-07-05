package com.rain.study.net.bio_nio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * bio - nio client
 *
 * @author lazy cat
 * 2020-07-05
 **/
public class BnClients {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9876);
        OutputStream os = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入ni丫要发送的内容:");
        os.write(scanner.next().getBytes());
        socket.close();
    }
}
