// ========================================
// HEALTHPET - JAVASCRIPT APPLICATION
// Sistema Veterinário Completo
// ========================================

// ========== CONFIGURAÇÕES ==========
const API_URL = 'http://localhost:8080/api/animais';
const API_PRONTUARIO = 'http://localhost:8080/api/prontuarios';
const API_VACINA = 'http://localhost:8080/api/vacinas';

let animaisCache = [];
let animalAtual = null;

// ========== INICIALIZAÇÃO ==========
document.addEventListener('DOMContentLoaded', () => {
    inicializarApp();
    inicializarNavegacao();
    carregarAnimais();
    carregarEstatisticas();
    carregarAlertasVacinas();
    configurarDataAtual();
});

function inicializarApp() {
    console.log('🐾 HealthPet iniciado!');
    criarModais();
}

// ========== NAVEGAÇÃO ==========
function inicializarNavegacao() {
    // Navegação do navbar
    document.querySelectorAll('.nav-link[data-section]').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = link.dataset.section;
            navegarPara(section);
            
            // Atualizar classe active
            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        });
    });

    // Botões de ação rápida
    document.querySelectorAll('.action-btn[data-section]').forEach(btn => {
        btn.addEventListener('click', () => {
            const section = btn.dataset.section;
            navegarPara(section);
            
            // Atualizar navbar
            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            document.querySelector(`.nav-link[data-section="${section}"]`)?.classList.add('active');
        });
    });

    // Dropdown items
    document.querySelectorAll('.dropdown-item[data-section]').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            navegarPara(section);
        });
    });
}

function navegarPara(section) {
    // Esconder todas as seções
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    
    // Mostrar seção selecionada
    const sectionElement = document.getElementById(section);
    if (sectionElement) {
        sectionElement.classList.add('active');
        window.scrollTo({ top: 0, behavior: 'smooth' });
        
        // Carregar dados específicos da seção
        if (section === 'animais') carregarAnimais();
        if (section === 'vacinas') carregarTodasVacinas();
    }
}

// ========== FORMULÁRIO DE CADASTRO ==========
function alterarFormulario() {
    const tipo = document.getElementById('tipoAnimal').value;
    const camposCachorro = document.getElementById('camposCachorro');
    const camposGato = document.getElementById('camposGato');
    const especie = document.getElementById('especie');
    
    camposCachorro.style.display = 'none';
    camposGato.style.display = 'none';
    
    if (tipo === 'cachorro') {
        camposCachorro.style.display = 'block';
        especie.value = 'Canina';
    } else if (tipo === 'gato') {
        camposGato.style.display = 'block';
        especie.value = 'Felina';
    } else {
        especie.value = '';
    }
}

async function cadastrarAnimal(event) {
    event.preventDefault();
    
    const tipo = document.getElementById('tipoAnimal').value;
    let endpoint = API_URL;
    
    const dados = {
        nome: document.getElementById('nome').value,
        especie: document.getElementById('especie').value,
        idade: parseFloat(document.getElementById('idade').value),
        nomeDono: document.getElementById('nomeDono').value,
        telefone: document.getElementById('telefone').value,
        raca: document.getElementById('raca').value || 'Não informado'
    };
    
    if (tipo === 'cachorro') {
        endpoint += '/cachorro';
        dados.porte = document.getElementById('porte').value;
    } else if (tipo === 'gato') {
        endpoint += '/gato';
        dados.pelagem = document.getElementById('pelagem').value;
        dados.temperamento = document.getElementById('temperamento').value;
    }
    
    try {
        mostrarLoading();
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });
        
        if (response.ok) {
            mostrarAlerta('✅ Animal cadastrado com sucesso!', 'success');
            document.getElementById('formCadastro').reset();
            alterarFormulario();
            carregarAnimais();
            carregarEstatisticas();
            
            // Navegar para lista
            setTimeout(() => navegarPara('animais'), 1500);
        } else {
            const erro = await response.json();
            mostrarAlerta('❌ ' + (erro.erro || 'Erro ao cadastrar'), 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão com o servidor', 'danger');
    } finally {
        esconderLoading();
    }
}

