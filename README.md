# JMC Estoque - Catálogo Digital

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)

**JMC Estoque** é uma aplicação web desenvolvida para a consulta de catálogo de produtos da loja **Jairinho Materiais de Construção**. O projeto funciona como um catálogo digital ágil, permitindo que vendedores e clientes consultem preços de produtos em tempo real, com suporte para dispositivos móveis (PWA).

## 🚀 Funcionalidades

* **Consulta Rápida de Produtos:** Busca inteligente por:
    * Nome do produto (ex: "Cimento", "Bloco").
    * Código de Barras (EAN).
    * Código Interno do Sistema.
    * Referência do Fabricante.
* **Gestão de Estoque via CSV:** Atualização em massa do banco de dados através de upload de arquivos CSV.
* **Indicador de Atualização:** Exibição clara da data e hora da última carga de dados realizada.
* **Progressive Web App (PWA):** Interface otimizada para mobile com manifesto de aplicativo e Service Worker configurado.
* **Interface Responsiva:** Design limpo e moderno construído com Tailwind CSS.

## 🛠️ Tecnologias Utilizadas

### Backend
* **Java 17**
* **Spring Boot 3.5.14**
* **Spring Data JPA** (Persistência de dados)
* **OpenCSV** (Processamento de arquivos de carga)
* **Lombok** (Redução de boilerplate code)

### Frontend
* **HTML5 & JavaScript (ES6+)**
* **Tailwind CSS** (via CDN)
* **Fetch API** (Comunicação assíncrona com o backend)

### Banco de Dados
* **H2 Database:** Banco em memória para ambiente de desenvolvimento (`dev`).
* **PostgreSQL:** Banco robusto para ambiente de produção (`prod`).

### Infraestrutura
* **Docker:** Containerização da aplicação para deploy simplificado.
* **Maven:** Gerenciamento de dependências e build.

## ⚙️ Configuração e Execução

### Pré-requisitos
* Java JDK 17
* Maven

### Perfis de Execução (Profiles)

O projeto está configurado com dois perfis principais:

1.  **Dev (`dev`):** Utiliza banco H2 em memória. Ideal para testes locais rápidos.
2.  **Prod (`prod`):** Utiliza PostgreSQL. Exige configuração de variáveis de ambiente.

### Rodando Localmente (Perfil Dev)

```bash
# Clone o repositório
git clone [https://github.com/erickborgess/jmc-estoque.git](https://github.com/erickborgess/jmc-estoque.git)

# Entre na pasta
cd jmc-estoque

# Execute com Maven (O perfil 'dev' geralmente é o padrão se não especificado, ou ajuste no application.properties)
./mvnw spring-boot:run
````

Acesse: `http://localhost:8080`

### Rodando com Docker (Perfil Prod)

Para rodar em produção (como no Render.com), configure as seguintes variáveis de ambiente:

  * `SPRING_PROFILES_ACTIVE`: `prod`
  * `DB_HOST`: Host do banco PostgreSQL
  * `DB_PORT`: Porta do banco (ex: 5432)
  * `DB_NAME`: Nome do banco de dados
  * `DB_USER`: Usuário do banco
  * `DB_PASS`: Senha do banco

Build e execução via Docker:

```bash
docker build -t jmc-estoque .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod -e DB_HOST=... jmc-estoque
```

## 📂 Formato do Arquivo de Importação (CSV)

Para atualizar o estoque através da página `/upload.html`, o arquivo CSV deve seguir estritamente o formato abaixo, utilizando **ponto e vírgula (;)** como separador:

| Índice | Coluna | Descrição | Exemplo |
| :--- | :--- | :--- | :--- |
| 0 | Código Sistema | ID único do produto no sistema interno | `12345` |
| 1 | Nome | Descrição do produto | `CIMENTO CP II` |
| 2 | Código de Barras | EAN/GTIN do produto | `7891234567890` |
| 3 | (Ignorado) | Coluna não utilizada atualmente | - |
| 4 | Referência | Referência do fabricante | `REF-99` |
| 5 | Marca | Marca/Fabricante | `VOTORAN` |
| 6 | Preço | Valor unitário (use vírgula ou ponto) | `35,90` |

*O sistema trata automaticamente a conversão de vírgulas para pontos no preço.*

## 🤝 Evolução

Este é um projeto acadêmico/profissional em desenvolvimento contínuo.

## 📄 Licença

Este projeto está sob a licença definida no arquivo `LICENSE`.

-----

Desenvolvido por **Erick Borges**
