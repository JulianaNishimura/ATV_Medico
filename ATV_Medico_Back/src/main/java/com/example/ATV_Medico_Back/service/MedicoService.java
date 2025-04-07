package com.example.ATV_Medico_Back.service;

import com.example.ATV_Medico_Back.model.Consulta;
import com.example.ATV_Medico_Back.model.Medico;
import com.example.ATV_Medico_Back.model.Paciente;
import com.example.ATV_Medico_Back.repository.MedicoRepository;
import com.example.ATV_Medico_Back.repository.PacienteRepository;
import com.example.ATV_Medico_Back.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    // Lista todos os médicos
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    // Salva um novo médico com validação de CRM
    public String salvarMedico(Medico medico) {
        if (medico.getCrm() == null || medico.getCrm().isEmpty()) {
            throw new IllegalArgumentException("O CRM é obrigatório.");
        }
        if (medicoRepository.findByCrm(medico.getCrm()) != null) {
            throw new IllegalArgumentException("Já existe um médico cadastrado com o CRM: " + medico.getCrm());
        }
        medicoRepository.save(medico);
        return "Médico cadastrado com sucesso!";
    }

    // Atualiza um médico existente
    public String atualizarMedico(Long id, Medico medicoAtualizado) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + id + " não encontrado."));

        // Verifica se o CRM foi alterado e se já existe
        if (!medico.getCrm().equals(medicoAtualizado.getCrm())) {
            if (medicoRepository.findByCrm(medicoAtualizado.getCrm()) != null) {
                throw new IllegalArgumentException("O CRM " + medicoAtualizado.getCrm() + " já está em uso por outro médico.");
            }
        }

        medico.setNome(medicoAtualizado.getNome());
        medico.setEspecialidade(medicoAtualizado.getEspecialidade());
        medico.setNumeroConsultorio(medicoAtualizado.getNumeroConsultorio());
        medico.setValorConsulta(medicoAtualizado.getValorConsulta());
        medico.setSalario(medicoAtualizado.getSalario());
        medico.setCrm(medicoAtualizado.getCrm());

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

    // Adiciona uma consulta ao médico
    public String adicionarConsulta(Long medicoId, Consulta consulta) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico com ID " + medicoId + " não encontrado."));

        if (consulta.getPaciente() == null || !pacienteRepository.existsById(consulta.getPaciente().getId())) {
            throw new IllegalArgumentException("Paciente inválido ou não encontrado.");
        }

        medico.adicionarConsulta(consulta);
        medicoRepository.save(medico);
        return "Consulta adicionada ao médico com sucesso!";
    }
}