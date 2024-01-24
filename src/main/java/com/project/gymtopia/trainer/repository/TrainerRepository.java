package com.project.gymtopia.trainer.repository;

import com.project.gymtopia.trainer.data.entity.Trainer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

  Optional<Trainer> findByEmail(String email);

  boolean existsByEmail(String email);
}
