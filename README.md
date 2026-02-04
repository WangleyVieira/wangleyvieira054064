## Projeto Desenvolvedor Back End – API REST de Artistas, Álbuns e Regionais

Projeto desenvolvido como desafio técnico backend, com foco em engenharia de software, boas práticas, código limpo, testes automatizados e facilidade de evolução.

A aplicação foi pensada como um cenário real de produção, incluindo integração com sistema externo, sincronização de dados, versionamento de banco, armazenamento de arquivos e ambiente padronizado via Docker.

## Dados da candidatura

Nome: WANGLEY MATHEUS DE SOUZA VIEIRA - N° Inscrição : 16453

PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG e demais Órgãos - Engenheiro da Computação- Sênior

Cidade: Cuiabá

Local: SECRETARIA DE ESTADO DE PLANEJAMENTO E GESTÃO

Cargo: ANALISTA DE TECNOLOGIA DA INFORMAÇÃO

Perfil: ENGENHEIRO DA COMPUTAÇÃO - SÊNIOR

Stack principal:
Java 21 · Spring Boot · JPA/Hibernate · PostgreSQL · MinIO · Docker · Flyway · JUnit · Mockito

##  Índice

1. [Dados da candidatura](#dados-da-candidatura)
2. [Objetivo do projeto](#objetivo-do-projeto)
3. [Arquitetura adotada](#arquitetura-adotada)
4. [Modelagem de domínio](#modelagem-de-domínio)
5. [Integração com sistema externo (Regionais)](#integração-com-sistema-externo-regionais)
6. [Sincronização de dados externos](#sincronização-de-dados-externos)
7. [Versionamento do banco de dados](#versionamento-do-banco-de-dados)
8. [Armazenamento de arquivos](#armazenamento-de-arquivos)
9. [Estratégia de testes](#estratégia-de-testes)
10. [Ambiente com Docker](#ambiente-com-docker)
11. [Decisões técnicas relevantes](#decisões-técnicas-relevantes)
12. [O que não foi priorizado](#o-que-não-foi-priorizado)
13. [Instalação e execução do projeto](#instalação-e-execução-do-projeto)
14. [Considerações finais](#considerações-finais)


## Objetivo do projeto

Construir uma API REST para gerenciamento de Artistas, Álbuns e Regionais, priorizando:

- Código legível e organizado

- Separação clara de responsabilidades

- Facilidade de testes

- Evolução segura do banco de dados

- Integração desacoplada com sistemas externos

Decisões técnicas coerentes com ambiente real

## Arquitetura adotada

A aplicação segue uma arquitetura em camadas bem definidas, favorecendo manutenção e escalabilidade:

1. **Controller**
: Responsável apenas por entrada/saída HTTP, validações e códigos de status.

2. **Service**
: Camada central de regras de negócio, orquestração de fluxos e controle transacional.

3. **Repository**
: Acesso a dados via Spring Data JPA, sem regras de negócio embutidas.

4. **DTOs (Request / Response)**
: Utilizados para evitar o acoplamento direto das entidades com a API pública.

Essa separação reduz impactos de mudança e facilita testes automatizados.

## Modelagem de domínio
Relacionamento Artista ↔ Álbum

- Relacionamento muitos-para-muitos

- Um artista pode participar de vários álbuns

- Um álbum pode conter vários artistas

- Entidades desacopladas dos contratos de API

Essa modelagem permite evolução futura sem reestruturações profundas.

## Integração com sistema externo (Regionais)

O sistema consome dados de uma API externa de regionais, mas mantém uma tabela interna própria, evitando dependência direta do serviço externo para consultas do dia a dia.

**Decisão arquitetural**

: Dados externos não sobrescrevem registros existentes.

Foi adotado um modelo de versionamento lógico, garantindo histórico e rastreabilidade.

**Estratégia aplicada**

- Registros novos são inseridos

- Registros ausentes passam a ser inativados

- Registros alterados geram uma nova versão ativa, mantendo o histórico

Essa abordagem evita perda de dados e facilita auditoria.

## Sincronização de dados externos

A sincronização foi implementada com foco em simplicidade e previsibilidade:

- Uso de Map para acesso O(1)

- Processamento apenas de registros ativos

- Operações explícitas (inativar / criar)

- Transações bem delimitadas

O comportamento do sistema é determinístico e fácil de testar.

## Versionamento do banco de dados

Foi utilizado Flyway para versionamento do schema:

- Migrations imutáveis

- Ajustes estruturais sempre em novas versões

- Nenhuma migration antiga é alterada

- Evolução incremental e segura

Isso garante que o projeto possa ser executado do zero sem inconsistências.

## Armazenamento de arquivos

Para simular um ambiente real:

- Utilizado MinIO (compatível com S3)

- Upload de capas de álbuns

- Geração de URLs temporárias (presigned)

- Metadados no banco, binários no storage

A solução permite troca futura do provider sem impacto no domínio.

## Estratégia de testes

Os testes foram escritos priorizando clareza e isolamento:

- Services testados com Mockito (@Mock e @InjectMocks)

- Controllers testados com MockMvc

- Cenários reais simulados:

: -Inserção

: - Atualização

: - Listagem

: - Integração externa

: - Sincronização de dados

Os testes validam comportamento, não implementação interna.

## Ambiente com Docker

A aplicação é entregue como containers orquestrados, garantindo ambiente reproduzível.

Serviços incluídos:

- API

- Banco de dados

- Storage de arquivos

Tudo pode ser executado com um único comando via Docker Compose.

## Decisões técnicas relevantes

Algumas decisões importantes tomadas durante o desenvolvimento:

- Preferência por soluções simples e explícitas

- Uso consciente de transações

- Histórico de dados ao invés de sobrescrita

- DTOs para proteger o domínio

- Commits pequenos e semânticos

- Código escrito para leitura por outros desenvolvedores

## O que não foi priorizado

Os itens abaixo foram conscientemente deixados fora do escopo:

- Autenticação e autorização avançadas

- Cache distribuído

- Observabilidade (logs estruturados, métricas)

A prioridade foi **garantir qualidade, clareza e consistência do core da aplicação**.

## Instalação e execução do projeto

Instalação e execução do projeto

**Pré-requisitos**

Para executar o projeto localmente, é necessário ter instalado:

- Docker

- Docker Compose

Não é necessário instalar Java, Maven ou banco de dados localmente.

**Subindo a aplicação com Docker**
1. **Clone o repositório**
    ```bash
    git clone https://github.com/WangleyVieira/wangleyvieira054064.git
    cd musicapi
   ```
2. **Suba os containers**
    ```bash
    docker-compose up -d
   ```
Esse comando irá subir:

- API Spring Boot

- Banco de dados PostgreSQL

- MinIO (armazenamento de arquivos)

O Flyway será executado automaticamente na inicialização, criando e versionando o banco de dados.

**Acessando a aplicação**

Após a inicialização:

- **API disponível em**
    ```bash
      http://localhost:8080
   ```

- **Console do MinIO:**
    ```bash
      http://localhost:9001
   ```

As credenciais do MinIO estão definidas no docker-compose.yml.

**Executando os testes**

- **Os testes podem ser executados localmente via Maven::**
    ```bash
      ./mvnw test
   ```
Os testes cobrem:

- Camada de serviços

- Camada de controllers

- Integração simulada com serviços externos

- Regras de sincronização e versionamento de dados

**Fluxo esperado ao rodar o projeto**

Ao subir a aplicação:

- O banco é criado e versionado automaticamente (Flyway)

- A API fica pronta para receber requisições

- A integração com regionais pode ser acionada via endpoint específico

- Dados externos são importados e sincronizados respeitando histórico

Encerrando os containers

- **Para parar o ambiente:**
    ```bash
      docker-compose down
   ```

## Considerações finais

Este projeto foi desenvolvido como se fosse um sistema real:

- Código autoral

- Foco em manutenção e evolução

- Facilidade de execução e avaliação

- Decisões técnicas justificadas

Cada escolha foi feita com base em engenharia de software, e não apenas no cumprimento literal de requisitos.