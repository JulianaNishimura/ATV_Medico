package com.example.ATV_Medico_Back.controller;

import com.example.ATV_Medico_Back.model.Consulta;
import com.example.ATV_Medico_Back.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // GET: Retorna a lista de todas as consultas
    @GetMapping
    public ResponseEntity<List<Consulta>> listarConsultas() {
        List<Consulta> consultas = consultaService.listarTodas();
        return ResponseEntity.ok(consultas);
    }

    // GET: Retorna a lista de consultas por médico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Consulta>> listarConsultasPorMedico(@PathVariable Long medicoId) {
        try {
            List<Consulta> consultas = consultaService.listarPorMedico(medicoId);
            return ResponseEntity.ok(consultas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // GET: Retorna a lista de consultas por paciente (histórico)
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Consulta>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        try {
            List<Consulta> consultas = consultaService.listarPorPaciente(pacienteId);
            return ResponseEntity.ok(consultas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // POST: Cria uma nova consulta
    @PostMapping
    public ResponseEntity<String> criarConsulta(@RequestBody Consulta consulta) {
        try {
            String resultado = consultaService.salvarConsulta(consulta);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Atualiza uma consulta existente
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConsulta(@PathVariable Long id, @RequestBody Consulta consulta) {
        try {
            String resultado = consultaService.atualizarConsulta(id, consulta);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Remove uma consulta
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarConsulta(@PathVariable Long id) {
        try {
            String resultado = consultaService.deletarConsulta(id);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}