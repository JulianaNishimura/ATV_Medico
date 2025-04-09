package com.example.ATV_Medico_Back.repository;

import com.example.ATV_Medico_Back.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Medico findByCrm(String crm);
}