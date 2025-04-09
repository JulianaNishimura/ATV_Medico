package com.example.ATV_Medico_Back.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // Gera getters, setters, toString, equals e hashCode
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDTO {
    private Long id;

    private LocalDateTime dataHora;
    private Double valor;
    private LocalDateTime dataRetorno;
    private Long medicoId;
    private Long pacienteId;

    // Getters e Setters
}
