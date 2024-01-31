package com.project.gymtopia.member.repository;

import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.member.data.entity.Media;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

  Optional<List<Media>> findALlByJournal(Journal journal);

  @Modifying
  @Transactional
  @Query("delete from Media m where m.journal = :journal")
  void deleteAllInQuery(Journal journal);


}
