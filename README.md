# Coupon API

API REST para gerenciamento de cupons, desenvolvida como parte de um desafio técnico utilizando Java e Spring Boot.

O projeto implementa criação, consulta e exclusão lógica de cupons, mantendo as regras de negócio encapsuladas no domínio e separando as responsabilidades entre as camadas da aplicação.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- H2 Database
- Gradle
- JUnit 5
- Mockito
- MockMvc
- Swagger / OpenAPI
- Docker
- Docker Compose
- Lombok

## Arquitetura

O projeto foi estruturado buscando separar as regras de negócio das tecnologias externas.

```text
coupon
├── application
│   ├── command
│   ├── port
│   └── usecase
├── config
├── domain
│   ├── enums
│   ├── exception
│   └── model
├── persistence
│   ├── adapter
│   ├── entity
│   ├── mapper
│   └── repository
└── web
    ├── contract
    ├── controller
    ├── dto
    ├── handler
    └── mapper
```

### Domain

Contém o modelo de domínio e as regras de negócio do cupom, sem dependência das camadas Web ou Persistence.

### Application

Contém os casos de uso da aplicação e as portas utilizadas para comunicação com as demais camadas.

Os casos de uso são separados por responsabilidade:

- `CreateCouponUseCase`
- `GetCouponUseCase`
- `DeleteCouponUseCase`

### Persistence

Implementa a persistência utilizando Spring Data JPA e banco H2.

### Web

Responsável pela exposição da API REST, contratos HTTP, DTOs, validação das requisições, tratamento de exceções e mapeamento dos objetos.

## Regras de negócio

### Criação

- `code`, `description`, `discountValue` e `expirationDate` são obrigatórios.
- O código do cupom deve possuir exatamente 6 caracteres alfanuméricos.
- Caracteres especiais informados no código são removidos antes da persistência.
- Após a remoção dos caracteres especiais, o código deve continuar contendo exatamente 6 caracteres.
- O desconto mínimo permitido é `0.5`.
- Não existe valor máximo de desconto.
- A data de expiração não pode estar no passado.
- O cupom pode ser criado como publicado.
- Um novo cupom é criado com status `ACTIVE`.
- Um novo cupom ainda não foi resgatado (`redeemed = false`).

### Exclusão

- A exclusão é lógica (soft delete).
- Os dados originais do cupom são preservados.
- O status do cupom passa para `DELETED`.
- Um cupom já excluído não pode ser excluído novamente.
- Cupons excluídos não são retornados pela consulta.

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/coupon` | Cadastra um novo cupom |
| GET | `/coupon/{id}` | Consulta um cupom pelo ID |
| DELETE | `/coupon/{id}` | Realiza a exclusão lógica do cupom |

## Executando localmente

### Pré-requisitos

- Java 21

No Linux/macOS:

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew bootRun
```

A aplicação ficará disponível na porta `8080`.

## Executando com Docker

É necessário possuir Docker e Docker Compose instalados.

Execute:

```bash
docker compose up --build
```

Para encerrar:

```bash
docker compose down
```

A API ficará disponível na porta `8080`.

## Swagger

Com a aplicação em execução, a documentação da API pode ser acessada em:

`http://localhost:8080/swagger-ui/index.html`

A especificação OpenAPI está disponível em:

`http://localhost:8080/v3/api-docs`

## Banco H2

O projeto utiliza banco H2 em memória.

Console:

`http://localhost:8080/h2-console`

Configuração:

```text
JDBC URL: jdbc:h2:mem:coupondb
User Name: sa
Password: [vazio]
```

Por ser um banco em memória, os dados são perdidos quando a aplicação é encerrada.

## Testes

O projeto possui testes unitários e testes de integração cobrindo os principais comportamentos e regras de negócio.

Entre os cenários testados estão:

- criação de cupom;
- sanitização do código;
- validação do tamanho do código;
- campos obrigatórios;
- desconto mínimo;
- desconto sem limite máximo;
- data de expiração;
- criação de cupom publicado;
- consulta por ID;
- comportamento de cupom excluído;
- soft delete;
- tentativa de excluir um cupom duas vezes;
- preservação dos dados após exclusão;
- persistência utilizando H2.

Para executar todos os testes:

Linux/macOS:

```bash
./gradlew test
```

Windows:

```powershell
.\gradlew test
```

## Observações

O banco H2 e seu console são utilizados apenas para fins de desenvolvimento e testes.

A aplicação utiliza portas e adaptadores para manter as regras de negócio desacopladas das tecnologias de persistência e da camada Web.