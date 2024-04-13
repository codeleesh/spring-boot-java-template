package io.dodn.springboot.storage.db.core;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class MemberEntity extends BaseEntity {

    @Embedded
    private Name name;
    @Embedded
    private Email email;
    @Embedded
    private Password password;
    private Integer age;

    protected MemberEntity() {
    }

    public MemberEntity(final String nameOfFirst, final String nameOfLast, final String mail, final String password, final int age) {

        this.name = new Name(nameOfFirst, nameOfLast);
        this.email = new Email(mail);
        this.password = new Password(password);
        this.age = age;
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
