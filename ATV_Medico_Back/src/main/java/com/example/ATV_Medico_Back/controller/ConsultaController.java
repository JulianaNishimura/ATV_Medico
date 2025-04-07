package com.example.ATV_Medico_Back.controller;

import com.example.ATV_Medico_Back.dto.ConsultaDTO;
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

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarConsultas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaDTO>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        try {
            return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> criarConsulta(@RequestBody ConsultaDTO dto) {
        try {
            String resultado = consultaService.salvarConsulta(dto);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConsulta(@PathVariable Long id, @RequestBody ConsultaDTO dto) {
        try {
            String resultado = consultaService.atualizarConsulta(id, dto);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
