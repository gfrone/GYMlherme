# GYMlherme

<p align="center">
  <img src="src/assets/200.gif" alt="GYMlherme Demo" width="45%" />
  <img src="src/assets/Gemini_Generated_Image_a1u8iia1u8iia1u8.png" alt="Academia Fictícia" width="45%" />
</p>

Sistema de controle de academia desenvolvido em **Java** com persistência de dados em banco de dados relacional (**PostgreSQL**).

---

## 📋 Descrição

Este projeto implementa um sistema completo de gerenciamento de academia, permitindo o cadastro e controle de alunos, planos, exercícios e treinos. O sistema conta com funcionalidades para instrutores e alunos.

---

## 🗄️ Banco de Dados

Os dados de **planos**, **alunos** e **exercícios** são armazenados em um banco de dados relacional (PostgreSQL).

---

## 👤 Alunos

Deve ser possível **cadastrar alunos** com as seguintes operações:


- Incluir
- Alterar
- Excluir
- Listar
- Buscar pelo **CPF**
- Buscar pelo **nome**

### Campos do Aluno

| Campo              | Tipo   |
|--------------------|--------|
| CPF                | String |
| Nome               | String |
| Data de Nascimento | Date   |

### Exemplo

```
---------------------------------------------------------
Alunos:
---------------------------------------------------------
123.456.789-01     Fulano    20/01/1976
234.567.890-12     Ciclano   01/01/2000
---------------------------------------------------------
```

---

## 💳 Planos

Deve ser possível **cadastrar planos**. Cada plano possui:

| Campo        | Tipo    |
|--------------|---------|
| Código       | Integer |
| Nome         | String  |
| Valor Mensal | Decimal |

### Exemplo

```
----------------------------------------
Planos:
----------------------------------------
1   Simples   79,90
2   Gold      89,90
3   Premium   99,90
----------------------------------------
```

---

## 🏋️ Exercícios

Deve ser possível **cadastrar exercícios**. Cada exercício possui:

| Campo              | Tipo   |
|--------------------|--------|
| Número             | Integer |
| Nome               | String |
| Músculos Ativados  | String |

### Exemplo

```
----------------------------------------
Exercícios
----------------------------------------
01   Leg Press                            Quadríceps, Glúteos
05   Cadeira Adutora                      Adutores
20   Supino Máquina                       Peitorais, Tríceps
26   Crucifixo Máquina                    Peitorais
40   Abdominal Máquina                    Abdominais
50   Desenvolvimento Máquina Aberto       Deltoides, Trapézio, Tríceps
----------------------------------------
```

---

## 🎓 Funcionalidades do Instrutor

Para alunos cadastrados, o instrutor pode:

### Plano do Aluno
- Informar o **plano atual** do aluno:
  - Código do plano
  - Data de início do plano
  - Dados do cartão de crédito

### Treinos
- **Cadastrar** uma ou mais opções de treino para o aluno
  - Cada opção de treino possui: **nome** e uma **lista de exercícios**
  - Para cada exercício, informar:
    - Número de séries
    - Número mínimo e máximo de repetições
    - Carga utilizada (em kg)
    - Tempo de descanso (em minutos)
- **Alterar** opções de treino e dados dos exercícios cadastrados
- **Excluir** opções de treino

---

## 🏃 Funcionalidades do Aluno

O aluno pode, em uma determinada data:

1. **Iniciar um treino** — escolher uma das opções de treino disponíveis
2. **Consultar os exercícios** a serem feitos, com todos os dados cadastrados
3. **Encerrar o treino**

---

## 📊 Relatórios

- Mostrar as **datas em que o aluno compareceu à academia** em um intervalo de datas informado

---

## ⭐ Bônus

- O aluno pode **alterar a carga** de um determinado exercício
- Para um determinado exercício, mostrar a **evolução de cargas** ao longo do tempo

---

## 🚀 Tecnologias

- **Java**
- **PostgreSQL**
- **JDBC** / JPA / Hibernate (a definir)

---

## 📁 Estrutura do Projeto

```
GYMlherme/
├── src/
│   └── main/
│       └── java/
│           └── com/gymlherme/
│               ├── model/        # Entidades (Aluno, Plano, Exercício, Treino...)
│               ├── repository/   # Acesso ao banco de dados
│               ├── service/      # Regras de negócio
│               └── Main.java     # Ponto de entrada
├── README.md
└── ...
```

---

## ⚙️ Como Executar

1. Configure o banco de dados PostgreSQL com as credenciais desejadas
2. Atualize as configurações de conexão no projeto
3. Execute as migrations/scripts SQL para criar as tabelas
4. Compile e execute o projeto Java

```bash
# Compilar
javac -cp . src/main/java/com/gymlherme/Main.java

# Executar
java com.gymlherme.Main
```

---

## 📝 Licença

Projeto educacional — uso livre para fins de aprendizado.
