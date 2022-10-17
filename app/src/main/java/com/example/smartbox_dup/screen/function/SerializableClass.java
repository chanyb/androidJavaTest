package com.example.smartbox_dup.screen.function;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class SerializableClass implements Serializable, Cloneable {
    String name;
    int age;
    String address;
    int level;

    public SerializableClass() {

    }

    public SerializableClass(String _name) {
        this.name = _name;
    }

    public SerializableClass(String _name, String _address) {
        this.name = _name;
        this.address = _address;
    }

    public SerializableClass(String _name, int _age, String _address, int _level) {
        this.name = _name;
        this.age = _age;
        this.address = _address;
        this.level = _level;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // super.clone은 기본적으로 얕은복사라서 만약, implementation한 클래스에 배열과 같은 가변필드가 있다면, 원본 객체와 주소를 공유하게 된다.
        return super.clone();
    }
}

