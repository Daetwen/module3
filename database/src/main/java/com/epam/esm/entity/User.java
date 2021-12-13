package com.epam.esm.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public User() {}

    public User(Long id, String name, String surname, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Order> getOrders() {
        List<Order> resultOrders = new ArrayList<>();
        if (this.orders != null && !this.orders.isEmpty()) {
            for (Order element : this.orders) {
                Order order = new Order(element);
                resultOrders.add(order);
            }
        }
        return resultOrders;
    }

    public void setOrders(List<Order> orders) {
        if (orders != null) {
            this.orders = new ArrayList<>();
            for (Order element : orders) {
                Order order = new Order(element);
                this.orders.add(order);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return id.equals(user.id) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                orders.equals(user.orders);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + (id != null ? id.hashCode() : 0);
        result = result * prime + (name != null ? name.hashCode() : 0);
        result = result * prime + (surname != null ? surname.hashCode() : 0);
        result = result * prime + (orders != null ? orders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("User { ")
                .append("id = '").append(id).append('\'')
                .append(", name = '").append(name).append('\'')
                .append(", surname = '").append(surname).append('\'');
        stringBuilder.append(", orders = [");
        for(Order order : this.orders) {
            stringBuilder.append(order).append(", ");
        }
        stringBuilder.append("] }\n");
        return stringBuilder.toString();
    }
}
