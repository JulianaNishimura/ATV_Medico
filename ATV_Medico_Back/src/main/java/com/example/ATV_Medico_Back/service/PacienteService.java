package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.dto.ConsultaDTO;
import com.example.ATV_Medico_Back.dto.PacienteComConsultasDTO;
import com.example.ATV_Medico_Back.dto.PacienteDTO;
import com.example.ATV_Medico_Back.entity.Paciente;
import com.example.ATV_Medico_Back.repository.MedicoRepository;
import com.example.ATV_Medico_Back.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    public List<PacienteComConsultasDTO> listarPacientesComConsultas() {
        return pacienteRepository.findAll().stream()
                .map(this::toPacienteComConsultasDTO)
                .collect(Collectors.toList());
    }

    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::toPacienteDTO)
                .collect(Collectors.toList());
    }

    public PacienteComConsultasDTO buscarPorCpf(String cpf) {
        Paciente paciente = pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com CPF " + cpf + " não encontrado."));
        return toPacienteComConsultasDTO(paciente);
    }

    public PacienteComConsultasDTO buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com id " + id + " não encontrado."));
        return toPacienteComConsultasDTO(paciente);
    }

    public String salvarPaciente(PacienteDTO dto) {
        validarPaciente(dto, true);
        pacienteRepository.save(toEntity(dto));
        return "Paciente cadastrado com sucesso!";
    }

    public String atualizarPaciente(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com ID " + id + " não encontrado."));

        validarPaciente(dto, false);

        if (dto.getCpf() != null && !paciente.getCpf().equals(dto.getCpf())) {
            paciente.setCpf(dto.getCpf());
        }
        if (dto.getNome() != null) paciente.setNome(dto.getNome());
        if (dto.getDataNascimento() != null) paciente.setDataNascimento(dto.getDataNascimento());

        pacienteRepository.save(paciente);
        return "Paciente atualizado com sucesso!";
    }

    public String deletarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com ID " + id + " não encontrado."));
        pacienteRepository.delete(paciente);
        return "Paciente deletado com sucesso!";
    }

    private void validarPaciente(PacienteDTO dto, boolean novo) {
        if (dto.getCpf() == null || !dto.getCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos numéricos.");
        }
        if (novo && pacienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe um paciente cadastrado com o CPF: " + dto.getCpf());
        }
        if (dto.getDataNascimento() == null || dto.getDataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento deve ser válida e anterior à data atual.");
        }
    }

    private PacienteDTO toPacienteDTO(Paciente paciente) {
        return new PacienteDTO(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getDataNascimento());
    }

    private PacienteComConsultasDTO toPacienteComConsultasDTO(Paciente paciente) {
        PacienteComConsultasDTO dto = new PacienteComConsultasDTO();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setCpf(paciente.getCpf());
        dto.setDataNascimento(paciente.getDataNascimento());

        dto.setConsultas(
                paciente.getConsultas().stream().map(consulta -> {
                    ConsultaDTO c = new ConsultaDTO();
                    c.setId(consulta.getId());
                    c.setDataHora(consulta.getDataHora());
                    c.setDataRetorno(consulta.getDataRetorno());
                    c.setMedicoId(consulta.getMedico() != null ? consulta.getMedico().getId() : null);
                    c.setPacienteId(consulta.getPaciente().getId());
                    c.setValor(consulta.getValor());
                    return c;
                }).collect(Collectors.toList())
        );

        return dto;
    }

    private Paciente toEntity(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setId(dto.getId());
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        return paciente;
    }
}