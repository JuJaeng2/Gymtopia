package com.project.gymtopia.trainer.repository;

import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.trainer.data.entity.FeedBack;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

  Optional<FeedBack> findByJournal(Journal journal);

}
