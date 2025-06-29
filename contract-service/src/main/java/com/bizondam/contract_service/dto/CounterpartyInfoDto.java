package com.bizondam.contract_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CounterpartyInfoDto {
  private String nameKr;
  private String email;
  private String phoneNumber;
  private String department;
  private String position;
}