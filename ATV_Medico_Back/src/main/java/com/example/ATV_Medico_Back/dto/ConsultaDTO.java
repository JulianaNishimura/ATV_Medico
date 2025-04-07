package com.example.ATV_Medico_Back.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultaDTO {
    private Long id;
    private LocalDateTime dataHora;
    private String consultorio;
    private Double valor;
    private LocalDateTime dataRetorno;
    private Long pacienteId;

    // Getters e Setters
}
