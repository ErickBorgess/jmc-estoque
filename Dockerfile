# --- Estágio 1: Build (Construção) ---
# Usa uma imagem oficial do Maven (com Java 17) para compilar o projeto
FROM maven:3.8.5-openjdk-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o 'pom.xml'
COPY pom.xml .

# Copia o resto do código-fonte
COPY src ./src

# Executa o comando do Maven para compilar o projeto e gerar o .jar  -DskipTests pula os testes
RUN mvn clean package -DskipTests

# --- Estágio 2: Run (Execução) ---
# Usamos uma imagem base do Java 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o .jar que foi gerado no Estágio 1 para o container final
COPY --from=build /app/target/jmc-estoque-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 Spring para o Render
EXPOSE 8080

# Comando para iniciar a aplicação quando o container rodar
ENTRYPOINT ["java", "-jar", "app.jar"]
