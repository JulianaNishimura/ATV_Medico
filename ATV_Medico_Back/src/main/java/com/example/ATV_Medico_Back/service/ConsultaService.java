package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.dto.ConsultaDTO;
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
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultaDTO> listarPorMedico(Long medicoId) {
        if (!medicoRepository.existsById(medicoId)) {
            throw new IllegalArgumentException("Médico com ID " + medicoId + " não encontrado.");
        }
        return consultaRepository.findByMedicoId(medicoId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultaDTO> listarPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new IllegalArgumentException("Paciente com ID " + pacienteId + " não encontrado.");
        }
        return consultaRepository.findByPacienteId(pacienteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public String salvarConsulta(ConsultaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente inválido ou não encontrado."));

        if (dto.getDataHora() != null && dto.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da consulta deve ser futura.");
        }

        if (dto.getDataRetorno() != null && dto.getDataHora() != null &&
                dto.getDataRetorno().isBefore(dto.getDataHora())) {
            throw new IllegalArgumentException("A data de retorno deve ser posterior à data da consulta.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setDataHora(dto.getDataHora());
        consulta.setConsultorio(dto.getConsultorio());
        consulta.setValor(dto.getValor());
        consulta.setDataRetorno(dto.getDataRetorno());

        consultaRepository.save(consulta);
        return "Consulta cadastrada com sucesso!";
    }

    public String atualizarConsulta(Long id, ConsultaDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta com ID " + id + " não encontrada."));

        if (dto.getPacienteId() != null && !pacienteRepository.existsById(dto.getPacienteId())) {
            throw new IllegalArgumentException("Paciente inválido ou não encontrado.");
        }

        if (dto.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(dto.getPacienteId()).get();
            consulta.setPaciente(paciente);
        }
        if (dto.getDataHora() != null) {
            if (dto.getDataHora().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("A data da consulta deve ser futura.");
            }
            consulta.setDataHora(dto.getDataHora());
        }
        if (dto.getConsultorio() != null) consulta.setConsultorio(dto.getConsultorio());
        if (dto.getValor() != null) consulta.setValor(dto.getValor());
        if (dto.getDataRetorno() != null) {
            if (consulta.getDataHora() != null && dto.getDataRetorno().isBefore(consulta.getDataHora())) {
                throw new IllegalArgumentException("A data de retorno deve ser posterior à data da consulta.");
            }
            consulta.setDataRetorno(dto.getDataRetorno());
        }

        consultaRepository.save(consulta);
        return "Consulta atualizada com sucesso!";
    }

    public String deletarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta com ID " + id + " não encontrada."));
        consultaRepository.delete(consulta);
        return "Consulta deletada com sucesso!";
    }

    private ConsultaDTO toDTO(Consulta consulta) {
        ConsultaDTO dto = new ConsultaDTO();
        dto.setId(consulta.getId());
        dto.setPacienteId(consulta.getPaciente().getId());
        dto.setDataHora(consulta.getDataHora());
        dto.setConsultorio(consulta.getConsultorio());
        dto.setValor(consulta.getValor());
        dto.setDataRetorno(consulta.getDataRetorno());
        return dto;
    }
}
