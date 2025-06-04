package com.bizondam.userservice.mapper;

import com.bizondam.userservice.dto.response.SignUpResponse;
import com.bizondam.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMapper {
  // User → SignUpResponse DTO 매핑
  SignUpResponse toDto(User user);
}
