document.addEventListener('DOMContentLoaded', () => {
    const uploadForm = document.getElementById('uploadForm');
    const csvFileInput = document.getElementById('csvFile');
    const btnUpload = document.getElementById('btnUpload');
    const feedbackDiv = document.getElementById('feedbackUpload');

    const mostrarFeedback = (mensagem, tipo = 'info') => {
        let corTexto = 'text-gray-700';
        if (tipo === 'sucesso') {
            corTexto = 'text-green-600';
        } else if (tipo === 'erro') {
            corTexto = 'text-red-600';
        }
        
        feedbackDiv.innerHTML = `<p class="${corTexto} font-medium">${mensagem}</p>`;
        feedbackDiv.style.display = 'block';
    };

    const mostrarLoader = (enviando = false) => {
        if (enviando) {
            btnUpload.disabled = true;
            btnUpload.innerHTML = `
                <div class="flex justify-center items-center">
                    <div class="loader ease-linear rounded-full border-4 border-t-4 border-gray-200 h-6 w-6" style="border-top-color: white;"></div>
                    <span class="ml-2">Enviando...</span>
                </div>`;
            feedbackDiv.style.display = 'none';
        } else {
            btnUpload.disabled = false;
            btnUpload.innerHTML = 'Enviar Arquivo';
        }
    };


    uploadForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const file = csvFileInput.files[0];

        if (!file) {
            mostrarFeedback('Por favor, selecione um arquivo CSV.', 'erro');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        mostrarLoader(true);

        try {
            // Faz a requisição POST para o endpoint /file/upload
            const response = await fetch('/file/upload', {
                method: 'POST',
                body: formData,
            });

            // Pega a resposta do servidor como texto
            const responseBody = await response.text();

            if (response.ok) {
                mostrarFeedback(responseBody, 'sucesso');
                uploadForm.reset(); // Limpa o campo do arquivo
            } else {
                // Mostra a mensagem de erro que veio do controller
                mostrarFeedback(`Erro: ${responseBody}`, 'erro');
            }

        } catch (error) {
            console.error('Erro de rede:', error);
            mostrarFeedback('Erro de conexão. Não foi possível conectar ao servidor.', 'erro');
        } finally {
            mostrarLoader(false);
        }
    });
});