package com.tsfintech.rest.example.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.tsfintech.rest.core.meta.RestEntity;

/**
 * Created by jack on 14-8-1.
 */
@Entity
@RestEntity(entityScope = "shopId", defaultFind = true, defaultCount = true)
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long shopId;

    private String code;

    private BigDecimal payment;

    private Date orderDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String sayHello(String name, String name2) {
        return "Hello " + name + " and " + name2;
    }
}
