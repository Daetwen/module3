package com.epam.esm.builder;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {

    private Long id;
    private String name;
    private String surname;
    private List<Order> orders = new ArrayList<>();

    public User build() {
        User user = new User(id, name, surname, orders);
        this.id = null;
        this.name = null;
        this.surname = null;
        this.orders = null;
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
