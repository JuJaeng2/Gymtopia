package com.project.gymtopia.member.repository;

import com.project.gymtopia.member.data.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


  Optional<Member> findByEmail(String email);

  Optional<Member> findByName(String username);

  boolean existsByEmail(String email);
}
