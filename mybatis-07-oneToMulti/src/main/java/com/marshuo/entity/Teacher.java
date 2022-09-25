package com.marshuo.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mars
 * @date 2022/09/24
 */
@Data
public class Teacher {
    private int tid;
    private String tname;
    private List<Student> studentList;
}
