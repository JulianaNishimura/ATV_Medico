package com.example.ATV_Medico_Back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // Gera getters, setters, toString, equals e hashCode
@AllArgsConstructor
@NoArgsConstructor
public class MedicoDTO {
    private Long id;

    private String nome;
    private String especialidade;
    private Integer numeroConsultorio;
    private Double salario;
    private String crm;
    // Getters e Setters
}
