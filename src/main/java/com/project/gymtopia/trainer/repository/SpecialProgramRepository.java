package com.project.gymtopia.trainer.repository;

import com.project.gymtopia.trainer.data.entity.SpecialProgram;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialProgramRepository extends JpaRepository<SpecialProgram, Long> {

  //TODO: 동시성 처리를 위해 Pessimistic Lock 필요

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select s from SpecialProgram s where s.id = :id")
  Optional<SpecialProgram> findByIdWithPessimisticLock(long id);

}
