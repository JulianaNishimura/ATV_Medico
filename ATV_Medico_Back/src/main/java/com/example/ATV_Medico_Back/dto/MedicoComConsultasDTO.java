package com.example.ATV_Medico_Back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoComConsultasDTO {
    private Long id;
    private String nome;
    private String especialidade;
    private Integer numeroConsultorio;
    private Double salario;
    private String crm;

    private List<ConsultaDTO> consultas;
    private List<PacienteDTO> pacientes;

}

