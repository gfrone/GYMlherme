package src.main.model;
import java.time.LocalDateTime;

public class LoadHistory {
    private Integer id;
    private String clientCpf;
    private Integer exerciseId;
    private Double loadKg;
    private LocalDateTime recordedAt;

    public LoadHistory() {}

    // Construtor completo
    public LoadHistory(Integer id, String clientCpf, Integer exerciseId, Double loadKg, LocalDateTime recordedAt) {
        this.id = id;
        this.clientCpf = clientCpf;
        this.exerciseId = exerciseId;
        this.loadKg = loadKg;
        this.recordedAt = recordedAt;
    }

    // Construtor parcial para inserção
    public LoadHistory(String clientCpf, Integer exerciseId, Double loadKg, LocalDateTime recordedAt) {
        this.clientCpf = clientCpf;
        this.exerciseId = exerciseId;
        this.loadKg = loadKg;
        this.recordedAt = recordedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    public Integer getExerciseId() { return exerciseId; }
    public void setExerciseId(Integer exerciseId) { this.exerciseId = exerciseId; }

    public Double getLoadKg() { return loadKg; }
    public void setLoadKg(Double loadKg) { this.loadKg = loadKg; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}