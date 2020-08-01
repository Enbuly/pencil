package com.rain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private int id;

    private int count;
}
