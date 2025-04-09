const baseUrl = 'http://localhost:8080/api';

let mapaMedicos = {};
let mapaPacientes = {};
let mapaConsultorios = {};

async function carregarMapas() {
  try {
    const [medicosResp, pacientesResp] = await Promise.all([
      axios.get(`${baseUrl}/medicos`),
      axios.get(`${baseUrl}/pacientes`)
    ]);

    const medicos = medicosResp.data;
    const pacientes = pacientesResp.data;

    medicos.forEach((m) => {
      mapaMedicos[m.id] = m.nome;
      mapaConsultorios[m.id] = m.numeroConsultorio; // assumindo esse campo
    });

    pacientes.forEach((p) => (mapaPacientes[p.id] = p.nome));
  } catch (error) {
    console.error('Erro ao carregar médicos ou pacientes:', error);
  }
}

async function carregarConsultas() {
  try {
    const response = await axios.get(`${baseUrl}/consultas`);
    const consultas = response.data;
    const tbody = document.querySelector('#consultas-table tbody');
    tbody.innerHTML = '';

    consultas.forEach((consulta) => {
      const consultorio = mapaConsultorios[consulta.medicoId] || 'Desconhecido';
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${consulta.id}</td>
        <td>${mapaMedicos[consulta.medicoId] || 'Desconhecido'} (ID: ${consulta.medicoId})</td>
        <td>${mapaPacientes[consulta.pacienteId] || 'Desconhecido'} (ID: ${consulta.pacienteId})</td>
        <td>${new Date(consulta.dataHora).toLocaleString()}</td>
        <td>${consultorio}</td>
        <td>${consulta.valor ? `R$ ${consulta.valor.toFixed(2)}` : 'N/A'}</td>
        <td>${consulta.dataRetorno ? new Date(consulta.dataRetorno).toLocaleString() : 'N/A'}</td>
        <td>
          <button onclick="editarConsulta(${consulta.id})">Editar</button>
          <button onclick="deletarConsulta(${consulta.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (error) {
    console.error('Erro ao carregar consultas:', error);
  }
}

document.getElementById('consulta-form').addEventListener('submit', async (e) => {
  e.preventDefault();

  const id = document.getElementById('consulta-id').value;
  const medicoId = document.getElementById('medico-id').value;
  const pacienteId = document.getElementById('paciente-id').value;
  const dataHora = document.getElementById('data-hora').value;
  const valor = document.getElementById('valor').value;
  const dataRetorno = document.getElementById('data-retorno').value;

  const consulta = {
    medicoId: parseInt(medicoId),
    pacienteId: parseInt(pacienteId),
    dataHora: new Date(dataHora).toISOString(),
    valor: valor ? parseFloat(valor) : null,
    dataRetorno: dataRetorno ? new Date(dataRetorno).toISOString() : null,
  };

  try {
    if (id) {
      await axios.put(`${baseUrl}/consultas/${id}`, consulta);
      document.getElementById('mensagem').textContent = 'Consulta atualizada!';
    } else {
      await axios.post(`${baseUrl}/consultas`, consulta);
      document.getElementById('mensagem').textContent = 'Consulta criada!';
    }

    document.getElementById('consulta-form').reset();
    document.getElementById('consulta-id').value = '';
    carregarConsultas();
  } catch (error) {
    document.getElementById('mensagem').textContent = 'Erro: ' + (error.response?.data || error.message);
  }
});

async function editarConsulta(id) {
  try {
    const response = await axios.get(`${baseUrl}/consultas/${id}`);
    const consulta = response.data;

    document.getElementById('consulta-id').value = consulta.id;
    document.getElementById('medico-id').value = consulta.medico.id;
    document.getElementById('paciente-id').value = consulta.paciente.id;
    document.getElementById('data-hora').value = consulta.dataHora.slice(0, 16);
    document.getElementById('valor').value = consulta.valor || '';
    document.getElementById('data-retorno').value = consulta.dataRetorno
      ? consulta.dataRetorno.slice(0, 16)
      : '';

    document.getElementById('mensagem').textContent = 'Editando consulta ID ' + id;
  } catch (error) {
    document.getElementById('mensagem').textContent = 'Erro ao carregar consulta: ' + (error.response?.data || error.message);
  }
}

async function deletarConsulta(id) {
  if (!confirm('Deseja realmente excluir esta consulta?')) return;

  try {
    await axios.delete(`${baseUrl}/consultas/${id}`);
    carregarConsultas();
    document.getElementById('mensagem').textContent = 'Consulta excluída com sucesso.';
  } catch (error) {
    document.getElementById('mensagem').textContent = 'Erro: ' + (error.response?.data || error.message);
  }
}

window.onload = async () => {
  await carregarMapas();
  carregarConsultas();
};
