package com.rain.study.AboutComparable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Student
 *
 * @author lazy cat
 * 2020-07-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
class Student implements Comparable<Student> {
    private String name;

    private int ranking;

    public int compareTo(Student student) {
        return this.ranking - student.ranking;
    }
}
