package io.dodn.springboot.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Email {

    @Column
    private String email;

    protected Email() {}

    public Email(final String mail) {
        this.email = mail;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                '}';
    }
}
