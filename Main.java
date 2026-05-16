import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final ClientDAO dao = new ClientDAO();
    private static final Scanner sc = new Scanner(System.in);
    private static final String ADMIN_SENHA = "admin123";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════╗");
        System.out.println("║       GYMLHERME          ║");
        System.out.println("╚══════════════════════════╝");

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n[1] Entrar como Admin");
            System.out.println("[2] Entrar como Aluno");
            System.out.println("[0] Sair");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: loginAdmin(); break;
                case 2: loginAluno(); break;
                case 0: rodando = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
        System.out.println("Até logo!");
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────────

    private static void loginAdmin() {
        System.out.print("Senha: ");
        if (!ADMIN_SENHA.equals(sc.nextLine().trim())) {
            System.out.println("Senha incorreta.");
            return;
        }
        menuAdmin();
    }

    private static void loginAluno() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        Client cliente = dao.getClientByCpf(cpf);
        if (cliente == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        //bloquear aluno com matricula vencida
        if (!cliente.getIsActive()) {
            System.out.println("Matrícula inativa. Procure a recepção.");
            return;
        }
        ClientPlan plano = dao.getActivePlan(cpf);
        if (plano == null) {
            System.out.println("Nenhum plano ativo. Procure a recepção.");
            return;
        }
        System.out.println("Bem-vindo(a), " + cliente.getFullName() + "!");
        menuAluno(cliente, plano);
    }

    // ─── MENU ADMIN ──────────────────────────────────────────────────────────────

    private static void menuAdmin() {
        boolean no_menu = true;
        while (no_menu) {
            System.out.println("\n══ ADMIN ════════════════════");
            System.out.println("[1] Aluno");
            System.out.println("[2] Treino / Exercícios");
            System.out.println("[3] Personal");
            System.out.println("[4] Plano");
            System.out.println("[0] Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: menuAdminAluno();    break;
                case 2: menuAdminTreino();   break;
                case 3: menuAdminPersonal(); break;
                case 4: menuAdminPlano();    break;
                case 0: no_menu = false;     break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAdminAluno() {
        boolean no_menu = true;
        while (no_menu) {
            System.out.println("\n── ALUNO ────────────────────");
            System.out.println("[1] Cadastrar aluno");
            System.out.println("[2] Deletar aluno");
            System.out.println("[3] Listar alunos");
            System.out.println("[4] Vincular plano");
            System.out.println("[5] Ver plano do aluno");
            System.out.println("[0] Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: adminCadastrarAluno();   break;
                case 2: adminDeletarAluno();     break;
                case 3: adminListarAlunos();     break;
                case 4: adminVincularPlano();    break;
                case 5: adminVerPlanoAluno();    break;
                case 0: no_menu = false;         break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAdminTreino() {
        boolean no_menu = true;
        while (no_menu) {
            System.out.println("\n── TREINO / EXERCÍCIOS ──────");
            System.out.println("[1] Cadastrar exercício");
            System.out.println("[2] Listar exercícios");
            System.out.println("[3] Deletar exercício");
            System.out.println("[4] Criar template de treino");
            System.out.println("[5] Deletar template");
            System.out.println("[0] Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: adminAdicionarExercicio(); break;
                case 2: adminListarExercicios();   break;
                case 3: adminDeletarExercicio();   break;
                case 4: adminAdicionarTemplate();  break;
                case 5: adminDeletarTemplate();    break;
                case 0: no_menu = false;           break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAdminPersonal() {
        boolean no_menu = true;
        while (no_menu) {
            System.out.println("\n── PERSONAL ─────────────────");
            System.out.println("[1] Cadastrar personal");
            System.out.println("[2] Listar personais");
            System.out.println("[3] Deletar personal");
            System.out.println("[0] Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: adminCadastrarPersonal(); break;
                case 2: adminListarPersonais();   break;
                case 3: adminDeletarPersonal();   break;
                case 0: no_menu = false;          break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAdminPlano() {
        boolean no_menu = true;
        while (no_menu) {
            System.out.println("\n── PLANO ────────────────────");
            System.out.println("[1] Criar plano");
            System.out.println("[2] Listar planos");
            System.out.println("[3] Deletar plano");
            System.out.println("[0] Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: adminCadastrarPlano(); break;
                case 2: adminListarPlanos();   break;
                case 3: adminDeletarPlano();   break;
                case 0: no_menu = false;       break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void adminListarAlunos() {
        List<Client> alunos = dao.getAllClients();
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        System.out.printf("%n── Alunos (%d) ──%n", alunos.size());
        for (Client c : alunos) {
            System.out.printf("  CPF: %s  |  %-30s  |  Nasc.: %s%n",
                c.getCpf(), c.getFullName(), c.getBirthDate().format(FMT));
        }
    }

    private static void adminListarPlanos() {
        List<Plan> planos = dao.getAllPlans();
        if (planos.isEmpty()) {
            System.out.println("Nenhum plano cadastrado.");
            return;
        }
        System.out.printf("%n── Planos (%d) ──%n", planos.size());
        for (Plan p : planos) {
            System.out.printf("  [%d] %s%n", p.getId(), p.getPlanName());
            System.out.println("      Customizar     : " + (p.isCanCustomizeWorkout() ? "Sim" : "Não"));
            System.out.println("      Personal       : " + (p.isIncludeTrainer()      ? "Sim" : "Não"));
        }
    }

    private static void adminListarExercicios() {
        List<Exercise> exercicios = dao.getAllExercises();
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado.");
            return;
        }
        System.out.printf("%n── Exercícios (%d) ──%n", exercicios.size());
        for (Exercise e : exercicios) {
            System.out.printf("  [%d] %-30s  %s%n",
                e.getExerciseNumber(), e.getName(), e.getMusclesWorked());
        }
    }

    private static void adminCadastrarAluno() {
        System.out.println("\n── Cadastrar Aluno ──────────");
        System.out.println("Digite 'quit' para voltar");

        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        if (cpf.equals("quit")) {
            return;
        }

        if(dao.clientExists(cpf)) {
            System.out.println("Aluno já Cadastrado.\nRetornando ...");
            return;
        }
    
        System.out.print("Nome completo: ");
        String nome = sc.nextLine().trim();
        if(nome.equals("quit")) {
            return;
        }
        LocalDate nascimento = lerData("Data de nascimento (dd/MM/yyyy): ");
        dao.addClient(new Client(cpf, nome, nascimento));
        System.out.println("Aluno cadastrado! Use 'Vincular plano' para associar um plano.");
    }

    private static void adminDeletarAluno() {
        System.out.println("\n── Remover Aluno ──────────");
        System.out.println("Digite 'q' para voltar");

        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        if (cpf.equals("quit") || cpf.equals("q")) {
            return;
        }

        // si no existe lo alueno, tenemos que retornar la funcion
        if(!dao.clientExists(cpf)) {
            System.out.println("Aluno não Cadastrado. Retornando ...");
            return;
        }
        dao.deactivateClient(cpf);
        System.out.println("Aluno deletado com sucesso!");
    }

    private static void adminVincularPlano() {
        System.out.println("\n── Vincular Plano ───────────");
        System.out.print("CPF do aluno: ");
        String cpf = sc.nextLine().trim();
        Client cliente = dao.getClientByCpf(cpf);
        
        //verificação se o aluno existe
        if (cliente == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        //verificar se ja existem planos cadastrados
        List<Plan> planos = dao.getAllPlans();
        if (planos.isEmpty()) {
            System.out.println("Nenhum plano cadastrado.");
            return;
        }

        //verificação se o aluno ja tem plano - dar um aviso, mas pode ser prosseguido para atualização, por exemplo
        if(dao.getActivePlan(cpf) != null) {
            if(!lerSimNao(("Atenção, aluno já possui plano, deseja prosseguir? "))) return;
        }

        System.out.println("\nPlanos disponíveis:");
        for (int i = 0; i < planos.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, planos.get(i).getPlanName());
        }
        System.out.print("Escolha o plano: ");
        int idxPlano = lerInt() - 1;
        if (idxPlano < 0 || idxPlano >= planos.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        Plan plano = planos.get(idxPlano);

        Integer trainerId = null;
        if (plano.isIncludeTrainer()) {
            List<Trainer> trainers = dao.getAllTrainers();
            if (!trainers.isEmpty()) {
                System.out.println("\nPersonais disponíveis:");
                for (int i = 0; i < trainers.size(); i++) {
                    System.out.printf("  [%d] %s — %s%n", i + 1, trainers.get(i).getName(), trainers.get(i).getSpecialization());
                }
                System.out.print("Escolha o personal (0 = nenhum): ");
                int idxT = lerInt() - 1;
                if (idxT >= 0 && idxT < trainers.size()) {
                    trainerId = trainers.get(idxT).getId();
                }
            }
        }

        System.out.print("Número do cartão: ");
        String cartao = sc.nextLine().trim();
        System.out.print("Validade do cartão (MM/AA): ");
        String validade = sc.nextLine().trim();

        LocalDate inicio = LocalDate.now();
        LocalDate fim    = inicio.plusDays(30);
        ClientPlan assinatura = new ClientPlan(cpf, plano.getId(), inicio, fim, cartao, validade, trainerId);
        // se ja tem plano, ele atualiza, senão ele matricula o aluno
        if (dao.getActivePlan(cpf) != null) {
            dao.upgradePlan(assinatura, cliente);
        } else {
            dao.subscribeClientToPlan(assinatura, cliente);
        }
        System.out.printf("Plano '%s' vinculado até %s!%n", plano.getPlanName(), fim.format(FMT));
    }

    private static void adminVerPlanoAluno() {
        System.out.print("CPF do aluno: ");
        String cpf = sc.nextLine().trim();
        Client cliente = dao.getClientByCpf(cpf);
        if (cliente == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        ClientPlan planoAtivo = dao.getActivePlan(cpf);
        if (planoAtivo == null) {
            System.out.printf("%s não possui plano ativo.%n", cliente.getFullName());
            return;
        }
        String nomePlano = dao.getPlanNameByClientSubscription(planoAtivo);
        System.out.printf("%nAluno : %s%n", cliente.getFullName());
        System.out.printf("Plano : %s%n", nomePlano);
        System.out.printf("Valido: %s até %s%n",
            planoAtivo.getStartDate().format(FMT),
            planoAtivo.getEndDate().format(FMT));
        if (planoAtivo.getTrainerId() != null) {
            System.out.printf("Personal ID: %d%n", planoAtivo.getTrainerId());
        }
    }

    private static void adminAdicionarExercicio() {
        System.out.println("\n── Adicionar Exercício ──────");
        System.out.print("Número do exercício: ");
        int numero = lerInt();
        if(dao.exerciseExists(numero)) {
            System.out.println("Exercicio já cadastrado: " + dao.getExerciceName(numero));
            return;
        }
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Músculos trabalhados: ");
        String musculos = sc.nextLine().trim();
        dao.addExercise(new Exercise(numero, nome, musculos));
        System.out.println("Exercício adicionado!");
    }

    private static void adminCadastrarPlano() {
        System.out.println("\n── Cadastrar Plano ──────────");
        boolean no_menu = true;
        while(no_menu){
            System.out.println("digite 'q' para sair");
            System.out.print("Nome do plano: ");
            String nome = sc.nextLine().trim();
            
            if(nome.equals("Q") || nome.equals("q")){
                break;
            }

            if(dao.Existplan(nome)){
                System.out.println("Plano já existe. Retornando ...");
                break;
            }

            // Todos os planos podem escolher frequência — a diferença é se pode customizar.
            boolean customiza = lerSimNao("Pode customizar treinos? (s/n): ");
            boolean personal  = lerSimNao("Inclui personal trainer? (s/n): ");
            dao.createPlan(new Plan(nome, customiza, personal));
            System.out.println("Plano cadastrado!");
        }
    }

    private static void adminCadastrarPersonal() {
        System.out.println("\n── Cadastrar Personal ───────");
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        if(dao.personalAlreadyExist(nome)){
            System.out.println("Personal já cadastrado!");
            return;
        }
        System.out.print("Especialização: ");
        String espec = sc.nextLine().trim();
        dao.registerTrainer(new Trainer(nome, espec));
        System.out.println("Personal cadastrado!");
    }

    private static void adminListarPersonais() {
        List<Trainer> trainers = dao.getAllTrainers();
        if (trainers.isEmpty()) {
            System.out.println("Nenhum personal cadastrado.");
            return;
        }
        System.out.printf("%n── Personais (%d) ──%n", trainers.size());
        for (Trainer t : trainers) {
            System.out.printf("  [%d] %-30s  %s%n", t.getId(), t.getName(), t.getSpecialization());
        }
    }

    private static void adminDeletarPersonal() {
        System.out.println("\n── Deletar Personal ───────");
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        if(!dao.personalAlreadyExist(nome)){
            System.out.println("Personal não cadastrado!");
            return;
        }
        dao.deletePersonalTrainer(nome);
    }

    private static void adminDeletarExercicio() {
        List<Exercise> exercicios = dao.getAllExercises();
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado.");
            return;
        }
        System.out.printf("%n── Deletar Exercício ──%n");
        for (int i = 0; i < exercicios.size(); i++) {
            System.out.printf("  [%d] %s (%s)%n",
                i + 1, exercicios.get(i).getName(), exercicios.get(i).getMusclesWorked());
        }
        System.out.print("Escolha (0 = cancelar): ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= exercicios.size()) return;

        Exercise ex = exercicios.get(idx);
        // Bloqueia deleção se o exercício está em uso em templates ou fichas de alunos
        if (dao.exerciseIsInUse(ex.getId())) {
            System.out.println("Não é possível deletar: exercício está em uso em templates ou fichas de alunos.");
            return;
        }
        if (!lerSimNao("Deletar '" + ex.getName() + "'? (s/n): ")) return;
        dao.deleteExercice(ex.getId());
        System.out.println("Exercício deletado.");
    }

    private static void adminDeletarTemplate() {
        // Mostra apenas splits default (admin) — não afeta splits customizados de alunos
        List<WorkoutSplit> splits = dao.getAllDefaultSplits();
        if (splits.isEmpty()) {
            System.out.println("Nenhum template cadastrado.");
            return;
        }
        System.out.printf("%n── Deletar Template de Treino ──%n");
        for (int i = 0; i < splits.size(); i++) {
            System.out.printf("  [%d] %s — %s%n",
                i + 1, splits.get(i).getSplitName(), splits.get(i).getDescription());
        }
        System.out.print("Escolha (0 = cancelar): ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= splits.size()) return;

        WorkoutSplit split = splits.get(idx);
        // Avisa se algum aluno já usou este split (histórico preservado, mas split some do catálogo)
        if (dao.splitIsInUse(split.getId())) {
            System.out.println("Atenção: alunos já usaram este split. Seus programas históricos serão mantidos,");
            System.out.println("mas o split sumirá do catálogo de novos programas.");
        }
        if (!lerSimNao("Deletar split '" + split.getSplitName() + "' e todos os seus treinos? (s/n): ")) return;
        dao.deleteWorkoutSplit(split.getId());
        System.out.println("Template deletado.");
    }

    private static void adminDeletarPlano() {
        List<Plan> planos = dao.getAllPlans();
        if (planos.isEmpty()) {
            System.out.println("Nenhum plano cadastrado.");
            return;
        }
        System.out.printf("%n── Deletar Plano ──%n");
        for (int i = 0; i < planos.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, planos.get(i).getPlanName());
        }
        System.out.print("Escolha (0 = cancelar): ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= planos.size()) return;

        Plan plano = planos.get(idx);
        // Não permite deletar se há alunos vinculados — histórico de assinaturas seria perdido
        if (dao.planIsInUse(plano.getId())) {
            System.out.println("Não é possível deletar: existem alunos vinculados a este plano.");
            return;
        }
        if (!lerSimNao("Deletar plano '" + plano.getPlanName() + "'? (s/n): ")) return;
        dao.deletePlan(plano.getId());
        System.out.println("Plano deletado.");
    }

    private static void adminAdicionarTemplate() {
        System.out.println("\n── Adicionar Template de Treino ──");

        //escolher uma frequencia para escolher um split que encaixe nos dias da semana desta.
        List<TrainingFrequency> frequencias = dao.getAllFrequencies();
        TrainingFrequency freq;
        if (!frequencias.isEmpty()) {
            System.out.println("Frequências existentes:");
            for (int i = 0; i < frequencias.size(); i++) {
                System.out.printf("  [%d] %dx/semana%n", i + 1, frequencias.get(i).getDaysPerWeek());
            }
            System.out.println("  [N] Criar nova frequência");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            if (op.equalsIgnoreCase("N")) {
                freq = criarNovaFrequencia();
            } else {
                int idx = Integer.parseInt(op) - 1;
                freq = frequencias.get(idx);
            }
        } else {
            System.out.println("Nenhuma frequência cadastrada. Criando nova.");
            freq = criarNovaFrequencia();
        }

        // Passo 2: split
        System.out.print("\nNome da divisão (ex: ABC, Upper/Lower): ");
        String splitNome = sc.nextLine().trim();
        System.out.print("Descrição: ");
        String splitDesc = sc.nextLine().trim();
        WorkoutSplit split = new WorkoutSplit(freq.getId(), splitNome, splitDesc);
        int splitId = dao.addWorkoutSplit(split);
        split.setId(splitId);
        System.out.println("Divisão '" + splitNome + "' criada!");

        // Passo 3: dias
        List<Exercise> exercicios = dao.getAllExercises();
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado. Adicione exercícios primeiro.");
            return;
        }
        int numDias = freq.getDaysPerWeek();
        System.out.printf("%nEste split terá %d dia(s) de treino.%n", numDias);

        for (int dia = 1; dia <= numDias; dia++) {
            // criar os treinos individuais, tipo o treino A do split ABC.
            System.out.printf("\n── Dia %d ──%n", dia);
            System.out.print("Nome do treino (ex: Treino A): ");
            String nomeTemplate = sc.nextLine().trim();

            // Criação dos Templates. Ex: Treino A do Split ABC, ou Upper do treino Upper/Lower
            WorkoutTemplate template = new WorkoutTemplate(split.getId(), nomeTemplate, dia);
            int templateId = dao.buildWorkoutTemplate(template, split);
            template.setId(templateId);

            System.out.print("Quantos exercícios neste   treino? ");
            int numEx = lerInt();

            Set<Integer> usados = new HashSet<>();

            System.out.println("Exercícios disponíveis:");
            for (int i = 0; i < exercicios.size(); i++) {
                System.out.printf("  [%d] %s (%s)%n",
                    i + 1, exercicios.get(i).getName(), exercicios.get(i).getMusclesWorked());
            }

            // Inserção dos exercicios nos Templates. Ex: supino, crucifixo, voador no treino A do ABC.
            for (int ordem = 1; ordem <= numEx; ordem++) {
                System.out.printf("  Exercício %d — número da lista: ", ordem);
                int idxEx = lerInt() - 1;

                if (idxEx < 0 || idxEx >= exercicios.size()) {
                    System.out.println("  Opção inválida, tente novamente.");
                    ordem--;
                    continue;
                }

                Exercise ex = exercicios.get(idxEx);

                if (usados.contains(ex.getId())) {
                    System.out.println("  Exercício já inserido neste treino.");
                    ordem--;
                    continue;
                }

                System.out.print("  Séries: ");          int series  = lerInt();
                System.out.print("  Reps mínimo: ");     int repsMin = lerInt();
                System.out.print("  Reps máximo: ");     int repsMax = lerInt();
                System.out.print("  Carga (kg): ");      double carga    = lerDouble();
                System.out.print("  Descanso (min): ");  double descanso = lerDouble();

                WorkoutExerciseTemplate wet = new WorkoutExerciseTemplate(template.getId(), ex.getId(), series, repsMin, repsMax, carga, descanso, ordem);
                dao.addExerciseToTemplate(wet, template, ex);

                usados.add(ex.getId());
            }
        }
        System.out.println("\nTemplate adicionado com sucesso!");
    }

    private static TrainingFrequency criarNovaFrequencia() {
        System.out.print("Dias por semana: ");
        int dias = lerInt();
        // Frequência com esses dias já existe — retorna a existente sem duplicar
        if (dao.frequencyExistsByDays(dias)) {
            System.out.println("  Frequência de " + dias + " dias já existe. Usando a existente.");
            return dao.getFrequencyByDays(dias);
        }
        TrainingFrequency freq = new TrainingFrequency(dias);
        int id = dao.addTrainingFrequency(freq);
        freq.setId(id);
        return freq;
    }

    // ─── MENU ALUNO ──────────────────────────────────────────────────────────────

    private static void menuAluno(Client cliente, ClientPlan plano) {
        boolean no_menu = true;
        while (no_menu) {
            System.out.printf("%n══ OLÁ, %s ═══════════%n", cliente.getFullName().toUpperCase());
            System.out.println("[1] Ver progressão de carga");
            System.out.println("[2] Meus programas de treino");
            System.out.println("[3] Mudar frequência / split");
            System.out.println("[4] Escolher personal");
            System.out.println("[5] Tempo restante no plano");
            System.out.println("[6] Benefícios do meu plano");
            System.out.println("[7] Alterar plano");
            System.out.println("[8] Cancelar matrícula");
            System.out.println("[9] Atualizar Carga de Exercicio");
            System.out.println("[10] Iniciar Treino");
            System.out.println("[0] Sair");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1: alunoVerProgressao(cliente.getCpf()); break;
                case 2: alunoVerProgramas(cliente.getCpf());  break;
                case 3: alunoMudarFrequencia(cliente, plano); break;
                case 4: alunoEscolherPersonal(cliente, plano); break;
                case 5: alunoTempoPlano(plano);               break;
                case 6: alunoVerBeneficios(plano);            break;
                case 7: plano = alunoAlterarPlano(cliente, plano); break;
                case 8:
                    if (alunoCancelarMatricula(cliente)) no_menu = false;
                    break;
                case 9: atualizarPesoExercicio(cliente); break;
                case 10: iniciarTreinoAluno(cliente); break;
                case 0: no_menu = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void alunoVerProgressao(String cpf) {
        List<Exercise> exercicios = dao.getAllExercises();
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado.");
            return;
        }
        System.out.println("\nQual exercício?");
        for (int i = 0; i < exercicios.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, exercicios.get(i).getName());
        }
        System.out.print("Opção: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= exercicios.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        Exercise ex = exercicios.get(idx);
        List<LoadHistory> historico = dao.getLoadHistoryByExercise(cpf, ex.getId());
        if (historico.isEmpty()) {
            System.out.println("Nenhum registro para " + ex.getName() + ".");
            return;
        }
        System.out.printf("%n── Histórico: %s ──%n", ex.getName());
        for (LoadHistory h : historico) {
            System.out.printf("  %s  →  %.1f kg%n",
                h.getRecordedAt().toLocalDate().format(FMT), h.getLoadKg());
        }
        double menor = historico.get(0).getLoadKg();
        double maior = historico.get(historico.size() - 1).getLoadKg();
        System.out.printf("%nMínimo: %.1f kg  |  PR: %.1f kg  |  Evolução: +%.1f kg%n",
            menor, maior, maior - menor);
    }

    private static void alunoVerProgramas(String cpf) {
        List<ClientWorkoutProgram> programas = dao.getClientPrograms(cpf);
        if (programas.isEmpty()) {
            System.out.println("Nenhum programa ativo.");
            return;
        }
        System.out.println("\n── Meus Programas ──");
        for (ClientWorkoutProgram p : programas) {
            String tipo = Boolean.TRUE.equals(p.getIsCustomized()) ? "Customizado" : "Padrão";
            System.out.printf("  [%d] %s (%s)%n", p.getId(), p.getName(), tipo);
        }
    }

    private static void alunoMudarFrequencia(Client cliente, ClientPlan plano) {
        // Mostra apenas frequências que já têm pelo menos um split cadastrado
        List<TrainingFrequency> frequencias = dao.getFrequenciesWithSplits(cliente.getCpf());
        if (frequencias.isEmpty()) {
            System.out.println("Nenhuma frequência com treinos disponível. Contate o admin.");
            return;
        }
        System.out.println("\nFrequências disponíveis:");
        for (int i = 0; i < frequencias.size(); i++) {
            System.out.printf("  [%d] %dx/semana%n", i + 1, frequencias.get(i).getDaysPerWeek());
        }
        System.out.print("Escolha: ");
        int idxFreq = lerInt() - 1;
        if (idxFreq < 0 || idxFreq >= frequencias.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        TrainingFrequency freq = frequencias.get(idxFreq);

        Plan planInfo = dao.getPlanById(plano.getPlanId());
        boolean podeCustomizar = planInfo != null && planInfo.isCanCustomizeWorkout();

        // Básico vê só defaults; normal/premium vê defaults + os próprios splits customizados
        List<WorkoutSplit> splits = podeCustomizar
            ? dao.getSplitsForAluno(freq.getId(), cliente.getCpf())
            : dao.getDefaultSplitsByFrequency(freq.getId());

        if (splits.isEmpty() && !podeCustomizar) {
            System.out.println("Nenhum treino padrão disponível para esta frequência. Contate o admin.");
            return;
        }

        System.out.println("Divisões disponíveis:");
        for (int i = 0; i < splits.size(); i++) {
            String origem = splits.get(i).getClientCpf() == null ? "default" : "meu";
            System.out.printf("  [%d] %s — %s (%s)%n",
                i + 1, splits.get(i).getSplitName(), splits.get(i).getDescription(), origem);
        }
        // Aluno normal/premium pode criar um split novo
        if (podeCustomizar) {
            System.out.println("  [N] Criar novo split customizado");
        }
        System.out.print("Escolha: ");
        String opSplit = sc.nextLine().trim();
        String nomeProg;
        WorkoutSplit split;
        if (podeCustomizar && opSplit.equalsIgnoreCase("N")) {
            split = alunoCriarSplitCustom(cliente, freq);
            if (split == null) return;
            nomeProg = split.getSplitName();
        } else {
            int idxSplit;
            try { idxSplit = Integer.parseInt(opSplit) - 1; }
            catch (NumberFormatException e) { System.out.println("Opção inválida."); return; }
            if (idxSplit < 0 || idxSplit >= splits.size()) {
                System.out.println("Opção inválida.");
                return;
            }
            split = splits.get(idxSplit);
            nomeProg = split.getSplitName();
        }

        // Arquiva programas ativos antes de criar o novo
        for (ClientWorkoutProgram p : dao.getClientPrograms(cliente.getCpf())) {
            dao.deactivateProgram(p.getId());
        }

        dao.createWorkoutProgramFromSplit(split, plano, cliente, nomeProg);
        if (opSplit.equalsIgnoreCase("N")){
            System.out.println("Programa '" + nomeProg + "' criado com sucesso!");
            return;
        } 
        System.out.println("Programa '" + nomeProg + "' selecionado com sucesso!");
    }

    // Cria um split customizado para o aluno (normal/premium).
    // Gera entradas em workout_split, workout_template e workout_exercise_template com o CPF do aluno.
    private static WorkoutSplit alunoCriarSplitCustom(Client cliente, TrainingFrequency freq) {
        List<Exercise> exercicios = dao.getAllExercises();
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado. Contate o admin.");
            return null;
        }

        System.out.print("Nome do seu split: ");
        String nome = sc.nextLine().trim();
        System.out.print("Descrição: ");
        String desc = sc.nextLine().trim();

        WorkoutSplit split = new WorkoutSplit(freq.getId(), cliente.getCpf(), nome, desc);
        int splitId = dao.addWorkoutSplit(split);
        split.setId(splitId);

        int numDias = freq.getDaysPerWeek();
        System.out.printf("Este split terá %d dia(s) de treino.%n", numDias);

        for (int dia = 1; dia <= numDias; dia++) {
            System.out.printf("%n── Dia %d ──%n", dia);
            System.out.print("Nome do treino (ex: Treino A): ");
            String nomeTreino = sc.nextLine().trim();

            WorkoutTemplate template = new WorkoutTemplate(split.getId(), nomeTreino, dia);
            int templateId = dao.buildWorkoutTemplate(template, split);
            template.setId(templateId);

            System.out.print("Quantos exercícios neste treino? ");
            int numEx = lerInt();

            Set<Integer> usados = new HashSet<>();
            System.out.println("Exercícios disponíveis:");
            for (int i = 0; i < exercicios.size(); i++) {
                System.out.printf("  [%d] %s (%s)%n",
                    i + 1, exercicios.get(i).getName(), exercicios.get(i).getMusclesWorked());
            }

            for (int ordem = 1; ordem <= numEx; ordem++) {
                System.out.printf("  Exercício %d — número da lista: ", ordem);
                int idxEx = lerInt() - 1;
                if (idxEx < 0 || idxEx >= exercicios.size()) {
                    System.out.println("  Opção inválida, tente novamente.");
                    ordem--;
                    continue;
                }
                Exercise ex = exercicios.get(idxEx);
                if (usados.contains(ex.getId())) {
                    System.out.println("  Exercício já inserido neste treino.");
                    ordem--;
                    continue;
                }
                System.out.print("  Séries: ");         int series  = lerInt();
                System.out.print("  Reps mínimo: ");    int repsMin = lerInt();
                System.out.print("  Reps máximo: ");    int repsMax = lerInt();
                System.out.print("  Carga (kg): ");     double carga    = lerDouble();
                System.out.print("  Descanso (min): "); double descanso = lerDouble();

                WorkoutExerciseTemplate wet = new WorkoutExerciseTemplate(
                    template.getId(), ex.getId(), series, repsMin, repsMax, carga, descanso, ordem);
                dao.addExerciseToTemplate(wet, template, ex);
                usados.add(ex.getId());
            }
        }
        System.out.println("Split '" + nome + "' criado!");
        return split;
    }

    private static void alunoEscolherPersonal(Client cliente, ClientPlan plano) {
        Plan p = dao.getPlanById(plano.getPlanId());
        if (p == null || !p.isIncludeTrainer()) {
            System.out.println("Seu plano não inclui personal. Considere fazer um upgrade.");
            return;
        }
        List<Trainer> trainers = dao.getAllTrainers();
        if (trainers.isEmpty()) {
            System.out.println("Nenhum personal cadastrado.");
            return;
        }
        System.out.println("\nPersonais disponíveis:");
        for (int i = 0; i < trainers.size(); i++) {
            System.out.printf("  [%d] %s — %s%n",
                i + 1, trainers.get(i).getName(), trainers.get(i).getSpecialization());
        }
        System.out.print("Escolha (0 = remover personal): ");
        int idx = lerInt();
        if (idx == 0) {
            dao.assignTrainer(cliente.getCpf(), null);
            System.out.println("Personal removido.");
        } else if (idx >= 1 && idx <= trainers.size()) {
            Trainer t = trainers.get(idx - 1);
            dao.assignTrainer(cliente.getCpf(), t.getId());
            System.out.println("Personal " + t.getName() + " vinculado!");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    private static void alunoTempoPlano(ClientPlan plano) {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), plano.getEndDate());
        System.out.printf("%nPlano vence em: %s%n", plano.getEndDate().format(FMT));
        if (dias > 0) {
            System.out.printf("Dias restantes: %d dia(s)%n", dias);
        } else {
            System.out.println("Plano vencido! Renove na recepção.");
        }
    }

    private static void alunoVerBeneficios(ClientPlan plano) {
        Plan p = dao.getPlanById(plano.getPlanId());
        if (p == null) return;
        System.out.printf("%n── Plano: %s ──%n", p.getPlanName());
        System.out.println("  ✓ Pode escolher frequência de treino");
        System.out.println((p.isCanCustomizeWorkout() ? "  ✓" : "  ✗") + " Pode criar treinos customizados");
        System.out.println((p.isIncludeTrainer()      ? "  ✓" : "  ✗") + " Inclui personal trainer");
    }

    private static void atualizarPesoExercicio(Client cliente) {
        System.out.println("------ Atualização de Peso ----------");
        List<Exercise> exercicios = dao.getAllExercises();

        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado.");
            return;
        }

        for (int i = 0; i < exercicios.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, exercicios.get(i).getName());
        }
        
        System.out.println("Escolha o índice do exercicio: ");
        int idx = lerInt() - 1;
        Exercise ex = exercicios.get(idx);

        List<LoadHistory> lh = dao.getLoadHistoryByExercise(cliente.getCpf(), ex.getId());

        if(lh.isEmpty()) {
            System.out.printf("Nenhum histórico do exercício %s encontrado", ex.getName());
        }
        else {
            int size = lh.size() - 1;
            LoadHistory pr = lh.get(size);
            System.out.printf("PR atual: %.1f kg%n", pr.getLoadKg());
        }

        System.out.printf("Nova carga para %s (kg): ", ex.getName());
        double novaCarga = lerDouble();

        dao.recordExerciseLoad(new LoadHistory(
            cliente.getCpf(), ex.getId(), novaCarga, java.time.LocalDateTime.now()));
        System.out.printf("Carga atualizada: %.1f kg!%n", novaCarga);

    }

    private static void iniciarTreinoAluno(Client cliente) {
        System.out.println("\n── Iniciar Treino ───────────");
        List<ClientWorkoutProgram> cwp = dao.getClientPrograms(cliente.getCpf());
        if (cwp.isEmpty()) {
            System.out.println("Nenhum programa ativo. Escolha uma frequência primeiro (opção 3).");
            return;
        }

        System.out.println("Seus programas:");
        for (int i = 0; i < cwp.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, cwp.get(i).getName());
        }
        System.out.print("Escolha a divisão: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= cwp.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        List<ClientWorkout> workouts = dao.ClientgetTemplatesBySplit(cwp.get(idx).getId());
        if (workouts.isEmpty()) {
            System.out.println("Nenhum treino encontrado neste programa.");
            return;
        }

        // Exibe cada treino com a data da última sessão — nunca feitos aparecem primeiro
        System.out.println("\nTreinos disponíveis:");
        for (int i = 0; i < workouts.size(); i++) {
            List<WorkoutSession> sessoes = dao.getWorkoutSessionbyWorkout(workouts.get(i));
            if (sessoes.isEmpty()) {
                System.out.printf("  [%d] %s — nunca realizado%n", i + 1, workouts.get(i).getName());
            } else {
                LocalDateTime ultima = sessoes.get(0).getStartedAt();
                System.out.printf("  [%d] %s — último: %s%n",
                    i + 1, workouts.get(i).getName(), ultima.toLocalDate().format(FMT));
            }
        }

        System.out.print("Escolha o treino de hoje: ");
        idx = lerInt() - 1;
        if (idx < 0 || idx >= workouts.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        ClientWorkout treinoEscolhido = workouts.get(idx);

        // Avisa se já foi feito nesta semana (segunda-feira como início)
        List<WorkoutSession> sessoes = dao.getWorkoutSessionbyWorkout(treinoEscolhido);
        if (!sessoes.isEmpty()) {
            LocalDate ultimaData = sessoes.get(0).getStartedAt().toLocalDate();
            LocalDate inicioSemana = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
            if (!ultimaData.isBefore(inicioSemana)) {
                if (!lerSimNao("Treino já realizado nesta semana. Tem certeza? (s/n): ")) return;
            }
        }

        WorkoutSession section = new WorkoutSession(cliente.getCpf(), treinoEscolhido.getId(), LocalDateTime.now(), null);
        int section_id = dao.startWorkoutSession(section, cliente, treinoEscolhido);

        List<ClientWorkoutExercise> exercicios = dao.getExercisesWithDetailsByWorkout(treinoEscolhido.getId());
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado neste treino.");
            dao.finishWorkoutSession(section_id);
            return;
        }

        System.out.println("\n========== TREINO INICIADO ==========");
        int registrados = 0;

        for (ClientWorkoutExercise ex : exercicios) {
            Double cargaAtual = dao.getMaxLoadForExercise(cliente.getCpf(), ex.getExerciseId());

            System.out.printf("%n  %s [%s]%n", ex.getName(), ex.getMusclesWorked());
            if (ex.getRepsMin().equals(ex.getRepsMax())) {
                System.out.printf("  %d séries x %d reps", ex.getSets(), ex.getRepsMin());
            } else {
                System.out.printf("  %d séries x %d-%d reps", ex.getSets(), ex.getRepsMin(), ex.getRepsMax());
            }
            if (ex.getRestMinutes() != null) {
                System.out.printf(" | descanso %.0f min", ex.getRestMinutes());
            }
            System.out.println();

            if (cargaAtual != null) {
                System.out.printf("  Carga atual (PR): %.1f kg%n", cargaAtual);
            } else {
                System.out.println("  Sem carga registrada ainda.");
            }

            if (lerSimNao("Deseja registrar/aumentar a carga? ")) {
                System.out.print("  Nova carga em kg: ");
                try {
                    double novaCarga = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                    dao.recordExerciseLoad(new LoadHistory(cliente.getCpf(), ex.getExerciseId(), novaCarga, LocalDateTime.now()));
                    System.out.printf("  %.1f kg registrado!%n", novaCarga);
                    registrados++;
                } catch (NumberFormatException e) {
                    System.out.println("  Valor inválido, carga não registrada.");
                }
            }
        }

        dao.finishWorkoutSession(section_id);
        System.out.println("\n========= TREINO FINALIZADO =========");
        System.out.printf("Cargas atualizadas: %d/%d exercícios%n", registrados, exercicios.size());
    }

    private static ClientPlan alunoAlterarPlano(Client cliente, ClientPlan planoAtual) {
        List<Plan> planos = dao.getAllPlans();
        if (planos.isEmpty()) {
            System.out.println("Nenhum plano disponível.");
            return planoAtual;
        }
        System.out.println("\nPlanos disponíveis:");
        for (int i = 0; i < planos.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, planos.get(i).getPlanName());
        }
        System.out.print("Escolha o novo plano: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= planos.size()) {
            System.out.println("Opção inválida.");
            return planoAtual;
        }
        Plan novoPlano = planos.get(idx);

        Integer trainerId = null;
        if (novoPlano.isIncludeTrainer()) {
            List<Trainer> trainers = dao.getAllTrainers();
            if (!trainers.isEmpty()) {
                System.out.println("Personais disponíveis:");
                for (int i = 0; i < trainers.size(); i++) {
                    System.out.printf("  [%d] %s%n", i + 1, trainers.get(i).getName());
                }
                System.out.print("Escolha (0 = nenhum): ");
                int idxT = lerInt() - 1;
                if (idxT >= 0 && idxT < trainers.size()) {
                    trainerId = trainers.get(idxT).getId();
                }
            }
        }

        System.out.print("Número do cartão: ");
        String cartao = sc.nextLine().trim();
        System.out.print("Validade do cartão (MM/AA): ");
        String validade = sc.nextLine().trim();

        LocalDate inicio = LocalDate.now();
        LocalDate fim    = inicio.plusDays(30);
        // Frequência não muda ao trocar de plano — o programa de treino permanece ativo
        ClientPlan novoCp = new ClientPlan(
            cliente.getCpf(), novoPlano.getId(),
            inicio, fim, cartao, validade, trainerId);
        dao.upgradePlan(novoCp, cliente);
        System.out.println("Plano alterado para " + novoPlano.getPlanName() + "!");
        return dao.getActivePlan(cliente.getCpf());
    }

    private static boolean alunoCancelarMatricula(Client cliente) {
        if (!lerSimNao("Tem certeza que deseja cancelar sua matrícula? (s/n): ")) return false;
        dao.deactivateClient(cliente.getCpf());
        System.out.println("Matrícula cancelada. Até logo, " + cliente.getFullName() + "!");
        return true;
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────────

    private static int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    private static LocalDate lerData(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(sc.nextLine().trim(), FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use dd/MM/yyyy.");
            }
        }
    }

    private static boolean lerSimNao(String prompt) {
        System.out.print(prompt);
        String r = sc.nextLine().trim().toLowerCase();
        return r.equals("s") || r.equals("sim");
    }
}
// O bug é clássico de referência Java: ex vem da lista exercicios, mas count_exercices é uma segunda busca no banco — são objetos diferentes na memória. Como Exercise não tem equals() sobrescrito, contains(ex) usa comparação por referência (==), que nunca é verdadeira entre duas listas distintas. Resultado: !contains é sempre true → cai sempre no "já inserido".

// A solução é trocar count_exercices por um Set<Integer> de IDs já usados, que usa == em inteiros primitivos (funciona corretamente).

// Causa provável do bug: getFrequenciesWithSplits retornava frequências que tinham splits de outros alunos, mas não splits padrão (admin). O aluno via aquela frequência na lista, selecionava, e o sistema não encontrava nenhum split para ele — mostrando só [N] Criar novo split.
// getFrequenciesWithSplits(clientCpf) — agora filtra client_cpf IS NULL OR client_cpf = ?, garantindo que só apareçam frequências com splits acessíveis ao aluno (padrões ou os próprios)