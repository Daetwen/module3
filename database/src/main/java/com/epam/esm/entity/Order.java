package com.epam.esm.entity;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Audited
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity=User.class)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity=Certificate.class)
    @JoinColumn(name = "gift_certificates_id")
    private Certificate certificate;

    public Order() {}

    public Order(Long id, BigDecimal price, OffsetDateTime createDate, User user, Certificate certificate) {
        this.id = id;
        this.price = price;
        this.createDate = createDate;
        this.user = user;
        this.certificate = certificate;
    }

    public Order(Order order) {
        this.id = order.getId();
        this.price = order.getPrice();
        this.createDate = order.getCreateDate();
        this.user = order.getUser();
        this.certificate = order.getCertificate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public Certificate getCertificate() {
        return new Certificate(certificate);
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = new Certificate(certificate);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Order order = (Order) obj;
        return id.equals(order.id)
                && price.equals(order.price)
                && createDate.equals(order.createDate)
                && user.equals(order.user)
                && certificate.equals(order.certificate);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + (id != null ? id.hashCode() : 0);
        result = result * prime + (price != null ? price.hashCode() : 0);
        result = result * prime + (createDate != null ? createDate.hashCode() : 0);
        result = result * prime + (user != null ? user.hashCode() : 0);
        result = result * prime + (certificate != null ? certificate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order { ")
                .append("id = '").append(id).append('\'')
                .append(", price = '").append(price).append('\'')
                .append(", createDate = '").append(createDate).append('\'')
                .append(", user = '").append(user).append('\'')
                .append(", certificate = '").append(certificate).append('\'')
                .append(" }\n");
        return stringBuilder.toString();
    }
}