// ========== LISTAGEM DE ANIMAIS ==========
async function carregarAnimais() {
    try {
        mostrarLoading();
        const response = await fetch(API_URL);
        animaisCache = await response.json();
        exibirAnimais(animaisCache);
    } catch (error) {
        mostrarAlerta('❌ Erro ao carregar animais', 'danger');
    } finally {
        esconderLoading();
    }
}

function exibirAnimais(animais) {
    const container = document.getElementById('listaAnimais');
    
    if (animais.length === 0) {
        container.innerHTML = `
            <div class="col-12">
                <div class="empty-state">
                    <i class="bi bi-inbox"></i>
                    <h3>Nenhum animal cadastrado</h3>
                    <p>Clique em "Cadastrar" para adicionar o primeiro animal</p>
                    <button class="btn btn-primary btn-lg mt-3" data-section="cadastrar" onclick="navegarPara('cadastrar')">
                        <i class="bi bi-plus-circle me-2"></i>Cadastrar Primeiro Animal
                    </button>
                </div>
            </div>
        `;
        return;
    }
    
    container.innerHTML = animais.map(animal => criarCardAnimal(animal)).join('');
}

function criarCardAnimal(animal) {
    const tipo = animal.porte ? 'cachorro' : (animal.pelagem ? 'gato' : 'animal');
    const emoji = tipo === 'cachorro' ? '🐕' : (tipo === 'gato' ? '🐈' : '🐾');
    const corGradiente = tipo === 'cachorro' ? 'warning' : (tipo === 'gato' ? 'info' : 'secondary');
    
    return `
        <div class="col-md-6 col-lg-4">
            <div class="card animal-card shadow h-100">
                <div class="animal-header bg-gradient-${corGradiente}">
                    <div class="d-flex justify-content-between align-items-start">
                        <h4 class="mb-0">${emoji} ${animal.nome}</h4>
                        <span class="badge animal-badge badge-${tipo}">ID: ${animal.id}</span>
                    </div>
                </div>
                <div class="card-body">
                    <p class="mb-2"><strong><i class="bi bi-tag me-2"></i>Espécie:</strong> ${animal.especie}</p>
                    <p class="mb-2"><strong><i class="bi bi-award me-2"></i>Raça:</strong> ${animal.raca}</p>
                    <p class="mb-2"><strong><i class="bi bi-calendar me-2"></i>Idade:</strong> ${animal.idadeFormatada}</p>
                    <p class="mb-2"><strong><i class="bi bi-person me-2"></i>Dono:</strong> ${animal.nomeDono}</p>
                    <p class="mb-2"><strong><i class="bi bi-telephone me-2"></i>Telefone:</strong> ${animal.telefone}</p>
                    ${animal.porte ? `<p class="mb-2"><strong><i class="bi bi-rulers me-2"></i>Porte:</strong> ${animal.porte}</p>` : ''}
                    ${animal.pelagem ? `<p class="mb-2"><strong><i class="bi bi-brush me-2"></i>Pelagem:</strong> ${animal.pelagem}</p>` : ''}
                    ${animal.temperamento ? `<p class="mb-2"><strong><i class="bi bi-emoji-smile me-2"></i>Temperamento:</strong> ${animal.temperamento}</p>` : ''}
                </div>
                <div class="card-footer bg-transparent border-top-0 p-3">
                    <div class="d-grid gap-2">
                        <button onclick="abrirModalEditar(${animal.id})" class="btn btn-warning btn-sm">
                            <i class="bi bi-pencil-square me-1"></i>Editar
                        </button>
                        <div class="row g-2">
                            <div class="col-6">
                                <button onclick="abrirModalProntuario(${animal.id})" class="btn btn-info btn-sm w-100">
                                    <i class="bi bi-clipboard-heart me-1"></i>Prontuário
                                </button>
                            </div>
                            <div class="col-6">
                                <button onclick="abrirModalVacinasAnimal(${animal.id})" class="btn btn-success btn-sm w-100">
                                    <i class="bi bi-shield-check me-1"></i>Vacinas
                                </button>
                            </div>
                        </div>
                        <button onclick="removerAnimal(${animal.id})" class="btn btn-danger btn-sm">
                            <i class="bi bi-trash me-1"></i>Remover
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function filtrarAnimais() {
    const filtro = document.getElementById('filtroNome').value.toLowerCase();
    const filtrados = animaisCache.filter(a => 
        a.nome.toLowerCase().includes(filtro) || 
        a.id.toString().includes(filtro) ||
        a.nomeDono.toLowerCase().includes(filtro)
    );
    exibirAnimais(filtrados);
}

// ========== BUSCA ==========
async function buscarAnimal() {
    const nome = document.getElementById('buscaNome').value;
    const id = document.getElementById('buscaId').value;
    
    if (!nome && !id) {
        mostrarAlerta('❌ Digite um nome ou ID para buscar', 'warning');
        return;
    }
    
    try {
        mostrarLoading();
        let animais = [];
        
        if (id) {
            const response = await fetch(`${API_URL}/${id}`);
            if (response.ok) {
                const animal = await response.json();
                animais = [animal];
            }
        } else if (nome) {
            const response = await fetch(`${API_URL}/buscar?nome=${nome}`);
            animais = await response.json();
        }
        
        const container = document.getElementById('resultadoBusca');
        
        if (animais.length === 0) {
            container.innerHTML = `
                <div class="alert alert-info">
                    <h5><i class="bi bi-info-circle me-2"></i>Nenhum resultado encontrado</h5>
                    <p class="mb-0">Tente buscar por outro nome ou ID</p>
                </div>
            `;
        } else {
            container.innerHTML = '<div class="row g-4">' + 
                animais.map(animal => criarCardAnimal(animal)).join('') + 
                '</div>';
            mostrarAlerta(`✅ ${animais.length} animal(is) encontrado(s)!`, 'success');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro ao buscar', 'danger');
    } finally {
        esconderLoading();
    }
}

// ========== EDIÇÃO ==========
async function abrirModalEditar(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const animal = await response.json();
        
        // Preencher formulário do modal
        document.getElementById('editId').value = animal.id;
        document.getElementById('editNome').value = animal.nome;
        document.getElementById('editEspecie').value = animal.especie;
        document.getElementById('editIdade').value = animal.idade;
        document.getElementById('editRaca').value = animal.raca;
        document.getElementById('editNomeDono').value = animal.nomeDono;
        document.getElementById('editTelefone').value = animal.telefone;
        
        const camposCachorro = document.getElementById('editCamposCachorro');
        const camposGato = document.getElementById('editCamposGato');
        
        camposCachorro.style.display = 'none';
        camposGato.style.display = 'none';
        
        if (animal.porte) {
            camposCachorro.style.display = 'block';
            document.getElementById('editPorte').value = animal.porte;
        } else if (animal.pelagem) {
            camposGato.style.display = 'block';
            document.getElementById('editPelagem').value = animal.pelagem;
            document.getElementById('editTemperamento').value = animal.temperamento;
        }
        
        // Abrir modal usando Bootstrap
        const modal = new bootstrap.Modal(document.getElementById('modalEditar'));
        modal.show();
    } catch (error) {
        mostrarAlerta('❌ Erro ao carregar dados para edição', 'danger');
    }
}

async function salvarEdicao(event) {
    event.preventDefault();
    
    const id = document.getElementById('editId').value;
    const dados = {
        nome: document.getElementById('editNome').value,
        especie: document.getElementById('editEspecie').value,
        idade: parseFloat(document.getElementById('editIdade').value),
        nomeDono: document.getElementById('editNomeDono').value,
        telefone: document.getElementById('editTelefone').value,
        raca: document.getElementById('editRaca').value
    };
    
    const camposCachorro = document.getElementById('editCamposCachorro');
    const camposGato = document.getElementById('editCamposGato');
    
    if (camposCachorro.style.display !== 'none') {
        dados.porte = document.getElementById('editPorte').value;
    }
    
    if (camposGato.style.display !== 'none') {
        dados.pelagem = document.getElementById('editPelagem').value;
        dados.temperamento = document.getElementById('editTemperamento').value;
    }
    
    try {
        mostrarLoading();
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });
        
        if (response.ok) {
            mostrarAlerta('✅ Animal atualizado com sucesso!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalEditar')).hide();
            carregarAnimais();
            carregarEstatisticas();
        } else {
            mostrarAlerta('❌ Erro ao atualizar', 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão', 'danger');
    } finally {
        esconderLoading();
    }
}

// ========== REMOÇÃO ==========
async function removerAnimal(id) {
    const confirmacao = await mostrarConfirmacao(
        'Tem certeza?',
        'Esta ação não pode ser desfeita! O animal e todos os seus dados serão removidos permanentemente.',
        'Sim, remover',
        'Cancelar'
    );
    
    if (!confirmacao) return;
    
    try {
        mostrarLoading();
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        
        if (response.ok) {
            mostrarAlerta('✅ Animal removido com sucesso!', 'success');
            carregarAnimais();
            carregarEstatisticas();
        } else {
            mostrarAlerta('❌ Erro ao remover animal', 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão', 'danger');
    } finally {
        esconderLoading();
    }
}

// ========== CONTINUAÇÃO DO APP.JS ==========
// Cole esta parte no final do app.js (PARTE 1)

// ========== PRONTUÁRIO ==========
async function abrirModalProntuario(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        animalAtual = await response.json();
        
        document.getElementById('prontuarioAnimalId').value = id;
        document.getElementById('prontuarioNomeAnimal').textContent = `Animal: ${animalAtual.nome}`;
        
        const prontuarioResp = await fetch(`${API_PRONTUARIO}/animal/${id}`);
        const prontuario = await prontuarioResp.json();
        
        document.getElementById('prontuarioPeso').value = prontuario.peso || '';
        document.getElementById('prontuarioAltura').value = prontuario.altura || '';
        document.getElementById('prontuarioAlergias').value = prontuario.alergias || '';
        document.getElementById('prontuarioMedicamentos').value = prontuario.medicamentosEmUso || '';
        document.getElementById('prontuarioCondicoes').value = prontuario.condicoesPreExistentes || '';
        document.getElementById('prontuarioObservacoes').value = prontuario.observacoes || '';
        
        const modal = new bootstrap.Modal(document.getElementById('modalProntuario'));
        modal.show();
    } catch (error) {
        mostrarAlerta('❌ Erro ao carregar prontuário', 'danger');
    }
}

async function salvarProntuario(event) {
    event.preventDefault();
    
    const animalId = document.getElementById('prontuarioAnimalId').value;
    const dados = {
        peso: parseFloat(document.getElementById('prontuarioPeso').value) || null,
        altura: parseFloat(document.getElementById('prontuarioAltura').value) || null,
        alergias: document.getElementById('prontuarioAlergias').value,
        medicamentosEmUso: document.getElementById('prontuarioMedicamentos').value,
        condicoesPreExistentes: document.getElementById('prontuarioCondicoes').value,
        observacoes: document.getElementById('prontuarioObservacoes').value
    };
    
    try {
        mostrarLoading();
        const response = await fetch(`${API_PRONTUARIO}/animal/${animalId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });
        
        if (response.ok) {
            mostrarAlerta('✅ Prontuário salvo com sucesso!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalProntuario')).hide();
        } else {
            mostrarAlerta('❌ Erro ao salvar prontuário', 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão', 'danger');
    } finally {
        esconderLoading();
    }
}

