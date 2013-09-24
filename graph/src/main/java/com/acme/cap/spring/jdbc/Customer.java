package com.acme.cap.spring.jdbc;

import com.google.common.base.Objects;

public class Customer {

    private long id;
    private String firstName, lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(Customer.class)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .toString();
    }
}
