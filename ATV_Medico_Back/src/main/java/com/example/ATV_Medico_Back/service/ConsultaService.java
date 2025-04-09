package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.dto.ConsultaDTO;
import com.example.ATV_Medico_Back.entity.Consulta;
import com.example.ATV_Medico_Back.entity.Medico;
import com.example.ATV_Medico_Back.entity.Paciente;
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

    public List<ConsultaDTO> listarPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new IllegalArgumentException("Paciente com ID " + pacienteId + " não encontrado.");
        }
        return consultaRepository.findByPacienteId(pacienteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public String salvarConsulta(ConsultaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente inválido ou não encontrado."));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new IllegalArgumentException("Médico inválido ou não encontrado."));

        validarDataConsulta(dto);

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dto.getDataHora());
        consulta.setValor(dto.getValor());
        consulta.setDataRetorno(dto.getDataRetorno());

        // Adicionar consulta nas listas de consultas de médico e paciente
        medico.adicionarConsulta(consulta);
        paciente.adicionarConsulta(consulta);

        // Adicionar paciente na lista de pacientes do médico, se necessário
        if (!medico.getPacientes().contains(paciente)) {
            medico.adicionarPaciente(paciente);
        }

        consultaRepository.save(consulta);
        return "Consulta cadastrada com sucesso!";
    }

    public String atualizarConsulta(Long id, ConsultaDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta com ID " + id + " não encontrada."));

        Paciente paciente = consulta.getPaciente();
        Medico medico = consulta.getMedico();

        if (dto.getPacienteId() != null) {
            Paciente novoPaciente = pacienteRepository.findById(dto.getPacienteId()).get();
            // Remover consulta do paciente antigo e adicionar ao novo paciente
            paciente.getConsultas().remove(consulta);
            novoPaciente.adicionarConsulta(consulta);
            consulta.setPaciente(novoPaciente);

            // Adicionar o novo paciente à lista de pacientes do médico
            if (!medico.getPacientes().contains(novoPaciente)) {
                medico.adicionarPaciente(novoPaciente);
            }
        }

        if (dto.getMedicoId() != null) {
            Medico novoMedico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + dto.getMedicoId() + " não encontrado."));
            // Remover consulta do médico antigo e adicionar ao novo médico
            medico.getConsultas().remove(consulta);
            novoMedico.adicionarConsulta(consulta);
            consulta.setMedico(novoMedico);

            // Adicionar o paciente à lista de pacientes do novo médico
            if (!novoMedico.getPacientes().contains(paciente)) {
                novoMedico.adicionarPaciente(paciente);
            }

            // Remover paciente da lista de médicos antigos, se necessário
            if (medico.getPacientes().contains(paciente)) {
                medico.getPacientes().remove(paciente);
            }
        }

        if (dto.getDataHora() != null) {
            validarDataConsulta(dto);
            consulta.setDataHora(dto.getDataHora());
        }

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

        Paciente paciente = consulta.getPaciente();
        Medico medico = consulta.getMedico();

        // Remover consulta das listas de consultas de paciente e médico
        paciente.getConsultas().remove(consulta);
        medico.getConsultas().remove(consulta);

        // Remover paciente da lista de médicos, se necessário
        if (medico.getPacientes().contains(paciente)) {
            medico.getPacientes().remove(paciente);
        }

        consultaRepository.delete(consulta);
        return "Consulta deletada com sucesso!";
    }

    private void validarDataConsulta(ConsultaDTO dto) {
        if (dto.getDataHora() != null && dto.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da consulta deve ser futura.");
        }

        if (dto.getDataRetorno() != null && dto.getDataHora() != null &&
                dto.getDataRetorno().isBefore(dto.getDataHora())) {
            throw new IllegalArgumentException("A data de retorno deve ser posterior à data da consulta.");
        }
    }

    private ConsultaDTO toDTO(Consulta consulta) {
        ConsultaDTO dto = new ConsultaDTO();
        dto.setId(consulta.getId());
        dto.setPacienteId(consulta.getPaciente().getId());
        dto.setMedicoId(consulta.getMedico().getId());
        dto.setDataHora(consulta.getDataHora());
        dto.setValor(consulta.getValor());
        dto.setDataRetorno(consulta.getDataRetorno());
        return dto;
    }
}
