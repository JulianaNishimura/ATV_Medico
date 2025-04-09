package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.dto.MedicoDTO;
import com.example.ATV_Medico_Back.entity.Medico;
import com.example.ATV_Medico_Back.entity.Paciente;
import com.example.ATV_Medico_Back.repository.MedicoRepository;
import com.example.ATV_Medico_Back.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Lista todos os médicos como DTOs
    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAll().stream().map(medico -> {
            MedicoDTO dto = new MedicoDTO();
            dto.setId(medico.getId());
            dto.setNome(medico.getNome());
            dto.setEspecialidade(medico.getEspecialidade());
            dto.setNumeroConsultorio(medico.getNumeroConsultorio());
            dto.setSalario(medico.getSalario());
            dto.setCrm(medico.getCrm());
            return dto;
        }).collect(Collectors.toList());
    }

    // Salva um novo médico com validação de CRM
    public String salvarMedico(MedicoDTO dto) {
        if (dto.getCrm() == null || dto.getCrm().isEmpty()) {
            throw new IllegalArgumentException("O CRM é obrigatório.");
        }
        if (medicoRepository.findByCrm(dto.getCrm()) != null) {
            throw new IllegalArgumentException("Já existe um médico cadastrado com o CRM: " + dto.getCrm());
        }

        Medico medico = new Medico();
        medico.setNome(dto.getNome());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setNumeroConsultorio(dto.getNumeroConsultorio());
        medico.setSalario(dto.getSalario());
        medico.setCrm(dto.getCrm());

        medicoRepository.save(medico);
        return "Médico cadastrado com sucesso!";
    }

    // Atualiza um médico existente
    public String atualizarMedico(Long id, MedicoDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + id + " não encontrado."));

        if (!medico.getCrm().equals(dto.getCrm())) {
            if (medicoRepository.findByCrm(dto.getCrm()) != null) {
                throw new IllegalArgumentException("O CRM " + dto.getCrm() + " já está em uso por outro médico.");
            }
        }

        medico.setNome(dto.getNome());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setNumeroConsultorio(dto.getNumeroConsultorio());
        medico.setSalario(dto.getSalario());
        medico.setCrm(dto.getCrm());

        medicoRepository.save(medico);
        return "Médico atualizado com sucesso!";
    }

    // Deleta um médico
    public String deletarMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + id + " não encontrado."));
        medicoRepository.delete(medico);
        return "Médico deletado com sucesso!";
    }

    // Adiciona um paciente ao médico
    public String adicionarPaciente(Long medicoId, Long pacienteId) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + medicoId + " não encontrado."));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com ID " + pacienteId + " não encontrado."));

        medico.adicionarPaciente(paciente);
        medicoRepository.save(medico);
        return "Paciente adicionado ao médico com sucesso!";
    }
}
