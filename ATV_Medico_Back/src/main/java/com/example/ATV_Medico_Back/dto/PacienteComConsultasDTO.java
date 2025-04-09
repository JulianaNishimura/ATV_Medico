package com.example.ATV_Medico_Back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteComConsultasDTO {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private List<ConsultaDTO> consultas;
}

