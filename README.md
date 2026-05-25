# Psi Rizerio Mobile - Backend

Este é o backend do ecossistema mobile **Psi Rizerio**, construído com Java 21, Spring Boot 3.x, PostgreSQL, e integrado à Inteligência Artificial (Google Gemini) para a geração de relatórios clínicos a partir do feedback dos pacientes.

---

## Arquitetura

O projeto utiliza **Domain-Driven Design (DDD)** aliado à estratégia de **Package by Feature**.
A aplicação está dividida verticalmente por domínio funcional:
- **`auth`**: Segurança, Autenticação, Usuários, JWT e Roles.
- **`patient`**: Domínio central do Paciente (Dados Pessoais, Anotações, Localização).
- **`feedback`**: Domínio para capturar os relatos e métricas (ex: humor) fornecidos pelo paciente.
- **`report`**: Domínio responsável pela geração de relatórios através do Google Gemini.

---

## Tecnologias Utilizadas
- Java 21 & Spring Boot 3.2.x (compilado via container)
- PostgreSQL 15 & Flyway para migrações
- MapStruct & Lombok
- Podman / Docker

---

## Como Executar o Banco de Dados (PostgreSQL)

O projeto está totalmente migrado para o PostgreSQL (H2 removido). Para rodar o banco usando **Podman**, siga os passos abaixo:

1. **Criar a rede no Podman:**
   ```bash
   podman network create psimobile_net
   ```

2. **Iniciar o container do PostgreSQL:**
   ```bash
   podman run -d --name psimobile_postgres --network psimobile_net -p 5432:5432 -e POSTGRES_DB=psimobile -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=12345 -v postgres_data:/var/lib/postgresql/data postgres:15-alpine
   ```

3. **(Opcional) Restaurar o dump de dados de simulação:**
   Caso queira iniciar o banco a partir do dump de simulação gerado:
   ```bash
   # Copiar o arquivo dump.sql para dentro do container
   podman cp dump.sql psimobile_postgres:/tmp/dump.sql
   # Importar o dump
   podman exec -it psimobile_postgres psql -U postgres -d psimobile -f /tmp/dump.sql
   ```

---

## Como Executar o Backend

### 1. Usando Containers (Podman)
Você pode buildar e rodar o backend encapsulado em um container na mesma rede do banco:

```bash
# 1. Buildar a imagem do backend
podman build -t psimobile_backend .

# 2. Iniciar o container do backend conectado à rede
podman run -d --name psimobile_backend --network psimobile_net -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://psimobile_postgres:5432/psimobile -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=12345 psimobile_backend
```

### 2. Rodando Localmente
Se você possuir Java 21 e Maven instalados na máquina, configure as variáveis no seu `.env` ou execute passando os parâmetros:
```bash
# Definir a chave da API do Gemini (Windows Powershell):
$env:GEMINI_API_KEY="sua-chave-aqui"

# Executar a aplicação via Maven
mvn spring-boot:run
```

A API estará disponível localmente em: `http://localhost:8080`.

---

## Endpoints Principais
*Todos os endpoints que não sejam de autenticação exigem o header: `Authorization: Bearer <seu-token-jwt>`*

### Autenticação (`/api/v1/auth`)
- `POST /api/v1/auth/register` - Registra um novo psicólogo.
- `POST /api/v1/auth/authenticate` - Realiza login (Credenciais padrão de teste: `psicologo@teste.com` / `senha123`).

### Pacientes (`/clientes`)
- `GET /clientes` - Lista pacientes.
- `POST /clientes` - Cria um novo paciente (contendo latitude/longitude).

### Agenda e Sessões (`/sessoes`)
- `GET /sessoes` - Lista sessões cadastradas.
- `POST /sessoes` - Cria um agendamento.
- `PUT /sessoes/cancelar/{id}` - Cancela uma sessão por ID.

### Feedbacks (`/api/v1/feedbacks`)
- `POST /api/v1/feedbacks` - Registra um novo feedback do paciente (humor e diário).
- `GET /api/v1/feedbacks/patient/{patientId}` - Lista todos os feedbacks de um paciente específico.

### Relatórios com IA (`/api/v1/patients/{patientId}/reports`)
- `POST /api/v1/patients/{patientId}/reports/generate` - Solicita à IA a geração de um relatório com base nos feedbacks.
- `GET /api/v1/patients/{patientId}/reports` - Lista o histórico de relatórios gerados.