package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.model.Paciente;
import com.example.ATV_Medico_Back.repository.PacienteRepository;
import com.example.ATV_Medico_Back.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // Lista todos os pacientes
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    // Salva um novo paciente com validações
    public String salvarPaciente(Paciente paciente) {
        // Verifica se o CPF é válido
        if (paciente.getCpf() == null || paciente.getCpf().length() != 11 || !paciente.getCpf().matches("\\d+")) {
            throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos numéricos.");
        }
        if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe um paciente cadastrado com o CPF: " + paciente.getCpf());
        }

        // Verifica se o médico existe
        if (paciente.getMedico() == null || !medicoRepository.existsById(paciente.getMedico().getId())) {
            throw new IllegalArgumentException("Médico inválido ou não encontrado.");
        }

        // Verifica se a data de nascimento é válida
        if (paciente.getDataNascimento() == null || paciente.getDataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento deve ser válida e anterior à data atual.");
        }



        pacienteRepository.save(paciente);
        return "Paciente cadastrado com sucesso!";
    }

    // Atualiza um paciente existente
    public String atualizarPaciente(Long id, Paciente pacienteAtualizado) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com ID " + id + " não encontrado."));

        // Verifica se o CPF foi alterado e se já existe
        if (pacienteAtualizado.getCpf() != null && !paciente.getCpf().equals(pacienteAtualizado.getCpf())) {
            if (pacienteRepository.findByCpf(pacienteAtualizado.getCpf()).isPresent()) {
                throw new IllegalArgumentException("O CPF " + pacienteAtualizado.getCpf() + " já está em uso por outro paciente.");
            }
            if (pacienteAtualizado.getCpf().length() != 11 || !pacienteAtualizado.getCpf().matches("\\d+")) {
                throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos numéricos.");
            }
            paciente.setCpf(pacienteAtualizado.getCpf());
        }

        // Verifica se o médico foi alterado e existe
        if (pacienteAtualizado.getMedico() != null && !medicoRepository.existsById(pacienteAtualizado.getMedico().getId())) {
            throw new IllegalArgumentException("Médico inválido ou não encontrado.");
        }

        // Atualiza os campos
        if (pacienteAtualizado.getNome() != null) paciente.setNome(pacienteAtualizado.getNome());
        if (pacienteAtualizado.getDataNascimento() != null) {
            if (pacienteAtualizado.getDataNascimento().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("A data de nascimento deve ser anterior à data atual.");
            }
            paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        }
        if (pacienteAtualizado.getMedico() != null) paciente.setMedico(pacienteAtualizado.getMedico());

        pacienteRepository.save(paciente);
        return "Paciente atualizado com sucesso!";
    }

    // Deleta um paciente
    public String deletarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente com ID " + id + " não encontrado."));
        pacienteRepository.delete(paciente);
        return "Paciente deletado com sucesso!";
    }
}