package com.example.ATV_Medico_Back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {
    private Long id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
}
