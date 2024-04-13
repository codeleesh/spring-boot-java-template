package io.dodn.springboot.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Name {

    @Column
    private String first;
    @Column
    private String last;

    protected Name() {
    }

    public Name(final String nameOfFirst, final String nameOfLast) {
        this.first = nameOfFirst;
        this.last = nameOfLast;
    }

    public String getFullName() {

        return this.first + this.last;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(first, name.first) && Objects.equals(last, name.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last);
    }

    @Override
    public String toString() {
        return "Name{" +
                "first='" + first + '\'' +
                ", last='" + last + '\'' +
                '}';
    }
}
