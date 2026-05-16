package src.main.model;
import java.time.LocalDateTime;

public class WorkoutSession {
    private Integer id;
    private String clientCpf;
    private Integer clientWorkoutId;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public WorkoutSession() {}

    // Construtor completo
    public WorkoutSession(Integer id, String clientCpf, Integer clientWorkoutId, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.clientCpf = clientCpf;
        this.clientWorkoutId = clientWorkoutId;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    // Construtor parcial para inserção
    public WorkoutSession(String clientCpf, Integer clientWorkoutId, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.clientCpf = clientCpf;
        this.clientWorkoutId = clientWorkoutId;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    public Integer getClientWorkoutId() { return clientWorkoutId; }
    public void setClientWorkoutId(Integer clientWorkoutId) { this.clientWorkoutId = clientWorkoutId; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
}