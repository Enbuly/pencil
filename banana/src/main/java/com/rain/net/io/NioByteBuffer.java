package com.rain.net.io;

import java.nio.ByteBuffer;

/**
 * ByteBuffer
 *
 * @author lazy cat
 * 2020-07-08
 **/
public class NioByteBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("------" + "allocate" + "------");
        printByteBuffer(byteBuffer);
        System.out.println();

        byteBuffer.put("abc".getBytes());
        System.out.println("------" + "put(abc)" + "------");
        printByteBuffer(byteBuffer);
        System.out.println();

        byteBuffer.flip();
        System.out.println("------" + "flip" + "------");
        printByteBuffer(byteBuffer);
        System.out.println();

        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println("------" + "get" + "------");
        printByteBuffer(byteBuffer);
        System.out.println(new String(bytes));
        System.out.println();

        byteBuffer.clear();
        System.out.println("------" + "clear" + "------");
        printByteBuffer(byteBuffer);
        System.out.println();
    }

    private static void printByteBuffer(ByteBuffer byteBuffer) {
        System.out.println("position:" + byteBuffer.position());
        System.out.println("limit:" + byteBuffer.limit());
        System.out.println("capacity:" + byteBuffer.capacity());
    }
}
