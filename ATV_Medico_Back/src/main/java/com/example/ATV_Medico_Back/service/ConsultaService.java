package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.model.Consulta;
import com.example.ATV_Medico_Back.model.Medico;
import com.example.ATV_Medico_Back.model.Paciente;
import com.example.ATV_Medico_Back.repository.ConsultaRepository;
import com.example.ATV_Medico_Back.repository.MedicoRepository;
import com.example.ATV_Medico_Back.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Lista todas as consultas
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    // Lista consultas por médico
    public List<Consulta> listarPorMedico(Long medicoId) {
        if (!medicoRepository.existsById(medicoId)) {
            throw new IllegalArgumentException("Médico com ID " + medicoId + " não encontrado.");
        }
        return consultaRepository.findByMedicoId(medicoId);
    }

    // Lista consultas por paciente (histórico - RQ4)
    public List<Consulta> listarPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new IllegalArgumentException("Paciente com ID " + pacienteId + " não encontrado.");
        }
        return consultaRepository.findByPacienteId(pacienteId);
    }

    // Salva uma nova consulta com validações
    public String salvarConsulta(Consulta consulta) {
        // Verifica se o médico existe
        if (consulta.getMedico() == null || !medicoRepository.existsById(consulta.getMedico().getId())) {
            throw new IllegalArgumentException("Médico inválido ou não encontrado.");
        }

        // Verifica se o paciente existe
        if (consulta.getPaciente() == null || !pacienteRepository.existsById(consulta.getPaciente().getId())) {
            throw new IllegalArgumentException("Paciente inválido ou não encontrado.");
        }

        // Verifica se a data da consulta é futura
        if (consulta.getDataHora() != null && consulta.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da consulta deve ser futura.");
        }

        // Verifica se a data de retorno, se informada, é posterior à data da consulta
        if (consulta.getDataRetorno() != null && consulta.getDataHora() != null &&
                consulta.getDataRetorno().isBefore(consulta.getDataHora())) {
            throw new IllegalArgumentException("A data de retorno deve ser posterior à data da consulta.");
        }

        consultaRepository.save(consulta);
        return "Consulta cadastrada com sucesso!";
    }

    // Atualiza uma consulta existente
    public String atualizarConsulta(Long id, Consulta consultaAtualizada) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta com ID " + id + " não encontrada."));

        // Verifica se o médico existe
        if (consultaAtualizada.getMedico() != null && !medicoRepository.existsById(consultaAtualizada.getMedico().getId())) {
            throw new IllegalArgumentException("Médico inválido ou não encontrado.");
        }

        // Verifica se o paciente existe
        if (consultaAtualizada.getPaciente() != null && !pacienteRepository.existsById(consultaAtualizada.getPaciente().getId())) {
            throw new IllegalArgumentException("Paciente inválido ou não encontrado.");
        }

        // Atualiza os campos
        if (consultaAtualizada.getMedico() != null) consulta.setMedico(consultaAtualizada.getMedico());
        if (consultaAtualizada.getPaciente() != null) consulta.setPaciente(consultaAtualizada.getPaciente());
        if (consultaAtualizada.getDataHora() != null) {
            if (consultaAtualizada.getDataHora().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("A data da consulta deve ser futura.");
            }
            consulta.setDataHora(consultaAtualizada.getDataHora());
        }
        if (consultaAtualizada.getConsultorio() != null) consulta.setConsultorio(consultaAtualizada.getConsultorio());
        if (consultaAtualizada.getValor() != null) consulta.setValor(consultaAtualizada.getValor());
        if (consultaAtualizada.getDataRetorno() != null) {
            if (consultaAtualizada.getDataRetorno().isBefore(consulta.getDataHora())) {
                throw new IllegalArgumentException("A data de retorno deve ser posterior à data da consulta.");
            }
            consulta.setDataRetorno(consultaAtualizada.getDataRetorno());
        }

        consultaRepository.save(consulta);
        return "Consulta atualizada com sucesso!";
    }

    // Deleta uma consulta
    public String deletarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta com ID " + id + " não encontrada."));
        consultaRepository.delete(consulta);
        return "Consulta deletada com sucesso!";
    }
}