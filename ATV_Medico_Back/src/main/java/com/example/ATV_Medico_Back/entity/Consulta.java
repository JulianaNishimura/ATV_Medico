package com.example.ATV_Medico_Back.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora; // RQ2: Agendar em horários diferentes

    @Column(name = "valor")
    private Double valor; // RQ6: Relatório financeiro

    @Column(name = "data_retorno")
    private LocalDateTime dataRetorno; // RQ5: Agendar retornos

}
