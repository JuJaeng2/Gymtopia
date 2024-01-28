package com.project.gymtopia.common.repository;

import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.member.data.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

  Optional<Mission> findByMember(Member member);

}
