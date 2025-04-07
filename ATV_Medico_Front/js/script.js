const baseUrl = "http://localhost:8080/api";

// Função para carregar médicos
async function carregarMedicos() {
    try {
        console.log("Iniciando carregamento de médicos...");
        const response = await fetch(`${baseUrl}/medicos`);
        if (!response.ok) {
            throw new Error(`Erro na resposta da API: ${response.status}`);
        }
        const medicos = await response.json();
        console.log("Médicos carregados com sucesso:", medicos);
        const tbody = document.querySelector("#medicos-table tbody");
        tbody.innerHTML = "";
        medicos.forEach(medico => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${medico.id}</td>
                <td>${medico.nome}</td>
                <td>${medico.crm}</td>
                <td>${medico.especialidade || "N/A"}</td>
                <td>${medico.numeroConsultorio}</td>
            `;
            tbody.appendChild(tr);
        });
        console.log("Tabela de médicos preenchida com sucesso.");
    } catch (error) {
        console.error("Erro ao carregar médicos:", error.message);
    }
}

// Função para carregar pacientes
async function carregarPacientes() {
    try {
        console.log("Iniciando carregamento de pacientes...");
        const response = await fetch(`${baseUrl}/pacientes`);
        if (!response.ok) {
            throw new Error(`Erro na resposta da API: ${response.status}`);
        }
        const pacientes = await response.json();
        console.log("Pacientes carregados com sucesso:", pacientes);
        const tbody = document.querySelector("#pacientes-table tbody");
        tbody.innerHTML = "";
        pacientes.forEach(paciente => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${paciente.id}</td>
                <td>${paciente.nome}</td>
                <td>${paciente.cpf}</td>
                <td>${paciente.idade}</td>
                <td>${paciente.medico.nome} (ID: ${paciente.medico.id})</td>
            `;
            tbody.appendChild(tr);
        });
        console.log("Tabela de pacientes preenchida com sucesso.");
    } catch (error) {
        console.error("Erro ao carregar pacientes:", error.message);
    }
}

// Função para carregar consultas
async function carregarConsultas() {
    try {
        console.log("Iniciando carregamento de consultas...");
        const response = await fetch(`${baseUrl}/consultas`);
        if (!response.ok) {
            throw new Error(`Erro na resposta da API: ${response.status}`);
        }
        const consultas = await response.json();
        console.log("Consultas carregadas com sucesso:", consultas);
        const tbody = document.querySelector("#consultas-table tbody");
        tbody.innerHTML = "";
        consultas.forEach(consulta => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${consulta.id}</td>
                <td>${consulta.medico.nome} (ID: ${consulta.medico.id})</td>
                <td>${consulta.paciente.nome} (ID: ${consulta.paciente.id})</td>
                <td>${new Date(consulta.dataHora).toLocaleString()}</td>
                <td>${consulta.consultorio || "N/A"}</td>
                <td>${consulta.valor ? consulta.valor.toFixed(2) : "N/A"}</td>
                <td>${consulta.dataRetorno ? new Date(consulta.dataRetorno).toLocaleString() : "N/A"}</td>
            `;
            tbody.appendChild(tr);
        });
        console.log("Tabela de consultas preenchida com sucesso.");
    } catch (error) {
        console.error("Erro ao carregar consultas:", error.message);
    }
}

// Função para agendar uma nova consulta
document.getElementById("consulta-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    
    console.log("Iniciando envio do formulário de nova consulta...");
    const medicoId = document.getElementById("medico-id").value;
    const pacienteId = document.getElementById("paciente-id").value;
    const dataHora = document.getElementById("data-hora").value;
    const consultorio = document.getElementById("consultorio").value;
    const valor = document.getElementById("valor").value;
    const dataRetorno = document.getElementById("data-retorno").value;

    const consulta = {
        medico: { id: medicoId },
        paciente: { id: pacienteId },
        dataHora: new Date(dataHora).toISOString(),
        consultorio: consultorio || null,
        valor: valor ? parseFloat(valor) : null,
        dataRetorno: dataRetorno ? new Date(dataRetorno).toISOString() : null
    };

    console.log("Dados da consulta a serem enviados:", consulta);

    try {
        const response = await fetch(`${baseUrl}/consultas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(consulta)
        });
        const mensagem = await response.text();
        if (response.ok) {
            console.log("Consulta criada com sucesso:", mensagem);
            document.getElementById("mensagem").textContent = mensagem;
            carregarConsultas(); // Atualiza a tabela de consultas
            document.getElementById("consulta-form").reset(); // Limpa o formulário
        } else {
            throw new Error(`Erro na resposta da API: ${response.status} - ${mensagem}`);
        }
    } catch (error) {
        console.error("Erro ao criar consulta:", error.message);
        document.getElementById("mensagem").textContent = "Erro ao agendar consulta: " + error.message;
    }
});

// Carregar dados ao iniciar a página
window.onload = () => {
    console.log("Página carregada, iniciando carregamento dos dados...");
    carregarMedicos();
    carregarPacientes();
    carregarConsultas();
};