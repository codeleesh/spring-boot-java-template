package io.dodn.springboot.storage.db.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dodn.springboot.storage.db.order.entity.core.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Member")
@Getter
@Setter
public class Member extends BaseEntity {

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    private Integer age;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    protected Member() {
    }

    public Member(final String nameOfFirst, final String nameOfLast, final String mail, final String password,
                  final int age, final Address address) {

        this.name = new Name(nameOfFirst, nameOfLast);
        this.email = new Email(mail);
        this.password = new Password(password);
        this.age = age;
        this.address = address;
    }

    public static Member of(final String nameOfFirst, final String nameOfLast, final Address address) {

        return new Member(nameOfFirst, nameOfLast, null, null, 0, address);
    }

    public String getFullName() {
        return this.name.getFullName();
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public Integer getAge() {
        return age;
    }

}
