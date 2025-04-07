package com.example.ATV_Medico_Back.repository;

import com.example.ATV_Medico_Back.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByDataHoraBetween(LocalDateTime start, LocalDateTime end);
}