package com.project.gymtopia.trainer.service;

import com.project.gymtopia.trainer.data.model.SpecialProgramForm;
import com.project.gymtopia.trainer.data.model.SpecialProgramList;

public interface SpecialProgramService {

  void registerProgram(String email, SpecialProgramForm specialProgramForm);

  void closeProgram(String email, long programId);

  SpecialProgramList findAllSpecialProgram(String email);

  void applyProgram(String email, long programId);
}
