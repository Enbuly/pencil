package com.rain.study.AboutComparable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * demo
 *
 * @author lazy cat
 * 2020-07-20
 **/
public class Compare2 {

    public static void main(String[] args) {
        ArrayList<Student> arr = new ArrayList<>();
        arr.add(new Student("Jack", 10));
        arr.add(new Student("Bill", 23));
        arr.add(new Student("Rudy", 7));
        System.out.println(arr);
        Collections.sort(arr);
        System.out.println(arr);
    }
}