// ========== VACINAS ==========
async function abrirModalVacinasAnimal(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        animalAtual = await response.json();
        
        document.getElementById('vacinasNomeAnimal').textContent = `Animal: ${animalAtual.nome}`;
        
        const vacinasResp = await fetch(`${API_VACINA}/animal/${id}`);
        const vacinas = await vacinasResp.json();
        
        const container = document.getElementById('listaVacinasAnimal');
        
        if (vacinas.length === 0) {
            container.innerHTML = `
                <div class="alert alert-info">
                    <i class="bi bi-info-circle me-2"></i>Nenhuma vacina registrada para este animal
                </div>
            `;
        } else {
            container.innerHTML = vacinas.map(v => criarCardVacina(v)).join('');
        }
        
        const modal = new bootstrap.Modal(document.getElementById('modalVacinasAnimal'));
        modal.show();
    } catch (error) {
        mostrarAlerta('❌ Erro ao carregar vacinas', 'danger');
    }
}

function criarCardVacina(v) {
    const statusClass = v.vencida ? 'vacina-vencida' : (v.proxima ? 'vacina-proxima' : '');
    const statusBadge = v.completa ? 'emdia' : (v.vencida ? 'vencida' : (v.proxima ? 'proxima' : 'emdia'));
    const statusIcon = v.vencida ? 'exclamation-triangle-fill' : (v.proxima ? 'clock-fill' : 'check-circle-fill');
    
    return `
        <div class="vacina-card ${statusClass}">
            <div class="d-flex justify-content-between align-items-start mb-2">
                <h5 class="mb-0"><i class="bi bi-shield-fill-check me-2"></i>${v.nome}</h5>
                <span class="badge badge-${statusBadge}">
                    <i class="bi bi-${statusIcon} me-1"></i>${v.status}
                </span>
            </div>
            <p class="mb-1"><strong>Aplicada em:</strong> ${formatarData(v.dataAplicacao)}</p>
            ${v.proximaDose ? `<p class="mb-1"><strong>Próxima dose:</strong> ${formatarData(v.proximaDose)}</p>` : ''}
            ${v.lote ? `<p class="mb-1"><strong>Lote:</strong> ${v.lote}</p>` : ''}
            ${v.veterinario ? `<p class="mb-1"><strong>Veterinário:</strong> ${v.veterinario}</p>` : ''}
            ${v.observacoes ? `<p class="mb-1"><strong>Observações:</strong> ${v.observacoes}</p>` : ''}
            <div class="alert alert-${v.vencida ? 'danger' : 'success'} mt-3 mb-0 py-2">
                <strong><i class="bi bi-${statusIcon} me-1"></i>${v.mensagemProximaDose}</strong>
            </div>
            <div class="mt-3 d-flex gap-2">
                ${!v.completa ? `<button onclick="marcarVacinaCompleta(${v.id})" class="btn btn-success btn-sm">
                    <i class="bi bi-check-circle me-1"></i>Marcar Completa
                </button>` : ''}
                <button onclick="deletarVacina(${v.id})" class="btn btn-danger btn-sm">
                    <i class="bi bi-trash me-1"></i>Remover
                </button>
            </div>
        </div>
    `;
}

