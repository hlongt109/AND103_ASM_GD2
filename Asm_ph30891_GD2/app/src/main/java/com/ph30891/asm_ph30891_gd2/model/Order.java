package com.ph30891.asm_ph30891_gd2.model;

public class Order {
    private String _id, id_user, id_product;
    private int quantity;
    private double total;
    private String name_customer, phone_customer,location, pay_method;
    private int status;

    public Order(String _id, String id_user, String id_product, int quantity, double total, String name_customer, String phone_customer, String location, String pay_method,int status) {
        this._id = _id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.quantity = quantity;
        this.total = total;
        this.name_customer = name_customer;
        this.phone_customer = phone_customer;
        this.location = location;
        this.pay_method = pay_method;
        this.status = status;
    }

    public Order(String id_user, String id_product, int quantity, double total, String name_customer, String phone_customer, String location, String pay_method, int status) {
        this.id_user = id_user;
        this.id_product = id_product;
        this.quantity = quantity;
        this.total = total;
        this.name_customer = name_customer;
        this.phone_customer = phone_customer;
        this.location = location;
        this.pay_method = pay_method;
        this.status = status;
    }

    public Order() {
    }

    public String getId() {
        return _id;
    }

    public Order setId(String _id) {
        this._id = _id;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Order setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getId_user() {
        return id_user;
    }

    public Order setId_user(String id_user) {
        this.id_user = id_user;
        return this;
    }

    public String getId_product() {
        return id_product;
    }

    public Order setId_product(String id_product) {
        this.id_product = id_product;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Order setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public double getTotal() {
        return total;
    }

    public Order setTotal(double total) {
        this.total = total;
        return this;
    }

    public String getName_customer() {
        return name_customer;
    }

    public Order setName_customer(String name_customer) {
        this.name_customer = name_customer;
        return this;
    }

    public String getPhone_customer() {
        return phone_customer;
    }

    public Order setPhone_customer(String phone_customer) {
        this.phone_customer = phone_customer;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Order setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getPay_method() {
        return pay_method;
    }

    public Order setPay_method(String pay_method) {
        this.pay_method = pay_method;
        return this;
    }
}
