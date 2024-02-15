package com.project.gymtopia.common.repository;

import com.project.gymtopia.common.data.MissionState;
import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Trainer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

  List<Mission> findByMemberAndState(Member member, MissionState state);

  Optional<Mission> findByIdAndState(long id, MissionState state);

  Optional<Mission> findByTrainerAndMember(Trainer trainer, Member member);

  Optional<List<Mission>> findAllByMember(Member member);



}
