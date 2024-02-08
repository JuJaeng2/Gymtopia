package com.project.gymtopia.member.repository;

import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.entity.Review;
import com.project.gymtopia.trainer.data.entity.Trainer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  Optional<Review> findByMemberAndTrainer(Member member, Trainer trainer);

  Page<Review> findAllByTrainer(Trainer trainer, Pageable pageable);
}
