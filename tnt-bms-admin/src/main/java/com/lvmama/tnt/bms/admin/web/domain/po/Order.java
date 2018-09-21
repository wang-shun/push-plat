package com.lvmama.tnt.bms.admin.web.domain.po;

import sun.rmi.runtime.Log;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement
public class Order {
    private String name;
    private String orderId;
    private String amount;
    private String status;
    private String product;
    private String channel;
    private String payway;
    private String company;
    private String visitor;
    private String createUser;

    private Long startMillis;
    private Long endMillis;

    public Long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(Long startMillis) {
        this.startMillis = startMillis;
    }

    public Long getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(Long endMillis) {
        this.endMillis = endMillis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", product='" + product + '\'' +
                ", channel='" + channel + '\'' +
                ", payway='" + payway + '\'' +
                ", company='" + company + '\'' +
                ", visitor='" + visitor + '\'' +
                ", createUser='" + createUser + '\'' +
                '}';
    }
}
