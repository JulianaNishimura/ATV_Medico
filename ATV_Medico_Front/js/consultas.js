const baseUrl = 'http://localhost:8080/api';

let mapaMedicos = {};
let mapaPacientes = {};
let mapaConsultorios = {};

async function carregarMapas() {
  try {
    const [medicosResp, pacientesResp] = await Promise.all([
      fetch(`${baseUrl}/medicos`),
      fetch(`${baseUrl}/pacientes`),
    ]);

    const [medicos, pacientes] = await Promise.all([
      medicosResp.json(),
      pacientesResp.json(),
    ]);

    medicos.forEach((m) => {
      mapaMedicos[m.id] = m.nome;
      mapaConsultorios[m.id] = m.numeroConsultorio; // assumindo que médico tem campo "numeroConsultorio"
    });

    pacientes.forEach((p) => (mapaPacientes[p.id] = p.nome));
  } catch (error) {
    console.error('Erro ao carregar médicos ou pacientes:', error);
  }
}

async function carregarConsultas() {
  try {
    const response = await fetch(`${baseUrl}/consultas`);
    const consultas = await response.json();
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
          <button onclick="editarConsulta(${consulta.id}, ${consulta.medicoId}, ${consulta.pacienteId}, '${consulta.dataHora}', ${consulta.valor || 'null'}, '${consulta.dataRetorno || ''}')">✏️Editar</button>
          <button onclick="deletarConsulta(${consulta.id})">🗑️Excluir</button>
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
    const url = id ? `${baseUrl}/consultas/${id}` : `${baseUrl}/consultas`;
    const method = id ? 'PUT' : 'POST';

    const response = await fetch(url, {
      method: method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(consulta),
    });

    if (response.ok) {
      document.getElementById('mensagem').textContent = id ? 'Consulta atualizada!' : 'Consulta criada!';
      document.getElementById('consulta-form').reset();
      resetForm();
      carregarConsultas();
    } else {
      const erro = await response.text();
      throw new Error(erro);
    }
  } catch (error) {
    document.getElementById('mensagem').textContent = 'Erro: ' + error.message;
  }
});

function editarConsulta(id, medicoId, pacienteId, dataHora, valor, dataRetorno) {
  // Preenche os campos com os dados da tabela
  document.getElementById('consulta-id').value = id;
  document.getElementById('medico-id').value = medicoId;
  document.getElementById('paciente-id').value = pacienteId;
  document.getElementById('data-hora').value = dataHora.slice(0, 16);
  document.getElementById('valor').value = valor || '';
  document.getElementById('data-retorno').value = dataRetorno ? dataRetorno.slice(0, 16) : '';

  // Muda o botão para "Atualizar" e altera a cor
  const btnSalvar = document.getElementById('btn-salvar');
  btnSalvar.textContent = "Atualizar";
  btnSalvar.classList.add('btn-atualizar');

  // Mostra o botão "Cancelar"
  document.getElementById('btn-cancelar').style.display = 'inline-block';

  document.getElementById('mensagem').textContent = 'Editando consulta ID ' + id;
}

async function deletarConsulta(id) {
  if (!confirm('Deseja realmente excluir esta consulta?')) return;

  try {
    const response = await fetch(`${baseUrl}/consultas/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Erro ao excluir consulta');
    carregarConsultas();
    document.getElementById('mensagem').textContent = 'Consulta excluída com sucesso.';
  } catch (error) {
    document.getElementById('mensagem').textContent = 'Erro: ' + error.message;
  }
}

function resetForm() {
  const btnSalvar = document.getElementById('btn-salvar');
  btnSalvar.textContent = "Salvar Consulta";
  btnSalvar.classList.remove('btn-atualizar');
  document.getElementById('btn-cancelar').style.display = 'none';
  document.getElementById('consulta-id').value = '';
  document.getElementById('consulta-form').reset();
  document.getElementById('mensagem').textContent = '';
}

window.onload = async () => {
  await carregarMapas();
  carregarConsultas();
};