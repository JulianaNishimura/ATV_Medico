package com.example.ATV_Medico_Back.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_consultorio", nullable = false)
    private Integer numeroConsultorio; // RQ1: Gerir qual consultório utilizar

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paciente> pacientes = new ArrayList<>(); // RQ3: Controle explícito da lista de pacientes

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>(); // RQ2: Agendar pacientes em horários diferentes
    // RQ5: Agendar retornos

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "especialidade")
    private String especialidade;

    @Column(name = "valor_consulta")
    private Double valorConsulta; // RQ6: Relatório financeiro

    @Column(name = "salario")
    private Double salario;

    @Column(name = "crm", nullable = false, unique = true)
    private String crm;

    // Método auxiliar para adicionar paciente
    public void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);
        paciente.setMedico(this);
    }

    // Método auxiliar para adicionar consulta
    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
        consulta.setMedico(this);
    }
}