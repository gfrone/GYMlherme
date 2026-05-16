<div align="center">

![Ronnie Coleman](figures/source.gif)

# GYMLHERME

![Academia](figures/ChatGPT%20Image%2016%20de%20mai.%20de%202026%2C%2016_23_55.png)

*Sistema de gerenciamento de academia desenvolvido como trabalho de aula.*

</div>

---

## O que é

GYMLHERME é um sistema CLI de gerenciamento de academia escrito em Java, com persistência em PostgreSQL via JDBC. O sistema permite que um administrador gerencie alunos, planos, exercícios e templates de treino, enquanto os alunos acompanham sua progressão de cargas e gerenciam seus programas de treinamento diretamente pelo terminal.

---

## O que faz

### Perfil Admin
- Cadastrar, listar e deletar alunos, personais e exercícios
- Criar e deletar templates de treino (divisões de treino padrão para todos os alunos)
- Criar e gerenciar planos (Básico, Normal, Premium)
- Vincular e atualizar planos de alunos
- Consultar o plano ativo de qualquer aluno

### Perfil Aluno
- Login por CPF com validação de plano ativo
- Iniciar sessão de treino — registra início/fim e permite atualizar cargas por exercício
- Ver progressão de carga por exercício (histórico + PR)
- Mudar frequência semanal e divisão de treino
- Criar divisão de treino customizada (Normal e Premium)
- Escolher personal trainer (Premium)
- Alterar ou cancelar plano

---

## Estrutura de Treino

O treino é modelado em camadas hierárquicas:

```
TrainingFrequency (3x, 4x, 5x/semana)
    └── WorkoutSplit (ABC, Upper/Lower, FullBody...)
            └── WorkoutTemplate (Treino A, Treino B, Treino C...)
                    └── WorkoutExerciseTemplate (Supino, Agachamento... + séries/reps/carga/descanso)
```

Quando um aluno escolhe um split, o sistema copia toda essa hierarquia para tabelas individuais do aluno (`ClientWorkoutProgram → ClientWorkout → ClientWorkoutExercise`), permitindo progressão independente. O histórico de cargas (`LoadHistory`) é separado de qualquer programa — o PR nunca se perde ao trocar de divisão.

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 11+ | Linguagem principal |
| PostgreSQL | Banco de dados relacional |
| JDBC | Comunicação com o banco (sem ORM) |
| Driver `postgresql-42.7.4.jar` | Incluído manualmente, sem Maven/Gradle |

---

## Como executar

**Pré-requisitos:** JDK 11+, PostgreSQL rodando localmente.

**1. Criar o banco de dados:**
```sql
-- Execute o script em sql/guilherme.session.sql no seu PostgreSQL
```

**2. Configurar a conexão** em `src/main/dao/ConnectionFactory.java` (URL, usuário e senha).

**3. Compilar** (a partir da raiz do projeto):
```bash
javac -cp ".;postgresql-42.7.4.jar" -sourcepath . src\Main.java
```

**4. Executar:**
```bash
java -cp ".;postgresql-42.7.4.jar" src.Main
```

> No Linux/Mac substitua `;` por `:` no classpath.

**Login admin:** senha `admin123`  
**Login aluno:** CPF cadastrado

---

## Estrutura do Projeto

```
paradinhas/
├── src/
│   ├── Main.java                    — Menus e fluxos do CLI
│   └── main/
│       ├── dao/
│       │   ├── ClientDAO.java       — Todas as queries e operações no banco
│       │   └── ConnectionFactory.java
│       └── model/                   — POJOs espelhando as tabelas
├── sql/
│   ├── guilherme.session.sql        — Schema completo do banco
│   └── clear_section.sql            — Reset das tabelas
├── figures/                         — Imagens do projeto
└── postgresql-42.7.4.jar
```

---

## Limitações conhecidas

- Sem verificação de disponibilidade de personal trainer
- Senha do admin hardcoded; alunos autenticam apenas por CPF (sem senha)
- Admin não pode editar templates existentes — apenas criar ou deletar
- Sem renovação automática de plano ou aviso de vencimento
- Sem testes automatizados
- Sistema monousuário (CLI sequencial, sem controle de concorrência)

---

## Autor

Desenvolvido por **Guilherme** — trabalho de aula.
