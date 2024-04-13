package io.dodn.springboot.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Password {

    @Column
    private String password;

    protected Password() {}

    public Password(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                '}';
    }
}
