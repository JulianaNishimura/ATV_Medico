package com.example.ATV_Medico_Back.entity;

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
    private Integer numeroConsultorio; // RQ1

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "especialidade")
    private String especialidade;

    @Column(name = "salario")
    private Double salario;

    @Column(name = "crm", nullable = false, unique = true)
    private String crm;

    @Transient
    private List<Paciente> pacientes = new ArrayList<>(); // Lista auxiliar, preenchida via lógica

    // Método auxiliar para adicionar consulta
    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    // Método auxiliar para adicionar paciente à lista manualmente
    public void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }
}
