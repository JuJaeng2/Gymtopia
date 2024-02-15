package com.project.gymtopia.member.service;

import com.project.gymtopia.member.data.model.RegisterForm;

public interface MemberRegisterService {

  void register(String email, long trainerId, RegisterForm registerForm);
}
