package com.project.gymtopia.member.repository;

import com.project.gymtopia.common.data.JournalType;
import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.member.data.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

  Optional<List<Journal>> findAllByMember(Member member);

  List<Journal> findAllByMemberAndType(Member member, JournalType journalType);

}
