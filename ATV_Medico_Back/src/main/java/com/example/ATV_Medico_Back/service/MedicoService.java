package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.dto.*;
import com.example.ATV_Medico_Back.entity.Consulta;
import com.example.ATV_Medico_Back.entity.Medico;
import com.example.ATV_Medico_Back.entity.Paciente;
import com.example.ATV_Medico_Back.repository.MedicoRepository;
import com.example.ATV_Medico_Back.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicoComConsultasDTO> listarMedicosComConsultas() {
        return medicoRepository.findAll().stream()
                .map(this::toDTOConsulta)
                .collect(Collectors.toList());
    }

    public MedicoComConsultasDTO buscarPorCrm(String crm) {
        Medico medico = medicoRepository.findByCrm(crm);
        if (medico == null) {
            throw new IllegalArgumentException("Médico com CRM " + crm + " não encontrado.");
        }
        return toDTOConsulta(medico);
    }

    public String salvarMedico(MedicoDTO dto) {
        validarCrm(dto.getCrm());
        medicoRepository.save(toEntity(dto));
        return "Médico cadastrado com sucesso!";
    }

    public String atualizarMedico(Long id, MedicoDTO dto) {
        Medico medico = buscarMedico(id);
        if (!medico.getCrm().equals(dto.getCrm())) {
            validarCrm(dto.getCrm());
        }
        Medico atualizado = toEntity(dto);
        atualizado.setId(id);
        medicoRepository.save(atualizado);
        return "Médico atualizado com sucesso!";
    }

    public String deletarMedico(Long id) {
        Medico medico = buscarMedico(id);
        medicoRepository.delete(medico);
        return "Médico deletado com sucesso!";
    }

    public String adicionarPaciente(Long medicoId, Long pacienteId) {
        Medico medico = buscarMedico(medicoId);
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com ID " + pacienteId + " não encontrado."));
        medico.adicionarPaciente(paciente);
        medicoRepository.save(medico);
        return "Paciente adicionado ao médico com sucesso!";
    }

    private void validarCrm(String crm) {
        if (crm == null || crm.isEmpty()) {
            throw new IllegalArgumentException("O CRM é obrigatório.");
        }
        if (medicoRepository.findByCrm(crm) != null) {
            throw new IllegalArgumentException("Já existe um médico cadastrado com o CRM: " + crm);
        }
    }

    private Medico buscarMedico(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + id + " não encontrado."));
    }

    private MedicoDTO toDTO(Medico medico) {
        return new MedicoDTO(
                medico.getId(),
                medico.getNome(),
                medico.getEspecialidade(),
                medico.getNumeroConsultorio(),
                medico.getSalario(),
                medico.getCrm()
        );
    }

    private Medico toEntity(MedicoDTO dto) {
        Medico medico = new Medico();
        medico.setNome(dto.getNome());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setNumeroConsultorio(dto.getNumeroConsultorio());
        medico.setSalario(dto.getSalario());
        medico.setCrm(dto.getCrm());
        return medico;
    }

    private MedicoComConsultasDTO toDTOConsulta(Medico medico) {
        List<ConsultaDTO> consultas = medico.getConsultas().stream()
                .map(this::toConsultaDTO)
                .collect(Collectors.toList());

        List<PacienteDTO> pacientes = medico.getConsultas().stream()
                .map(Consulta::getPaciente)
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Paciente::getId, this::toPacienteDTO, (p1, p2) -> p1),
                        map -> new ArrayList<>(map.values())
                ));

        return new MedicoComConsultasDTO(
                medico.getId(),
                medico.getNome(),
                medico.getEspecialidade(),
                medico.getNumeroConsultorio(),
                medico.getSalario(),
                medico.getCrm(),
                consultas,
                pacientes
        );
    }

    private ConsultaDTO toConsultaDTO(Consulta consulta) {
        ConsultaDTO dto = new ConsultaDTO();
        dto.setId(consulta.getId());
        dto.setDataHora(consulta.getDataHora());
        dto.setValor(consulta.getValor());
        dto.setDataRetorno(consulta.getDataRetorno());
        if (consulta.getMedico() != null) dto.setMedicoId(consulta.getMedico().getId());
        if (consulta.getPaciente() != null) dto.setPacienteId(consulta.getPaciente().getId());
        return dto;
    }

    private PacienteDTO toPacienteDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setCpf(paciente.getCpf());
        dto.setDataNascimento(paciente.getDataNascimento());
        return dto;
    }
}