package com.project.gymtopia.member.repository;

import com.project.gymtopia.member.data.entity.Register;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

  void deleteAllByAcceptYnIsFalseAndRegisterDateAfter(LocalDate oneWeekAgo);

  @Modifying
  @Transactional
  @Query("delete from Register r where r.id in :registerIdList")
  void deleteAllInQuery(List<Long> registerIdList);

}
