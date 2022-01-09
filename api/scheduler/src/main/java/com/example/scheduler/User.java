package com.example.scheduler;

import java.util.Objects;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;

    User()  {}

    User(String name, String email, String password ){
        this.name = name;
        this.email = email;
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User)o;
        return Objects.equals(this.id, user.id) && Objects.equals(this.name, user.name)
                && Objects.equals(this.email, user.email)
                && Objects.equals(this.password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.email, this.password);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", name='" + this.name +
                '\'' + ", email='" + this.email +
                '\'' + '}';
    }
}
