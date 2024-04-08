package com.ph30891.asm_ph30891_gd2.model;

public class User {
    private String _id, username, password, email, name,avatar, available;
    private String createdAt, updatedAt;
    private int role;

    public User(String _id, String username, String password, String email, String name, String avatar, String available, String createdAt, String updatedAt, int role) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    public User() {
    }

    public String get_id() {
        return _id;
    }

    public User set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getAvailable() {
        return available;
    }

    public User setAvailable(String available) {
        this.available = available;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public User setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public int getRole() {
        return role;
    }

    public User setRole(int role) {
        this.role = role;
        return this;
    }
}