function abrirModalNovaVacina() {
    document.getElementById('vacinaAnimalId').value = animalAtual.id;
    document.getElementById('formNovaVacina').reset();
    configurarDataAtual();
    
    const modal = new bootstrap.Modal(document.getElementById('modalNovaVacina'));
    modal.show();
}

async function registrarVacina(event) {
    event.preventDefault();
    
    const animalId = document.getElementById('vacinaAnimalId').value;
    const dados = {
        nome: document.getElementById('vacinaNome').value,
        dataAplicacao: document.getElementById('vacinaData').value,
        proximaDose: document.getElementById('vacinaProximaDose').value || null,
        lote: document.getElementById('vacinaLote').value,
        veterinario: document.getElementById('vacinaVeterinario').value,
        observacoes: document.getElementById('vacinaObservacoes').value,
        completa: document.getElementById('vacinaCompleta').checked
    };
    
    try {
        mostrarLoading();
        const response = await fetch(`${API_VACINA}/animal/${animalId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });
        
        if (response.ok) {
            mostrarAlerta('✅ Vacina registrada com sucesso!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalNovaVacina')).hide();
            abrirModalVacinasAnimal(animalId);
            carregarEstatisticas();
        } else {
            mostrarAlerta('❌ Erro ao registrar vacina', 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão', 'danger');
    } finally {
        esconderLoading();
    }
}

async function marcarVacinaCompleta(id) {
    try {
        mostrarLoading();
        const response = await fetch(`${API_VACINA}/${id}/completa`, { method: 'PUT' });
        
        if (response.ok) {
            mostrarAlerta('✅ Vacina marcada como completa!', 'success');
            abrirModalVacinasAnimal(animalAtual.id);
            carregarEstatisticas();
        } else {
            mostrarAlerta('❌ Erro ao atualizar vacina', 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão', 'danger');
    } finally {
        esconderLoading();
    }
}

async function deletarVacina(id) {
    const confirmacao = await mostrarConfirmacao(
        'Remover Vacina?',
        'Tem certeza que deseja remover este registro de vacinação?',
        'Sim, remover',
        'Cancelar'
    );
    
    if (!confirmacao) return;
    
    try {
        mostrarLoading();
        const response = await fetch(`${API_VACINA}/${id}`, { method: 'DELETE' });
        
        if (response.ok) {
            mostrarAlerta('✅ Vacina removida com sucesso!', 'success');
            abrirModalVacinasAnimal(animalAtual.id);
            carregarEstatisticas();
        } else {
            mostrarAlerta('❌ Erro ao remover vacina', 'danger');
        }
    } catch (error) {
        mostrarAlerta('❌ Erro de conexão', 'danger');
    } finally {
        esconderLoading();
    }
}

async function carregarTodasVacinas() {
    try {
        mostrarLoading();
        const [vencidasResp, proximasResp] = await Promise.all([
            fetch(`${API_VACINA}/vencidas`),
            fetch(`${API_VACINA}/proximas`)
        ]);
        
        const vencidas = await vencidasResp.json();
        const proximas = await proximasResp.json();
        
        const container = document.getElementById('listaTodasVacinas');
        const alertBox = document.getElementById('alertasVacinas');
        
        if (vencidas.length > 0 || proximas.length > 0) {
            alertBox.style.display = 'block';
            document.getElementById('mensagemAlertas').innerHTML = 
                `<strong>${vencidas.length}</strong> vacina(s) vencida(s) e <strong>${proximas.length}</strong> vacina(s) próxima(s) do vencimento.`;
        } else {
            alertBox.style.display = 'none';
        }
        
        let html = '';
        
        if (vencidas.length > 0) {
            html += '<h4 class="text-danger mb-3"><i class="bi bi-exclamation-triangle-fill me-2"></i>Vacinas Vencidas</h4>';
            html += '<div class="row g-3 mb-4">';
            html += vencidas.map(v => `<div class="col-md-6">${criarCardVacina(v)}</div>`).join('');
            html += '</div>';
        }
        
        if (proximas.length > 0) {
            html += '<h4 class="text-warning mb-3"><i class="bi bi-clock-fill me-2"></i>Vacinas Próximas</h4>';
            html += '<div class="row g-3 mb-4">';
            html += proximas.map(v => `<div class="col-md-6">${criarCardVacina(v)}</div>`).join('');
            html += '</div>';
        }
        
        if (html === '') {
            html = `
                <div class="alert alert-success text-center">
                    <i class="bi bi-check-circle-fill me-2 fs-1"></i>
                    <h4>Todas as vacinas estão em dia!</h4>
                    <p class="mb-0">Não há vacinas vencidas ou próximas do vencimento.</p>
                </div>
            `;
        }
        
        container.innerHTML = html;
    } catch (error) {
        mostrarAlerta('❌ Erro ao carregar vacinas', 'danger');
    } finally {
        esconderLoading();
    }
}

// ========== ESTATÍSTICAS ==========
async function carregarEstatisticas() {
    try {
        const [statsResp, vencidasResp, proximasResp] = await Promise.all([
            fetch(`${API_URL}/estatisticas`),
            fetch(`${API_VACINA}/vencidas`),
            fetch(`${API_VACINA}/proximas`)
        ]);
        
        const stats = await statsResp.json();
        const vencidas = await vencidasResp.json();
        const proximas = await proximasResp.json();
        
        document.getElementById('totalAnimais').textContent = stats.total;
        document.getElementById('totalCachorros').textContent = stats.cachorros;
        document.getElementById('totalGatos').textContent = stats.gatos;
        document.getElementById('vacinasVencidas').textContent = vencidas.length;
        
        // Animar números
        animarNumero('totalAnimais', 0, stats.total);
        animarNumero('totalCachorros', 0, stats.cachorros);
        animarNumero('totalGatos', 0, stats.gatos);
        animarNumero('vacinasVencidas', 0, vencidas.length);
    } catch (error) {
        console.error('Erro ao carregar estatísticas');
    }
}

async function carregarAlertasVacinas() {
    try {
        const response = await fetch(`${API_VACINA}/vencidas`);
        const vencidas = await response.json();
        
        if (vencidas.length > 0) {
            mostrarAlerta(`⚠️ Atenção: ${vencidas.length} vacina(s) vencida(s)!`, 'warning');
        }
    } catch (error) {
        console.error('Erro ao carregar alertas');
    }
}

// ========== UTILIDADES ==========
function formatarData(data) {
    if (!data) return '';
    const d = new Date(data + 'T00:00:00');
    return d.toLocaleDateString('pt-BR');
}

function configurarDataAtual() {
    const hoje = new Date().toISOString().split('T')[0];
    document.getElementById('vacinaData').value = hoje;
}

function animarNumero(elementId, inicio, fim, duracao = 1000) {
    const elemento = document.getElementById(elementId);
    const range = fim - inicio;
    const increment = fim > inicio ? 1 : -1;
    const stepTime = Math.abs(Math.floor(duracao / range));
    let current = inicio;
    
    const timer = setInterval(() => {
        current += increment;
        elemento.textContent = current;
        if (current === fim) {
            clearInterval(timer);
        }
    }, stepTime);
}

function mostrarAlerta(mensagem, tipo) {
    const container = document.getElementById('alertContainer');
    const alertClass = `alert-${tipo}`;
    
    const alertHTML = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${mensagem}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    container.innerHTML = alertHTML;
    
    setTimeout(() => {
        const alert = container.querySelector('.alert');
        if (alert) {
            bootstrap.Alert.getInstance(alert)?.close();
        }
    }, 5000);
}

function mostrarLoading() {
    // Pode adicionar um spinner se quiser
    document.body.style.cursor = 'wait';
}

function esconderLoading() {
    document.body.style.cursor = 'default';
}

function mostrarConfirmacao(titulo, mensagem, btnConfirmar, btnCancelar) {
    return new Promise((resolve) => {
        const resultado = confirm(`${titulo}\n\n${mensagem}`);
        resolve(resultado);
    });
}

// ========== CRIAÇÃO DE MODAIS ==========
function criarModais() {
    const modalsHTML = `
        <!-- Modal Editar -->
        <div class="modal fade" id="modalEditar" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-primary text-white">
                        <h5 class="modal-title"><i class="bi bi-pencil-square me-2"></i>Editar Animal</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="formEditar" onsubmit="salvarEdicao(event)">
                            <input type="hidden" id="editId">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Nome *</label>
                                    <input type="text" class="form-control" id="editNome" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Espécie *</label>
                                    <input type="text" class="form-control" id="editEspecie" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Idade (anos) *</label>
                                    <input type="number" class="form-control" id="editIdade" step="0.1" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Raça</label>
                                    <input type="text" class="form-control" id="editRaca">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Nome do Dono *</label>
                                    <input type="text" class="form-control" id="editNomeDono" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Telefone *</label>
                                    <input type="tel" class="form-control" id="editTelefone" required>
                                </div>
                            </div>
                            <div id="editCamposCachorro" style="display: none;" class="mt-3">
                                <label class="form-label">Porte</label>
                                <select class="form-select" id="editPorte">
                                    <option value="Pequeno">Pequeno</option>
                                    <option value="Médio">Médio</option>
                                    <option value="Grande">Grande</option>
                                </select>
                            </div>
                            <div id="editCamposGato" style="display: none;" class="mt-3">
                                <div class="row g-3">
                                    <div class="col-6">
                                        <label class="form-label">Pelagem</label>
                                        <select class="form-select" id="editPelagem">
                                            <option value="Curta">Curta</option>
                                            <option value="Média">Média</option>
                                            <option value="Longa">Longa</option>
                                        </select>
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label">Temperamento</label>
                                        <select class="form-select" id="editTemperamento">
                                            <option value="Calmo">Calmo</option>
                                            <option value="Ativo">Ativo</option>
                                            <option value="Agressivo">Agressivo</option>
                                            <option value="Tímido">Tímido</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="mt-4">
                                <button type="submit" class="btn btn-success w-100">
                                    <i class="bi bi-check-circle me-2"></i>Salvar Alterações
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Prontuário -->
        <div class="modal fade" id="modalProntuario" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-info text-white">
                        <h5 class="modal-title"><i class="bi bi-clipboard-heart me-2"></i>Prontuário Médico</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="info-box">
                            <strong id="prontuarioNomeAnimal"></strong>
                        </div>
                        <form id="formProntuario" onsubmit="salvarProntuario(event)">
                            <input type="hidden" id="prontuarioAnimalId">
                            <h6 class="fw-bold mb-3">📏 Medidas</h6>
                            <div class="row g-3 mb-4">
                                <div class="col-6">
                                    <label class="form-label">Peso (kg)</label>
                                    <input type="number" class="form-control" id="prontuarioPeso" step="0.1">
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Altura (cm)</label>
                                    <input type="number" class="form-control" id="prontuarioAltura" step="0.1">
                                </div>
                            </div>
                            <h6 class="fw-bold mb-3">🩺 Informações Médicas</h6>
                            <div class="mb-3">
                                <label class="form-label">Alergias</label>
                                <textarea class="form-control" id="prontuarioAlergias" rows="2"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Medicamentos</label>
                                <textarea class="form-control" id="prontuarioMedicamentos" rows="2"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Condições</label>
                                <textarea class="form-control" id="prontuarioCondicoes" rows="2"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Observações</label>
                                <textarea class="form-control" id="prontuarioObservacoes" rows="3"></textarea>
                            </div>
                            <button type="submit" class="btn btn-success w-100">
                                <i class="bi bi-save me-2"></i>Salvar Prontuário
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Vacinas Animal -->
        <div class="modal fade" id="modalVacinasAnimal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-success text-white">
                        <h5 class="modal-title"><i class="bi bi-shield-check me-2"></i>Vacinas</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="info-box">
                            <strong id="vacinasNomeAnimal"></strong>
                        </div>
                        <button onclick="abrirModalNovaVacina()" class="btn btn-primary w-100 mb-3">
                            <i class="bi bi-plus-circle me-2"></i>Adicionar Vacina
                        </button>
                        <div id="listaVacinasAnimal"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Nova Vacina -->
        <div class="modal fade" id="modalNovaVacina" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-gradient-success text-white">
                        <h5 class="modal-title"><i class="bi bi-shield-plus me-2"></i>Registrar Vacina</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="formNovaVacina" onsubmit="registrarVacina(event)">
                            <input type="hidden" id="vacinaAnimalId">
                            <div class="mb-3">
                                <label class="form-label">Nome da Vacina *</label>
                                <input type="text" class="form-control" id="vacinaNome" required>
                            </div>
                            <div class="row g-3 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Data Aplicação *</label>
                                    <input type="date" class="form-control" id="vacinaData" required>
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Próxima Dose</label>
                                    <input type="date" class="form-control" id="vacinaProximaDose">
                                </div>
                            </div>
                            <div class="row g-3 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Lote</label>
                                    <input type="text" class="form-control" id="vacinaLote">
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Veterinário</label>
                                    <input type="text" class="form-control" id="vacinaVeterinario">
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Observações</label>
                                <textarea class="form-control" id="vacinaObservacoes" rows="2"></textarea>
                            </div>
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="checkbox" id="vacinaCompleta">
                                <label class="form-check-label" for="vacinaCompleta">
                                    Vacinação completa
                                </label>
                            </div>
                            <button type="submit" class="btn btn-success w-100">
                                <i class="bi bi-check-circle me-2"></i>Registrar
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    document.getElementById('modalsContainer').innerHTML = modalsHTML;
}

console.log('🐾 HealthPet JavaScript carregado com sucesso!');