package src.main.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import src.main.model.Client;
import src.main.model.ClientPlan;
import src.main.model.ClientWorkout;
import src.main.model.ClientWorkoutExercise;
import src.main.model.ClientWorkoutProgram;
import src.main.model.Exercise;
import src.main.model.LoadHistory;
import src.main.model.Plan;
import src.main.model.Trainer;
import src.main.model.TrainingFrequency;
import src.main.model.WorkoutExerciseTemplate;
import src.main.model.WorkoutSession;
import src.main.model.WorkoutSplit;
import src.main.model.WorkoutTemplate;


public class ClientDAO {
    private Connection connection;

    public ClientDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void addClient(Client client) {
        String SQL = "INSERT INTO client (cpf, full_name, birth_date, is_active) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, client.getCpf());
            stmt.setString(2, client.getFullName());
            stmt.setDate(3, java.sql.Date.valueOf(client.getBirthDate()));
            stmt.setBoolean(4, true);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addExercise(Exercise exercise) {
        String SQL = "INSERT INTO exercise (exercise_number, name, muscles_worked) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, exercise.getExerciseNumber());
            stmt.setString(2, exercise.getName());
            stmt.setString(3, exercise.getMusclesWorked());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerTrainer(Trainer trainer) {
        String SQL = "INSERT INTO trainer (name, specialization) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getSpecialization());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // can_choose_frequency removido — todos os planos podem escolher frequência.
    public void createPlan(Plan plan) {
        String SQL = "INSERT INTO plan (plan_name, can_customize_workout, include_trainer) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, plan.getPlanName());
            stmt.setBoolean(2, plan.isCanCustomizeWorkout());
            stmt.setBoolean(3, plan.isIncludeTrainer());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Frequência agora é só dias/semana. Retorna o ID gerado.
    public int addTrainingFrequency(TrainingFrequency frequency) {
        String SQL = "INSERT INTO training_frequency (days_per_week) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, frequency.getDaysPerWeek());
            stmt.execute();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Falha ao obter ID da frequência criada");
    }

    // client_cpf nullable: null = default do admin, preenchido = custom do aluno.
    public Integer addWorkoutSplit(WorkoutSplit split) {
        String SQL = "INSERT INTO workout_split (frequency_id, client_cpf, split_name, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, split.getFrequencyId());
            if (split.getClientCpf() != null) {
                stmt.setString(2, split.getClientCpf());
            } else {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }
            stmt.setString(3, split.getSplitName());
            stmt.setString(4, split.getDescription());
            stmt.execute();
            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer buildWorkoutTemplate(WorkoutTemplate workoutTemplate, WorkoutSplit split) {
        String SQL = "INSERT INTO workout_template (split_id, name, sort_order) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, split.getId());
            stmt.setString(2, workoutTemplate.getName());
            stmt.setInt(3, workoutTemplate.getSortOrder());
            stmt.execute();
            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addExerciseToTemplate(WorkoutExerciseTemplate exerciseTemplate, WorkoutTemplate workoutTemplate, Exercise exercise) {
        String SQL = "INSERT INTO workout_exercise_template (workout_template_id, exercise_id, sets, reps_min, reps_max, load_kg, resting_minutes, sort_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, workoutTemplate.getId());
            stmt.setInt(2, exercise.getId());
            stmt.setInt(3, exerciseTemplate.getSets());
            stmt.setInt(4, exerciseTemplate.getRepsMin());
            stmt.setInt(5, exerciseTemplate.getRepsMax());
            stmt.setDouble(6, exerciseTemplate.getLoadKg());
            stmt.setDouble(7, exerciseTemplate.getRestingMinutes());
            stmt.setInt(8, exerciseTemplate.getSortOrder());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // frequency_id removido da assinatura — o aluno escolhe a frequência no programa.
    public void subscribeClientToPlan(ClientPlan clientPlan, Client client) {
        String SQL = "INSERT INTO client_plan (client_cpf, plan_id, start_date, end_date, card_number, card_expire, trainer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, client.getCpf());
            stmt.setInt(2, clientPlan.getPlanId());
            stmt.setDate(3, java.sql.Date.valueOf(clientPlan.getStartDate()));
            stmt.setDate(4, java.sql.Date.valueOf(clientPlan.getEndDate()));
            stmt.setString(5, clientPlan.getCardNumber());
            stmt.setString(6, clientPlan.getCardExpire());
            if (clientPlan.getTrainerId() != null) {
                stmt.setInt(7, clientPlan.getTrainerId());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Retorna o plano ativo do cliente (end_date >= hoje), ou null se não houver
    public ClientPlan getActivePlan(String clientCpf) {
        String SQL = "SELECT id, plan_id, start_date, end_date, card_number, card_expire, trainer_id " +
                     "FROM client_plan WHERE client_cpf = ? AND end_date >= CURRENT_DATE ORDER BY start_date DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, clientCpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ClientPlan(
                    rs.getInt("id"),
                    clientCpf,
                    rs.getInt("plan_id"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getString("card_number"),
                    rs.getString("card_expire"),
                    rs.getObject("trainer_id") != null ? rs.getInt("trainer_id") : null
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getMaxLoadForExercise(String clientCpf, Integer exerciseId) {
        String SQL = "SELECT MAX(load_kg) AS max_load FROM load_history WHERE client_cpf = ? AND exercise_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, clientCpf);
            stmt.setInt(2, exerciseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double value = rs.getDouble("max_load");
                return rs.wasNull() ? null : value;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LoadHistory> getLoadHistoryByExercise(String clientCpf, Integer exerciseId) {
        String SQL = "SELECT id, client_cpf, exercise_id, load_kg, recorded_at " +
                     "FROM load_history WHERE client_cpf = ? AND exercise_id = ? " +
                     "ORDER BY recorded_at ASC";
        List<LoadHistory> history = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, clientCpf);
            stmt.setInt(2, exerciseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history.add(new LoadHistory(
                    rs.getInt("id"),
                    rs.getString("client_cpf"),
                    rs.getInt("exercise_id"),
                    rs.getDouble("load_kg"),
                    rs.getTimestamp("recorded_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return history;
    }

    // Todas as frequências cadastradas — usado no admin para criar templates.
    public List<TrainingFrequency> getAllFrequencies() {
        String SQL = "SELECT id, days_per_week FROM training_frequency ORDER BY days_per_week";
        List<TrainingFrequency> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new TrainingFrequency(rs.getInt("id"), rs.getInt("days_per_week")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Frequências com splits acessíveis ao aluno: defaults (admin) ou splits do próprio aluno.
    public List<TrainingFrequency> getFrequenciesWithSplits(String clientCpf) {
        String SQL = "SELECT DISTINCT tf.id, tf.days_per_week " +
                     "FROM training_frequency tf " +
                     "INNER JOIN workout_split ws ON ws.frequency_id = tf.id " +
                     "WHERE ws.client_cpf IS NULL OR ws.client_cpf = ? " +
                     "ORDER BY tf.days_per_week";
        List<TrainingFrequency> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, clientCpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new TrainingFrequency(rs.getInt("id"), rs.getInt("days_per_week")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Splits default (admin) de uma frequência — para aluno básico.
    public List<WorkoutSplit> getDefaultSplitsByFrequency(Integer frequencyId) {
        String SQL = "SELECT id, frequency_id, client_cpf, split_name, description " +
                     "FROM workout_split WHERE frequency_id = ? AND client_cpf IS NULL";
        return querySplits(SQL, frequencyId, null, false);
    }

    // Splits disponíveis para o aluno: defaults globais + splits que ele mesmo criou.
    public List<WorkoutSplit> getSplitsForAluno(Integer frequencyId, String clientCpf) {
        String SQL = "SELECT id, frequency_id, client_cpf, split_name, description " +
                     "FROM workout_split WHERE frequency_id = ? AND (client_cpf IS NULL OR client_cpf = ?)";
        return querySplits(SQL, frequencyId, clientCpf, true);
    }

    // Todos os splits de uma frequência — usado pelo admin.
    public List<WorkoutSplit> getSplitsByFrequency(Integer frequencyId) {
        String SQL = "SELECT id, frequency_id, client_cpf, split_name, description " +
                     "FROM workout_split WHERE frequency_id = ?";
        return querySplits(SQL, frequencyId, null, false);
    }

    private List<WorkoutSplit> querySplits(String sql, Integer frequencyId, String clientCpf, boolean hasSecondParam) {
        List<WorkoutSplit> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, frequencyId);
            if (hasSecondParam) stmt.setString(2, clientCpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new WorkoutSplit(
                    rs.getInt("id"),
                    rs.getInt("frequency_id"),
                    rs.getString("client_cpf"),
                    rs.getString("split_name"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void createWorkoutProgram(ClientWorkoutProgram program, Client client) {
        String SQL = "INSERT INTO client_workout_program (client_cpf, client_plan_id, name, frequency_id, is_customized, workout_split_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, client.getCpf());
            stmt.setInt(2, program.getClientPlanId());
            stmt.setString(3, program.getName());
            stmt.setInt(4, program.getFrequencyId());
            stmt.setBoolean(5, program.getIsCustomized());
            if (program.getWorkoutSplitId() != null) {
                stmt.setInt(6, program.getWorkoutSplitId());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Copia um WorkoutSplit inteiro para o cliente: cria o programa + todos os dias + todos os exercícios
    public void createWorkoutProgramFromSplit(WorkoutSplit split, ClientPlan activePlan, Client client, String programName) {
        String sqlProgram   = "INSERT INTO client_workout_program (client_cpf, client_plan_id, name, frequency_id, is_customized, workout_split_id) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlWorkout   = "INSERT INTO client_workout (client_cpf, client_plan_id, program_id, name, is_customized, workout_template_id, created_by) VALUES (?, ?, ?, ?, false, ?, ?)";
        String sqlTemplates = "SELECT id, name FROM workout_template WHERE split_id = ? ORDER BY sort_order";
        String sqlExercises = "SELECT exercise_id, sets, reps_min, reps_max, load_kg, resting_minutes, sort_order FROM workout_exercise_template WHERE workout_template_id = ?";
        String sqlInsertEx  = "INSERT INTO client_workout_exercise (client_workout_id, exercise_id, sets, reps_min, reps_max, load_kg, rest_minutes, sort_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // is_customized = true se o split pertence ao aluno, false se é um default do admin
        boolean isCustomized = split.getClientCpf() != null;

        try {
            connection.setAutoCommit(false);

            int programId;
            try (PreparedStatement stmt = connection.prepareStatement(sqlProgram, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, client.getCpf());
                stmt.setInt(2, activePlan.getId());
                stmt.setString(3, programName);
                stmt.setInt(4, split.getFrequencyId());
                stmt.setBoolean(5, isCustomized);
                stmt.setInt(6, split.getId());
                stmt.execute();
                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();
                programId = keys.getInt(1);
            }

            try (PreparedStatement stmtTemplates = connection.prepareStatement(sqlTemplates)) {
                stmtTemplates.setInt(1, split.getId());
                ResultSet rsTemplates = stmtTemplates.executeQuery();

                while (rsTemplates.next()) {
                    int templateId = rsTemplates.getInt("id");
                    String dayName = rsTemplates.getString("name");

                    int clientWorkoutId;
                    try (PreparedStatement stmtWorkout = connection.prepareStatement(sqlWorkout, Statement.RETURN_GENERATED_KEYS)) {
                        stmtWorkout.setString(1, client.getCpf());
                        stmtWorkout.setInt(2, activePlan.getId());
                        stmtWorkout.setInt(3, programId);
                        stmtWorkout.setString(4, dayName);
                        stmtWorkout.setInt(5, templateId);
                        stmtWorkout.setString(6, client.getCpf());
                        stmtWorkout.execute();
                        ResultSet keys = stmtWorkout.getGeneratedKeys();
                        keys.next();
                        clientWorkoutId = keys.getInt(1);
                    }

                    try (PreparedStatement stmtEx = connection.prepareStatement(sqlExercises)) {
                        stmtEx.setInt(1, templateId);
                        ResultSet rsEx = stmtEx.executeQuery();
                        try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsertEx)) {
                            while (rsEx.next()) {
                                stmtInsert.setInt(1, clientWorkoutId);
                                stmtInsert.setInt(2, rsEx.getInt("exercise_id"));
                                stmtInsert.setInt(3, rsEx.getInt("sets"));
                                stmtInsert.setInt(4, rsEx.getInt("reps_min"));
                                stmtInsert.setInt(5, rsEx.getInt("reps_max"));
                                stmtInsert.setDouble(6, rsEx.getDouble("load_kg"));
                                stmtInsert.setDouble(7, rsEx.getDouble("resting_minutes"));
                                stmtInsert.setInt(8, rsEx.getInt("sort_order"));
                                stmtInsert.execute();
                            }
                        }
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignora */ }
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
    }

    public void createClientWorkout(ClientWorkout clientWorkout, Client client) {
        String SQL = "INSERT INTO client_workout (client_cpf, client_plan_id, program_id, name, is_customized, workout_template_id, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, client.getCpf());
            stmt.setInt(2, clientWorkout.getClientPlanId());
            if (clientWorkout.getProgramId() != null) {
                stmt.setInt(3, clientWorkout.getProgramId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setString(4, clientWorkout.getName());
            stmt.setBoolean(5, clientWorkout.getIsCustomized() != null && clientWorkout.getIsCustomized());
            if (clientWorkout.getWorkoutTemplateId() != null) {
                stmt.setInt(6, clientWorkout.getWorkoutTemplateId());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.setString(7, clientWorkout.getCreatedBy());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createClientWorkoutFromTemplate(WorkoutTemplate template, ClientPlan activePlan, Client client, String workoutName) {
        String sqlWorkout   = "INSERT INTO client_workout (client_cpf, client_plan_id, program_id, name, is_customized, workout_template_id, created_by) VALUES (?, ?, null, ?, false, ?, ?)";
        String sqlExercises = "SELECT exercise_id, sets, reps_min, reps_max, load_kg, resting_minutes, sort_order FROM workout_exercise_template WHERE workout_template_id = ?";
        String sqlInsertEx  = "INSERT INTO client_workout_exercise (client_workout_id, exercise_id, sets, reps_min, reps_max, load_kg, rest_minutes, sort_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int clientWorkoutId;
            try (PreparedStatement stmt = connection.prepareStatement(sqlWorkout, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, client.getCpf());
                stmt.setInt(2, activePlan.getId());
                stmt.setString(3, workoutName);
                stmt.setInt(4, template.getId());
                stmt.setString(5, client.getCpf());
                stmt.execute();
                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();
                clientWorkoutId = keys.getInt(1);
            }

            try (PreparedStatement stmtEx = connection.prepareStatement(sqlExercises)) {
                stmtEx.setInt(1, template.getId());
                ResultSet rs = stmtEx.executeQuery();
                try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsertEx)) {
                    while (rs.next()) {
                        stmtInsert.setInt(1, clientWorkoutId);
                        stmtInsert.setInt(2, rs.getInt("exercise_id"));
                        stmtInsert.setInt(3, rs.getInt("sets"));
                        stmtInsert.setInt(4, rs.getInt("reps_min"));
                        stmtInsert.setInt(5, rs.getInt("reps_max"));
                        stmtInsert.setDouble(6, rs.getDouble("load_kg"));
                        stmtInsert.setDouble(7, rs.getDouble("resting_minutes"));
                        stmtInsert.setInt(8, rs.getInt("sort_order"));
                        stmtInsert.execute();
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignora */ }
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
    }

    public void addExerciseToClientWorkout(ClientWorkoutExercise exerciseSettings, ClientWorkout clientWorkout, Exercise exercise) {
        String SQL = "INSERT INTO client_workout_exercise (client_workout_id, exercise_id, sets, reps_min, reps_max, load_kg, rest_minutes, sort_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, clientWorkout.getId());
            stmt.setInt(2, exercise.getId());
            stmt.setInt(3, exerciseSettings.getSets());
            stmt.setInt(4, exerciseSettings.getRepsMin());
            stmt.setInt(5, exerciseSettings.getRepsMax());
            stmt.setDouble(6, exerciseSettings.getLoadKg());
            stmt.setDouble(7, exerciseSettings.getRestMinutes());
            stmt.setInt(8, exerciseSettings.getSortOrder());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int startWorkoutSession(WorkoutSession session, Client client, ClientWorkout workout) {
        String SQL = "INSERT INTO workout_session (client_cpf, client_workout_id, started_at, finished_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getCpf());
            stmt.setInt(2, workout.getId());
            if (session.getStartedAt() != null) {
                stmt.setTimestamp(3, java.sql.Timestamp.valueOf(session.getStartedAt()));
            } else {
                stmt.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            if (session.getFinishedAt() != null) {
                stmt.setTimestamp(4, java.sql.Timestamp.valueOf(session.getFinishedAt()));
            } else {
                stmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            stmt.execute();
            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void finishWorkoutSession(Integer sessionId) {
        String SQL = "UPDATE workout_session SET finished_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(2, sessionId);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void recordExerciseLoad(LoadHistory loadHistory) {
        String sqlInsert = "INSERT INTO load_history (client_cpf, exercise_id, load_kg, recorded_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlInsert)) {
            stmt.setString(1, loadHistory.getClientCpf());
            stmt.setInt(2, loadHistory.getExerciseId());
            stmt.setDouble(3, loadHistory.getLoadKg());
            if (loadHistory.getRecordedAt() != null) {
                stmt.setTimestamp(4, java.sql.Timestamp.valueOf(loadHistory.getRecordedAt()));
            } else {
                stmt.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Propaga o novo peso para todas as fichas ativas do aluno com esse exercício
        String sqlUpdate = "UPDATE client_workout_exercise cwe " +
                           "SET load_kg = ? " +
                           "FROM client_workout cw " +
                           "WHERE cwe.client_workout_id = cw.id " +
                           "  AND cw.client_cpf = ? " +
                           "  AND cwe.exercise_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
            stmt.setDouble(1, loadHistory.getLoadKg());
            stmt.setString(2, loadHistory.getClientCpf());
            stmt.setInt(3, loadHistory.getExerciseId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cargas nas fichas do aluno", e);
        }
    }

    // ─── SELECTS ─────────────────────────────────────────────────────────────────

    public List<Client> getAllClients() {
        String SQL = "SELECT cpf, full_name, birth_date, is_active FROM client WHERE is_active = true ORDER BY full_name";
        List<Client> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Client(
                    rs.getString("cpf"),
                    rs.getString("full_name"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Client getClientByCpf(String cpf) {
        String SQL = "SELECT cpf, full_name, birth_date, is_active FROM client WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getString("cpf"),
                    rs.getString("full_name"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getBoolean("is_active")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Exercise> getAllExercises() {
        String SQL = "SELECT id, exercise_number, name, muscles_worked FROM exercise ORDER BY exercise_number";
        List<Exercise> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exercise e = new Exercise(rs.getInt("exercise_number"), rs.getString("name"), rs.getString("muscles_worked"));
                e.setId(rs.getInt("id"));
                list.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Plan> getAllPlans() {
        String SQL = "SELECT id, plan_name, can_customize_workout, include_trainer FROM plan ORDER BY id";
        List<Plan> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Plan(
                    rs.getInt("id"),
                    rs.getString("plan_name"),
                    rs.getBoolean("can_customize_workout"),
                    rs.getBoolean("include_trainer")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Plan getPlanById(Integer planId) {
        String SQL = "SELECT id, plan_name, can_customize_workout, include_trainer FROM plan WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, planId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Plan(
                    rs.getInt("id"),
                    rs.getString("plan_name"),
                    rs.getBoolean("can_customize_workout"),
                    rs.getBoolean("include_trainer")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Trainer> getAllTrainers() {
        String SQL = "SELECT id, name, specialization FROM trainer ORDER BY name";
        List<Trainer> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Trainer t = new Trainer(rs.getString("name"), rs.getString("specialization"));
                t.setId(rs.getInt("id"));
                list.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<ClientWorkoutProgram> getClientPrograms(String clientCpf) {
        String SQL = "SELECT id, client_cpf, client_plan_id, name, frequency_id, is_customized, workout_split_id, is_active " +
                     "FROM client_workout_program WHERE client_cpf = ? AND is_active = true ORDER BY id DESC";
        List<ClientWorkoutProgram> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, clientCpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ClientWorkoutProgram(
                    rs.getInt("id"),
                    rs.getString("client_cpf"),
                    rs.getInt("client_plan_id"),
                    rs.getString("name"),
                    rs.getInt("frequency_id"),
                    rs.getBoolean("is_customized"),
                    rs.getObject("workout_split_id") != null ? rs.getInt("workout_split_id") : null,
                    rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<WorkoutTemplate> getTemplatesBySplit(Integer splitId) {
        String SQL = "SELECT id, split_id, name, sort_order FROM workout_template WHERE split_id = ? ORDER BY sort_order";
        List<WorkoutTemplate> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, splitId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new WorkoutTemplate(
                    rs.getInt("id"),
                    rs.getInt("split_id"),
                    rs.getString("name"),
                    rs.getInt("sort_order")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void assignTrainer(String clientCpf, Integer trainerId) {
        String SQL = "UPDATE client_plan SET trainer_id = ? WHERE client_cpf = ? AND end_date >= CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            if (trainerId != null) {
                stmt.setInt(1, trainerId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, clientCpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ─── SOFT DELETE ─────────────────────────────────────────────────────────────

    public void deactivateProgram(Integer programId) {
        String SQL = "UPDATE client_workout_program SET is_active = false WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, programId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deactivateClient(String clientCpf) {
        String SQL = "UPDATE client SET is_active = false WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, clientCpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ─── HARD DELETE ─────────────────────────────────────────────────────────────

    public void deleteWorkoutProgram(Integer programId) {
        String sqlSessions  = "DELETE FROM workout_session WHERE client_workout_id IN (SELECT id FROM client_workout WHERE program_id = ?)";
        String sqlExercises = "DELETE FROM client_workout_exercise WHERE client_workout_id IN (SELECT id FROM client_workout WHERE program_id = ?)";
        String sqlWorkouts  = "DELETE FROM client_workout WHERE program_id = ?";
        String sqlProgram   = "DELETE FROM client_workout_program WHERE id = ?";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement s1 = connection.prepareStatement(sqlSessions)) {
                s1.setInt(1, programId); s1.executeUpdate();
            }
            try (PreparedStatement s2 = connection.prepareStatement(sqlExercises)) {
                s2.setInt(1, programId); s2.executeUpdate();
            }
            try (PreparedStatement s3 = connection.prepareStatement(sqlWorkouts)) {
                s3.setInt(1, programId); s3.executeUpdate();
            }
            try (PreparedStatement s4 = connection.prepareStatement(sqlProgram)) {
                s4.setInt(1, programId); s4.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignora */ }
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
    }

    public void deletePersonalTrainer(String name){
        String SQL = "DELETE FROM trainer WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteExercice(int id) {
        String SQL = "DELETE FROM exercise WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ─── ATUALIZAÇÃO DE PLANO ────────────────────────────────────────────────────

    public void upgradePlan(ClientPlan newPlan, Client client) {
        // CURRENT_DATE - 1 (ontem) e não CURRENT_DATE: getActivePlan filtra end_date >= hoje,
        // então encerrar com hoje deixaria o plano antigo ainda visível no mesmo dia da troca.
        // aqui dava erro pq ainda continuavam filtrando planos passados, com esta mudança, serão lidos somente os planos validos
        String sqlEnd    = "UPDATE client_plan SET end_date = CURRENT_DATE - 1 " +
                           "WHERE client_cpf = ? AND end_date >= CURRENT_DATE";
        String sqlInsert = "INSERT INTO client_plan (client_cpf, plan_id, start_date, end_date, card_number, card_expire, trainer_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(sqlEnd)) {
                stmt.setString(1, client.getCpf());
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(sqlInsert)) {
                stmt.setString(1, client.getCpf());
                stmt.setInt(2, newPlan.getPlanId());
                stmt.setDate(3, java.sql.Date.valueOf(newPlan.getStartDate()));
                stmt.setDate(4, java.sql.Date.valueOf(newPlan.getEndDate()));
                stmt.setString(5, newPlan.getCardNumber());
                stmt.setString(6, newPlan.getCardExpire());
                if (newPlan.getTrainerId() != null) {
                    stmt.setInt(7, newPlan.getTrainerId());
                } else {
                    stmt.setNull(7, java.sql.Types.INTEGER);
                }
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignora */ }
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
    }

    // ─── VERIFICAÇÕES ────────────────────────────────────────────────────────────

    public boolean exerciseExists(int numero) {
        String SQL = "SELECT 1 FROM exercise WHERE exercise_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, numero);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Existplan(String plan_name) {
        String SQL = "SELECT 1 FROM plan WHERE plan_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, plan_name);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Para splits default (admin), verifica globalmente. Para splits do aluno, verifica por CPF.
    public boolean SplitAlreadyExist(String splitName, String clientCpf) {
        String SQL = clientCpf == null
            ? "SELECT 1 FROM workout_split WHERE split_name = ? AND client_cpf IS NULL"
            : "SELECT 1 FROM workout_split WHERE split_name = ? AND client_cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, splitName);
            if (clientCpf != null) stmt.setString(2, clientCpf);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean frequencyExistsByDays(int days) {
        String SQL = "SELECT 1 FROM training_frequency WHERE days_per_week = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, days);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TrainingFrequency getFrequencyByDays(int days) {
        String SQL = "SELECT id, days_per_week FROM training_frequency WHERE days_per_week = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new TrainingFrequency(rs.getInt("id"), rs.getInt("days_per_week"));
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean workoutTemplateAlreadyExist(String workoutTemplateName, int splitId) {
        String SQL = "SELECT 1 FROM workout_template WHERE name = ? AND split_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, workoutTemplateName);
            stmt.setInt(2, splitId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean clientExists(String cpf) {
        String SQL = "SELECT 1 FROM client WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, cpf);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exerciseInTemplateExists(int templateId, int exerciseId) {
        String SQL = "SELECT 1 FROM workout_exercise_template WHERE workout_template_id = ? AND exercise_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, templateId);
            stmt.setInt(2, exerciseId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exerciseInClientWorkoutExists(int clientWorkoutId, int exerciseId) {
        String SQL = "SELECT 1 FROM client_workout_exercise WHERE client_workout_id = ? AND exercise_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, clientWorkoutId);
            stmt.setInt(2, exerciseId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean personalAlreadyExist(String name) {
        String SQL = "SELECT 1 FROM trainer WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, name);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getExerciceName(int numero) {
        String SQL = "SELECT name FROM exercise WHERE exercise_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Retorna true se o exercício está referenciado em algum template ou ficha de aluno.
    // Usado para bloquear deleção de exercícios em uso.
    public boolean exerciseIsInUse(int exerciseId) {
        String sqlTemplate = "SELECT 1 FROM workout_exercise_template WHERE exercise_id = ? LIMIT 1";
        String sqlClient   = "SELECT 1 FROM client_workout_exercise WHERE exercise_id = ? LIMIT 1";
        try {
            try (PreparedStatement stmt = connection.prepareStatement(sqlTemplate)) {
                stmt.setInt(1, exerciseId);
                if (stmt.executeQuery().next()) return true;
            }
            try (PreparedStatement stmt = connection.prepareStatement(sqlClient)) {
                stmt.setInt(1, exerciseId);
                if (stmt.executeQuery().next()) return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Retorna true se algum programa de aluno (ativo ou arquivado) referencia este split.
    public boolean splitIsInUse(int splitId) {
        String SQL = "SELECT 1 FROM client_workout_program WHERE workout_split_id = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, splitId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Deleta um split (default ou do aluno) e toda sua hierarquia em cascata.
    // Ordem: template_exercises → templates → split
    public void deleteWorkoutSplit(int splitId) {
        String sqlEx       = "DELETE FROM workout_exercise_template WHERE workout_template_id IN (SELECT id FROM workout_template WHERE split_id = ?)";
        String sqlTemplates = "DELETE FROM workout_template WHERE split_id = ?";
        String sqlSplit    = "DELETE FROM workout_split WHERE id = ?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement s = connection.prepareStatement(sqlEx)) {
                s.setInt(1, splitId); s.executeUpdate();
            }
            try (PreparedStatement s = connection.prepareStatement(sqlTemplates)) {
                s.setInt(1, splitId); s.executeUpdate();
            }
            try (PreparedStatement s = connection.prepareStatement(sqlSplit)) {
                s.setInt(1, splitId); s.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignora */ }
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
    }

    // Retorna true se existe algum client_plan (ativo ou histórico) usando este plano.
    public boolean planIsInUse(int planId) {
        String SQL = "SELECT 1 FROM client_plan WHERE plan_id = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, planId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePlan(int planId) {
        String SQL = "DELETE FROM plan WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, planId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Exercise getExerciseById(int id) {
        String SQL = "SELECT id, exercise_number, name, muscles_worked FROM exercise WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Exercise e = new Exercise(rs.getInt("exercise_number"), rs.getString("name"), rs.getString("muscles_worked"));
                e.setId(rs.getInt("id"));
                return e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<WorkoutSplit> getAllDefaultSplits() {
        String SQL = "SELECT ws.id, ws.frequency_id, ws.client_cpf, ws.split_name, ws.description, tf.days_per_week " +
                     "FROM workout_split ws JOIN training_frequency tf ON tf.id = ws.frequency_id " +
                     "WHERE ws.client_cpf IS NULL ORDER BY tf.days_per_week, ws.split_name";
        List<WorkoutSplit> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new WorkoutSplit(
                    rs.getInt("id"), rs.getInt("frequency_id"),
                    null, rs.getString("split_name"), rs.getString("description")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // seleção dos templates a partir do clientWorkoutProgram
    public List<ClientWorkout> ClientgetTemplatesBySplit(int programId) {
        String SQL = "SELECT id, client_cpf, client_plan_id, program_id, name, " +
                 "is_customized, workout_template_id, created_by " +
                 "FROM client_workout WHERE program_id = ? ORDER BY id";

        List<ClientWorkout> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, programId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ClientWorkout(
                    rs.getInt("id"),
                    rs.getString("client_cpf"),
                    rs.getInt("client_plan_id"),
                    rs.getInt("program_id"),
                    rs.getString("name"),
                    rs.getBoolean("is_customized"),
                    rs.getObject("workout_template_id") != null ? rs.getInt("workout_template_id") : null,
                    rs.getString("created_by")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    public List<WorkoutSession> getWorkoutSessionbyWorkout(ClientWorkout work) {
        String SQL = "SELECT id, client_cpf, client_workout_id, started_at, finished_at " +
                 "FROM workout_session WHERE client_workout_id = ? ORDER BY started_at DESC";

        List<WorkoutSession> sessions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, work.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(new WorkoutSession(
                    rs.getInt("id"),
                    rs.getString("client_cpf"),
                    rs.getInt("client_workout_id"),
                    rs.getTimestamp("started_at").toLocalDateTime(),
                    rs.getObject("finished_at") != null ? rs.getTimestamp("finished_at").toLocalDateTime() : null));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sessions;
    }

    public List<Exercise> getExerciceByClientWorkout(int clientWorkoutId) {
        String SQL = "SELECT e.id, e.exercise_number, e.name, e.muscles_worked " +
                    "FROM exercise e " +
                    "JOIN client_workout_exercise cwe ON cwe.exercise_id = e.id " +
                    "WHERE cwe.client_workout_id = ? " +
                    "ORDER BY cwe.sort_order";
        List<Exercise> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, clientWorkoutId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exercise e = new Exercise(rs.getInt("exercise_number"), rs.getString("name"), rs.getString("muscles_worked"));
                e.setId(rs.getInt("id"));
                list.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<ClientWorkoutExercise> getExercisesWithDetailsByWorkout(int clientWorkoutId) {
        String SQL = "SELECT cwe.id, cwe.exercise_id, e.name, e.muscles_worked, " +
                     "cwe.sets, cwe.reps_min, cwe.reps_max, cwe.rest_minutes " +
                     "FROM client_workout_exercise cwe " +
                     "JOIN exercise e ON e.id = cwe.exercise_id " +
                     "WHERE cwe.client_workout_id = ? " +
                     "ORDER BY cwe.sort_order";
        List<ClientWorkoutExercise> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, clientWorkoutId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Double rest = rs.getObject("rest_minutes") != null ? rs.getDouble("rest_minutes") : null;
                list.add(new ClientWorkoutExercise(
                    rs.getInt("id"),
                    rs.getInt("exercise_id"),
                    rs.getString("name"),
                    rs.getString("muscles_worked"),
                    rs.getInt("sets"),
                    rs.getInt("reps_min"),
                    rs.getInt("reps_max"),
                    rest
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //tava faltando essa pra facilita pegar o nome do plano do cliente
    public String getPlanNameByClientSubscription(ClientPlan client_plan) {
        String SQL = "SELECT plan_name FROM plan WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, client_plan.getPlanId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("plan_name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
