package com.example.ATV_Medico_Back.dto;

import lombok.*;

import java.time.LocalDate;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PacienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
}
