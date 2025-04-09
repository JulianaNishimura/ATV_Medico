const baseUrl = "http://localhost:8080/api/medicos";

// carregar todos os médicos e preencher a tabela
async function carregarMedicos() {
    try {
        const response = await axios.get(baseUrl);
        const medicos = response.data;
        const tbody = document.querySelector("#medicos-table tbody");
        tbody.innerHTML = "";

        medicos.forEach(medico => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${medico.id}</td>
                <td>${medico.nome}</td>
                <td>${medico.crm}</td>
                <td>${medico.especialidade}</td>
                <td>${medico.numeroConsultorio}</td>
                <td>
                    <button onclick="editarMedico(${medico.id})">✏️</button>
                    <button onclick="excluirMedico(${medico.id})">🗑️</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        alert("Erro ao carregar médicos: " + err.message);
    }
}

// adicionar um novo médico (POST)
async function adicionarMedico(e) {
    e.preventDefault();
    const medico = {
        nome: document.getElementById("nome").value,
        crm: document.getElementById("crm").value,
        especialidade: document.getElementById("especialidade").value,
        numeroConsultorio: document.getElementById("consultorio").value
    };

    try {
        await axios.post(baseUrl, medico);
        alert("Médico adicionado!");
        carregarMedicos();
        document.getElementById("form-medico").reset();
    } catch (err) {
        alert("Erro ao adicionar médico: " + err.message);
    }
}

// buscar dados do médico e preencher o formulário para edição
async function editarMedico(id) {
    try {
        const response = await axios.get(`${baseUrl}/${id}`);
        const medico = response.data;

        document.getElementById("medico-id").value = medico.id;
        document.getElementById("nome").value = medico.nome;
        document.getElementById("crm").value = medico.crm;
        document.getElementById("especialidade").value = medico.especialidade;
        document.getElementById("consultorio").value = medico.numeroConsultorio;

        document.getElementById("btn-salvar").textContent = "Atualizar";
    } catch (err) {
        alert("Erro ao carregar médico: " + err.message);
    }
}

// atualizar ou adicionar médico, dependendo se tem ID
async function atualizarMedico(e) {
    e.preventDefault();

    const id = document.getElementById("medico-id").value;
    const medico = {
        nome: document.getElementById("nome").value,
        crm: document.getElementById("crm").value,
        especialidade: document.getElementById("especialidade").value,
        numeroConsultorio: document.getElementById("consultorio").value
    };

    if (!id) return adicionarMedico(e);

    try {
        await axios.put(`${baseUrl}/${id}`, medico);
        alert("Médico atualizado!");
        carregarMedicos();
        document.getElementById("form-medico").reset();
        document.getElementById("btn-salvar").textContent = "Salvar";
    } catch (err) {
        alert("Erro ao atualizar médico: " + err.message);
    }
}

// excluir médico
async function excluirMedico(id) {
    if (!confirm("Deseja realmente excluir este médico?")) return;
    try {
        await axios.delete(`${baseUrl}/${id}`);
        alert("Médico excluído.");
        carregarMedicos();
    } catch (err) {
        alert("Erro ao excluir médico: " + err.message);
    }
}

document.getElementById("form-medico").addEventListener("submit", atualizarMedico);
window.onload = carregarMedicos;
