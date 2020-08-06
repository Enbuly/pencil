package com.rain.study.AboutEqualsHashCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * about equals and hashcode
 *
 * @author lazy cat
 * 2020-08-06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private int id;
    private String name;

    public static void main(String[] args) {
        Person person1 = new Person(1, "Lisa");
        Person person2 = new Person(1, "Lisa");
        HashMap<Person, Integer> persons = new HashMap<>();
        persons.put(person1, 10);
        System.out.println(persons.get(person2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person person = (Person) obj;

            return name.equals(person.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
