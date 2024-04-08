package com.ph30891.asm_ph30891_gd2.model;

import com.google.gson.annotations.SerializedName;

public class Distributor {
    @SerializedName("_id")
    private String id;
    private String name , createdAt, updatedAt;

    public Distributor(String id, String name, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Distributor() {
    }

    public String getId() {
        return id;
    }

    public Distributor setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Distributor setName(String name) {
        this.name = name;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Distributor setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Distributor setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
