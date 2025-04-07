package com.example.ATV_Medico_Back.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultaDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private LocalDateTime dataHora;
    private String consultorio;
    private Double valor;
    private LocalDateTime dataRetorno;
    private Long pacienteId;

    // Getters e Setters
}
