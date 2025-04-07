const baseUrl = "http://localhost:8080/api";

async function carregarConsultas() {
    try {
        const response = await fetch(`${baseUrl}/consultas`);
        if (!response.ok) throw new Error("Erro ao buscar consultas.");
        const consultas = await response.json();
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
    } catch (error) {
        console.error("Erro ao carregar consultas:", error.message);
    }
}

document.getElementById("consulta-form").addEventListener("submit", async (e) => {
    e.preventDefault();
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

    try {
        const response = await fetch(`${baseUrl}/consultas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(consulta)
        });
        const mensagem = await response.text();
        if (response.ok) {
            document.getElementById("mensagem").textContent = mensagem;
            carregarConsultas();
            document.getElementById("consulta-form").reset();
        } else {
            throw new Error(mensagem);
        }
    } catch (error) {
        document.getElementById("mensagem").textContent = "Erro: " + error.message;
    }
});

window.onload = carregarConsultas;
