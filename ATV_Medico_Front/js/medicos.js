const baseUrl = "http://localhost:8080/api";

async function carregarMedicos() {
    try {
        const response = await fetch(`${baseUrl}/medicos`);
        if (!response.ok) throw new Error("Erro ao buscar médicos.");
        const medicos = await response.json();
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
    } catch (error) {
        console.error("Erro ao carregar médicos:", error.message);
    }
}

window.onload = carregarMedicos;
