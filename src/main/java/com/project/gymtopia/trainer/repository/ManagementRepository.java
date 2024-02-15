package com.project.gymtopia.trainer.repository;

import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Management;
import com.project.gymtopia.trainer.data.entity.Trainer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository extends JpaRepository<Management, Long> {


  Optional<List<Management>> findAllByTrainer(Trainer trainer);

  Optional<Management> findByTrainerAndMember(Trainer trainer, Member member);

}
