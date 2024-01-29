package com.project.gymtopia.member.repository;

import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.member.data.entity.Media;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

  Optional<List<Media>> findALlByJournal(Journal journal);
}
