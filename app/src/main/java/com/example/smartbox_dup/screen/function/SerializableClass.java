package com.example.smartbox_dup.screen.function;

import java.io.Serializable;

public class SerializableClass implements Serializable {
    String name;
    int age;
    String address;
    int level;

    public SerializableClass(String _name, int _age, String _address, int _level) {
        this.name = _name;
        this.age = _age;
        this.address = _address;
        this.level = _level;
    }


    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}

