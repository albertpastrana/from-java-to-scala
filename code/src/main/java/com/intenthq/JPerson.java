package com.intenthq;

import java.util.Objects;

public class JPerson {

    private String name;
    private int age;

    public JPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JPerson jPerson = (JPerson) o;
        return age == jPerson.age &&
                Objects.equals(name, jPerson.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, age);
    }
}
