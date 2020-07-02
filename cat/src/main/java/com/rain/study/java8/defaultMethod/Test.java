package com.rain.study.java8.defaultMethod;

/**
 * 接口的默认方法
 *
 * @author lazy cat
 * @since 2020-02-23
 **/
public class Test {

    public static void main(String[] args) {
        Formula formula = new FormulaImpl();
        System.out.println(formula.calculate(100));
        System.out.println(formula.sqrt(16));
    }
}
