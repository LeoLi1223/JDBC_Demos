package com.nocilantro.Blob;

public class Celebrity {
    private String name;

    public Celebrity() {
    }

    public Celebrity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "name='" + name + '\'' +
                '}';
    }
}
