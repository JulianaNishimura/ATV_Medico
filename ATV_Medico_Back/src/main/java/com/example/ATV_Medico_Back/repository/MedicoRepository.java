package com.example.ATV_Medico_Back.repository;

import com.example.ATV_Medico_Back.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    List<Medico> findByNumeroConsultorio(Integer numeroConsultorio);
    List<Medico> findByEspecialidade(String especialidade);
    List<Medico> findByNomeContainingIgnoreCase(String nome);
    // Novo método: buscar médico por CRM
    Medico findByCrm(String crm);
}