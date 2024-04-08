package com.ph30891.asm_ph30891_gd2.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Fruit implements Serializable {
    private String _id;
    private String name;
    private int quantity;
    private double price;
    private int status;
    private ArrayList<String> image;
    private String description;
//    @SerializedName("id_distributor")
    private String id_distributor;

    public Fruit(String _id, String name, int quantity, double price, int status, ArrayList<String> image, String description, String id_distributor) {
        this._id = _id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.image = image;
        this.description = description;
        this.id_distributor = id_distributor;
    }

    public Fruit() {
    }

    public String get_id() {
        return _id;
    }

    public Fruit set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Fruit setName(String name) {
        this.name = name;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Fruit setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Fruit setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Fruit setStatus(int status) {
        this.status = status;
        return this;
    }

    public ArrayList<String> getImages() {
        return image;
    }

    public Fruit setImages(ArrayList<String> images) {
        this.image = images;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Fruit setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDistributorId() {
        return id_distributor;
    }

    public Fruit setDistributorId(String id_distributor) {
        this.id_distributor = id_distributor;
        return this;
    }
}
