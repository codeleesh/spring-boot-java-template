package io.dodn.springboot.member.api.controller;

public class HelloData {

    private String username;
    private int age;

    public HelloData() {
    }

    public HelloData(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
