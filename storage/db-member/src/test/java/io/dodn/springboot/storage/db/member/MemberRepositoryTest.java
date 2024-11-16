package io.dodn.springboot.storage.db.member;

import io.dodn.springboot.storage.db.MemberDbContextTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest extends MemberDbContextTest {

    private final MemberRepository memberRepository;

    public MemberRepositoryTest(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    void save_and_find() {

        final Member member = new Member("이", "상호", "lovethefeel@gmail.com", "password", 38);
        final Member savedMember = memberRepository.save(member);
        assertAll(() -> assertThat(savedMember.getEmail()).isEqualTo("lovethefeel@gmail.com"));

        final Member foundMember = memberRepository.findById(savedMember.getId()).get();
        assertAll(() -> assertThat(foundMember.getEmail()).isEqualTo("lovethefeel@gmail.com"));
    }

}