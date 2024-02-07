package com.project.gymtopia.member.repository;

import com.project.gymtopia.member.data.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

}
