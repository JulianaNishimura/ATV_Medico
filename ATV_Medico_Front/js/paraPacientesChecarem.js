const pacientesUrl = "http://localhost:8080/api/pacientes/cpf";
const medicosUrl = "http://localhost:8080/api/medicos";

async function consultarDadosPaciente(e) {
    e.preventDefault();
    const cpf = document.getElementById("paciente-cpf").value.trim();
    const mensagem = document.getElementById("mensagem");

    try {
        // Busca o paciente pelo CPF
        const pacienteResponse = await fetch(`${pacientesUrl}/${cpf}`);
        if (!pacienteResponse.ok) {
            const errorText = await pacienteResponse.text();
            throw new Error(errorText || "CPF não encontrado ou erro na consulta.");
        }
        const paciente = await pacienteResponse.json();

        // Busca todos os médicos
        const medicosResponse = await fetch(medicosUrl);
        if (!medicosResponse.ok) throw new Error("Erro ao buscar dados dos médicos.");
        const medicos = await medicosResponse.json();

        mensagem.textContent = "Dados carregados com sucesso!";
        mensagem.style.color = "#0066cc";

        // Carrega as consultas do paciente
        carregarConsultas(paciente.consultas || [], medicos);

        // Filtra os médicos das consultas do paciente
        const medicosDoPaciente = medicos.filter(medico =>
            paciente.consultas.some(consulta => consulta.medicoId === medico.id)
        );
        carregarMedicos(medicosDoPaciente);
    } catch (err) {
        mensagem.textContent = err.message;
        mensagem.style.color = "red";
        document.getElementById("consultas-section").style.display = "none";
        document.getElementById("medicos-section").style.display = "none";
    }
}

function carregarConsultas(consultas, medicos) {
    const tbody = document.querySelector("#consultas-table tbody");
    tbody.innerHTML = "";
    const section = document.getElementById("consultas-section");

    if (!consultas || consultas.length === 0) {
        tbody.innerHTML = "<tr><td colspan='5'>Nenhuma consulta encontrada.</td></tr>";
    } else {
        consultas.forEach(consulta => {
            const medico = medicos.find(m => m.id === consulta.medicoId);
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${new Date(consulta.dataHora).toLocaleString("pt-BR")}</td>
                <td>${medico ? medico.nome : 'Desconhecido'}</td>
                <td>${medico ? medico.numeroConsultorio || '-' : '-'}</td>
                <td>${consulta.valor ? consulta.valor.toFixed(2) : '0.00'}</td>
                <td>${consulta.dataRetorno ? new Date(consulta.dataRetorno).toLocaleDateString("pt-BR") : 'Nenhum'}</td>
            `;
            tbody.appendChild(tr);
        });
    }
    section.style.display = "block";
}

function carregarMedicos(medicos) {
    const tbody = document.querySelector("#medicos-table tbody");
    tbody.innerHTML = "";
    const section = document.getElementById("medicos-section");

    if (!medicos || medicos.length === 0) {
        tbody.innerHTML = "<tr><td colspan='4'>Nenhum médico encontrado.</td></tr>";
    } else {
        medicos.forEach(medico => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${medico.nome}</td>
                <td>${medico.crm}</td>
                <td>${medico.especialidade || '-'}</td>
                <td>${medico.numeroConsultorio || '-'}</td>
            `;
            tbody.appendChild(tr);
        });
    }
    section.style.display = "block";
}

document.getElementById("paciente-login-form").addEventListener("submit", consultarDadosPaciente);