package com.rain.study.net.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * bio client
 *
 * @author lazy cat
 * 2020-07-05
 **/
public class BioClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 9999);
        OutputStream os = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要发送的内容:");
        os.write(scanner.next().getBytes());
        socket.close();
    }
}
