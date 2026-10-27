# Restaurant API — Fase 2

API REST em Spring Boot com **Clean Architecture** para gestão de **tipos de usuário**, **usuários**, **restaurantes** e **itens do cardápio**.

**Repositório:** https://github.com/BernardoSemiOficial/restaurant-fase2-posgraduacao

## Stack

- Java 25 / Spring Boot 4.1
- Spring Data JPA + PostgreSQL
- Spring Security (BCrypt; endpoints liberados para avaliação)
- SpringDoc OpenAPI (Swagger UI)
- Docker / Docker Compose
- JaCoCo (meta de cobertura de linhas ≥ 80% em `./mvnw verify`)

## Como executar

### Opção 1 — App + banco com Docker Compose

```bash
docker compose --profile full up --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Postgres (host): `localhost:5436`

### Opção 2 — Desenvolvimento local (Maven)

Requisitos: JDK 25+ e Docker (para o Postgres do Compose).

```bash
./mvnw spring-boot:run
```

### Testes e cobertura

```bash
./mvnw verify
```

Relatório JaCoCo: `target/site/jacoco/index.html`.

## Endpoints principais (`/api/v1`)

| Método | Path | Descrição |
|---|---|---|
| CRUD | `/user-types` | Tipos de usuário (`name` + `category`) |
| CRUD | `/users` | Usuários |
| `PATCH` | `/users/{id}/user-type` | Associa/troca tipo (bloqueado se for dono de restaurante) |
| CRUD | `/restaurants` | Restaurantes (dono deve ser `RESTAURANT_STAFF`) |
| CRUD | `/restaurants/{restaurantId}/menu-items` | Itens do cardápio |

Seed automático ao subir: tipos `Cliente` (`CUSTOMER`), `Dono` e `Equipe` (`RESTAURANT_STAFF`).

## Documentação

| Arquivo | Conteúdo |
|---|---|
| [docs/CLEAN-ARCHITECTURE.md](docs/CLEAN-ARCHITECTURE.md) | Guia de Clean Architecture do repositório |
| [docs/RELATORIO-TECNICO.md](docs/RELATORIO-TECNICO.md) | Relatório técnico (arquitetura, endpoints, execução) |
| [Restaurant API - Fase 2 - Clean Architecture.postman_collection.json](Restaurant%20API%20-%20Fase%202%20-%20Clean%20Architecture.postman_collection.json) | Collection Postman |

Variável Postman: `baseUrl` = `http://localhost:8080`.

## Vídeo de entrega

Grave ~5 minutos mostrando: Compose/Swagger, CRUD dos 3 recursos do enunciado, regras (staff no dono, bloqueio de troca de tipo, cascade no cardápio) e, se possível, `./mvnw verify`.
