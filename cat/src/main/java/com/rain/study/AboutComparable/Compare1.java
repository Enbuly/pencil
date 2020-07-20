package com.rain.study.AboutComparable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * demo
 *
 * @author lazy cat
 * 2020-07-20
 **/
public class Compare1 {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(10);
        arr.add(23);
        arr.add(7);
        System.out.println(arr);
        Collections.sort(arr);
        System.out.println(arr);
    }
}
