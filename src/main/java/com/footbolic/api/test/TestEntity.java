package com.footbolic.api.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "TestEntity")
public class TestEntity {

    @Id
    private String id;

    @Column
    private String val1;

    @Column
    private String val2;
}
