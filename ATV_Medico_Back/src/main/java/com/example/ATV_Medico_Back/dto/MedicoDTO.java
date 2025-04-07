package com.example.ATV_Medico_Back.dto;

import java.util.List;

import lombok.*;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicoDTO {
    private Long id;
    private Integer numeroConsultorio;
    private String nome;
    private String especialidade;
    private Double salario;
    private String crm;
    private List<Long> pacientesIds;
    private List<Long> consultasIds;

    // Getters e Setters
}
