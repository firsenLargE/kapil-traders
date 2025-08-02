package com.example.KapilTraders.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uname;
    private String email;
    private String password;
}
