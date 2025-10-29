// --- CONFIGURAÇÃO ---
const API_BASE_URL = '/produtos';

// --- ELEMENTOS DA PÁGINA ---
const resultadosDiv = document.getElementById('resultados');
const inputNome = document.getElementById('buscaNome');
const inputCodigoEan = document.getElementById('buscaCodigoEan');
const inputCodigo = document.getElementById('buscaCodigo');
const inputRef = document.getElementById('buscaRef');
const btnBuscaNome = document.getElementById('btnBuscaNome');
const btnBuscaCodigoEan = document.getElementById('btnBuscaCodigoEan');
const btnBuscaCodigo = document.getElementById('btnBuscaCodigo');
const btnBuscaRef = document.getElementById('btnBuscaRef');

// --- FUNÇÕES DE LÓGICA ---

// Exibir feedback (loading, erro)
const mostrarFeedback = (mensagem) => {
    resultadosDiv.innerHTML = `
        <div class="text-center text-gray-500 py-10 bg-white rounded-lg shadow-md">
            <p>${mensagem}</p>
        </div>`;
};

// Spinner de carregamento
const mostrarLoader = () => {
    resultadosDiv.innerHTML = `
        <div class="flex justify-center items-center py-10 bg-white rounded-lg shadow-md">
            <div class="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-24 w-24"></div>
        </div>`;
}

// Função base para fazer a busca na API
const fetchData = async (endpoint) => {
    mostrarLoader();
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        if (response.status === 404) {
            return { success: true, data: [] };
        }
        if (!response.ok) {
            throw new Error(`Erro de rede: ${response.statusText}`);
        }
        const data = await response.json();
        return { success: true, data: Array.isArray(data) ? data : [data] };
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        return { success: false, message: 'Não foi possível conectar à API. Verifique se o backend está no ar.' };
    }
};

// --- FUNÇÕES DE BUSCA ---

const buscarPorNome = async () => {
    const valor = inputNome.value.trim();
    if (!valor) { mostrarFeedback('Por favor, digite um nome para buscar.'); return; }
    const resultado = await fetchData(`/buscanome?nome=${encodeURIComponent(valor)}`);
    if (resultado.success) exibirResultados(resultado.data);
    else mostrarFeedback(resultado.message);
};

const buscarPorCodigoEan = async () => {
    const valor = inputCodigoEan.value.trim();
    if (!valor) { mostrarFeedback('Por favor, digite um código para buscar.'); return; }
    // Regex para verificar se o código é um código de barras (12 ou 13 dígitos)
    let endpoint = '';
    if(/^\d{12,13}$/.test(valor)) {
        endpoint = `/buscaean?codigoBarras=${valor}`
    } else {
        mostrarFeedback('Por favor, digite um código de barras válido (12 ou 13 dígitos).');
        return;
    }
    const resultado = await fetchData(endpoint);
    if (resultado.success) exibirResultados(resultado.data);
    else mostrarFeedback(resultado.message);
};

const buscarPorCodigo = async () => {
    const valor = inputCodigo.value.trim();
    if (!valor) { mostrarFeedback('Por favor, digite um código para buscar.'); return; }
    const endpoint = `/buscacod?codigo=${encodeURIComponent(valor)}`;
    const resultado = await fetchData(endpoint);
    if (resultado.success) exibirResultados(resultado.data);
    else mostrarFeedback(resultado.message);
};

const buscarPorReferencia = async () => {
    const valor = inputRef.value.trim();
    if (!valor) { mostrarFeedback('Por favor, digite uma referência para buscar.'); return; }
    const resultado = await fetchData(`/buscaref?referencia=${encodeURIComponent(valor)}`);
    if (resultado.success) exibirResultados(resultado.data);
    else mostrarFeedback(resultado.message);
};

// --- FUNÇÃO PARA RENDERIZAR OS RESULTADOS ---

const exibirResultados = (produtos) => {
    if (!produtos || produtos.length === 0) {
        mostrarFeedback('Nenhum produto encontrado para sua busca.');
        return;
    }

    resultadosDiv.innerHTML = ''; // Limpa a área de resultados
    const lista = document.createElement('div');
    lista.className = 'space-y-4';

    const formatter = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

    produtos.forEach(produto => {
        const card = document.createElement('div');
        card.className = 'bg-white p-5 rounded-lg shadow-sm border border-gray-200';
        card.innerHTML = `
            <div class="flex justify-between items-start">
                <div class="flex-1">
                    <h3 class="text-lg font-bold text-gray-900">${produto.nome || 'Nome não disponível'}</h3>
                    <p class="text-sm text-gray-500">${produto.marca || 'Marca não disponível'}</p>
                </div>
                <p class="text-xl font-bold text-blue-600 ml-4">${formatter.format(produto.preco)}</p>
            </div>
            <div class="mt-4 pt-4 border-t border-gray-200 text-sm text-gray-600 grid grid-cols-2 gap-x-4 gap-y-1">
                <p><span class="font-semibold">Código:</span> ${produto.codigoSistema}</p>
                <p><span class="font-semibold">Referência:</span> ${produto.referencia || 'N/A'}</p>
                ${produto.codigoBarras ? `<p class="col-span-2"><span class="font-semibold">Cód. Barras:</span> ${produto.codigoBarras}</p>` : ''}
            </div>
        `;
        lista.appendChild(card);
    });
    resultadosDiv.appendChild(lista);
};

// --- EVENTOS DOS BOTÕES E TECLA ENTER ---
btnBuscaNome.addEventListener('click', buscarPorNome);
btnBuscaCodigoEan.addEventListener('click', buscarPorCodigoEan);
btnBuscaCodigo.addEventListener('click', buscarPorCodigo);
btnBuscaRef.addEventListener('click', buscarPorReferencia);

inputNome.addEventListener('keyup', e => e.key === 'Enter' && buscarPorNome());
inputCodigoEan.addEventListener('keyup', e => e.key === 'Enter' && buscarPorCodigoEan());
inputCodigo.addEventListener('keyup', e => e.key === 'Enter' && buscarPorCodigo());
inputRef.addEventListener('keyup', e => e.key === 'Enter' && buscarPorReferencia());


// --- FUNÇÃO PARA CARREGAR A DATA DE ATUALIZAÇÃO ---

const carregarUltimaAtualizacao = async () => {
    const updateElement = document.getElementById('last-update');
    try {
        const response = await fetch('/produtos/ultima-atualizacao');

        if (response.ok) {
            const dataTexto = await response.text();
            updateElement.textContent = `Última atualização: ${dataTexto}`;
        } else {
            updateElement.textContent = 'Não foi possível carregar a data de atualização.';
        }
    } catch (error) {
        console.error('Erro ao buscar data de atualização:', error);
        updateElement.textContent = 'Erro de conexão ao buscar data.';
    }
};

// --- EVENTO PARA EXECUTAR QUANDO A PÁGINA CARREGAR ---

document.addEventListener('DOMContentLoaded', () => {
    carregarUltimaAtualizacao();
});