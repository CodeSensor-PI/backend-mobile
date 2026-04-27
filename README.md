# Psi Rizerio Mobile - Backend

Este é o backend do ecossistema mobile **Psi Rizerio**, construído com Java 21, Spring Boot 3.x, PostgreSQL, e integrado à Inteligência Artificial (Google Gemini) para a geração de relatórios clínicos a partir do feedback dos pacientes.

## Arquitetura

O projeto utiliza **Domain-Driven Design (DDD)** aliado à estratégia de **Package by Feature**.
Isso significa que a aplicação está dividida verticalmente por domínio funcional:
- **`auth`**: Segurança, Autenticação, Usuários, JWT e Roles.
- **`patient`**: Domínio central do Paciente (Dados Pessoais, Anotações).
- **`feedback`**: Domínio para capturar os relatos e métricas (ex: humor) fornecidos pelo paciente no app mobile.
- **`report`**: Domínio responsável pela geração de relatórios através da Inteligência Artificial.

Em cada feature, as camadas respeitam a segregação do DDD:
- **`domain`**: Entidades e Interfaces de Repositório/Portas (Sem acoplamento com infra/web).
- **`application`**: Casos de uso, Services, DTOs e Mappers (MapStruct).
- **`infrastructure`**: Implementações de persistência (JPA) e Integrações (RestTemplate para Gemini).
- **`interfaces`**: Controladores REST para expor os endpoints.

## Especificação da Integração com IA (Google Gemini)

O backend realiza a geração automática de relatórios evolutivos.

### Como funciona no Backend
1. Quando o endpoint `POST /api/v1/patients/{id}/reports/generate` é chamado, o sistema busca o Paciente e seus `Feedbacks` recentes.
2. O `ReportService` orquestra a construção de um "Prompt" combinando as notas clínicas do paciente e as entradas de diário/feedbacks (com datas e humor).
3. O prompt é enviado ao Google Gemini via integração HTTP REST direta.
4. A IA analisa os sentimentos, traça a evolução do paciente e identifica alertas de risco, retornando um texto formatado em Markdown.
5. O resultado é armazenado no banco como um `Report` associado ao paciente.

### Como aplicar no Frontend (Mobile)
1. **Histórico de Feedbacks**: O paciente insere feedbacks via mobile e o frontend consome a API (`POST /api/v1/feedbacks`).
2. **Área do Profissional**: O psicólogo ou profissional de saúde acessa o perfil do paciente e clica em "Gerar Relatório de Evolução com IA".
3. O app mobile chama o endpoint de geração.
4. O app recebe o resultado (campo `aiAnalysisContent` em Markdown) e o exibe utilizando uma biblioteca de renderização Markdown (ex: `react-native-markdown-display`).

## Tecnologias Utilizadas
- Java 21 & Spring Boot 3.2.x
- PostgreSQL 15
- MapStruct & Lombok
- Spring Security & JWT (jjwt)
- Docker & Docker Compose

## Pré-requisitos
- Docker e Docker Compose instalados.
- (Opcional) Java 21 e Maven para rodar localmente sem Docker.

## Como Executar

### Usando Docker (Recomendado)
Para iniciar toda a infraestrutura (Banco de Dados PostgreSQL e a Aplicação Spring Boot):

1. Defina a chave da API do Gemini (Windows Powershell):
```powershell
$env:GEMINI_API_KEY="sua-chave-aqui"
```
2. Suba o ambiente:
```bash
docker-compose up --build -d
```
3. A aplicação estará rodando em: `http://localhost:8080`.

### Rodando Localmente (Sem Docker)
1. Suba apenas o PostgreSQL no Docker Compose ou tenha um banco local rodando na porta 5432 com banco `psimobile` e credenciais `postgres/postgres`.
```bash
docker-compose up postgres -d
```
2. Rode a aplicação com Maven:
```bash
set GEMINI_API_KEY=sua-chave-aqui
mvn spring-boot:run
```

## Endpoints Principais

### Autenticação (`/api/v1/auth`)
- `POST /api/v1/auth/register` - Registra um novo usuário (Profissional).
- `POST /api/v1/auth/authenticate` - Autentica o usuário e retorna o token JWT.

*Todos os próximos endpoints exigem o Header: `Authorization: Bearer <seu-token-jwt>`*

### Pacientes (`/api/v1/patients`)
- `GET /api/v1/patients` - Lista pacientes.
- `POST /api/v1/patients` - Cria um novo paciente.
- `GET /api/v1/patients/{id}` - Busca um paciente por ID.
- `PUT /api/v1/patients/{id}` - Atualiza um paciente.
- `DELETE /api/v1/patients/{id}` - Remove um paciente.

### Feedbacks (`/api/v1/feedbacks`)
- `POST /api/v1/feedbacks` - Registra um novo feedback do paciente.
- `GET /api/v1/feedbacks/patient/{patientId}` - Lista todos os feedbacks de um paciente.

### Relatórios com IA (`/api/v1/patients/{patientId}/reports`)
- `POST /api/v1/patients/{patientId}/reports/generate` - Solicita à IA a geração de um relatório com base no histórico.
- `GET /api/v1/patients/{patientId}/reports` - Lista o histórico de relatórios gerados para aquele paciente.