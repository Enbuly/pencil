package com.rain.study.designPatterns.simpleFactory;

/**
 * @author lazy cat
 * @since 2019-07-15
 **/
public class ConcreteProduct2 implements Product {

    @Override
    public void hello() {
        System.out.println("ConcreteProduct2...");
    }
}
