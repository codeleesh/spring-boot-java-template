package io.dodn.springboot.storage.db.core;

import io.dodn.springboot.storage.db.CoreDbContextTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest extends CoreDbContextTest {

    private final MemberRepository memberRepository;

    public MemberRepositoryTest(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    void save_and_find() {

        final MemberEntity member = new MemberEntity("이", "상호", "lovethefeel@gmail.com", "password", 38);
        final MemberEntity savedMember = memberRepository.save(member);

        assertAll(
                () -> assertThat(savedMember.getEmail()).isEqualTo("lovethefeel@gmail.com")
        );

        final MemberEntity foundMember = memberRepository.findById(savedMember.getId()).get();

        assertAll(
                () -> assertThat(foundMember.getEmail()).isEqualTo("lovethefeel@gmail.com")
        );

    }
}