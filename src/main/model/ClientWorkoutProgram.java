package src.main.model;
public class ClientWorkoutProgram {
    private Integer id;
    private String clientCpf;
    private Integer clientPlanId;
    private String name;
    private Integer frequencyId;
    private Boolean isCustomized;
    private Integer workoutSplitId; // nullable — origem se copiado de um split padrão
    private Boolean isActive;       // false = programa arquivado (aluno trocou ou deletou)

    public ClientWorkoutProgram() {}

    // Construtor completo (busca do banco)
    public ClientWorkoutProgram(Integer id, String clientCpf, Integer clientPlanId, String name, Integer frequencyId, Boolean isCustomized, Integer workoutSplitId, Boolean isActive) {
        this.id = id;
        this.clientCpf = clientCpf;
        this.clientPlanId = clientPlanId;
        this.name = name;
        this.frequencyId = frequencyId;
        this.isCustomized = isCustomized;
        this.workoutSplitId = workoutSplitId;
        this.isActive = isActive;
    }

    // Construtor para inserção (banco gera o ID — isActive começa true por padrão)
    public ClientWorkoutProgram(String clientCpf, Integer clientPlanId, String name, Integer frequencyId, Boolean isCustomized, Integer workoutSplitId) {
        this.clientCpf = clientCpf;
        this.clientPlanId = clientPlanId;
        this.name = name;
        this.frequencyId = frequencyId;
        this.isCustomized = isCustomized;
        this.workoutSplitId = workoutSplitId;
        this.isActive = true;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    public Integer getClientPlanId() { return clientPlanId; }
    public void setClientPlanId(Integer clientPlanId) { this.clientPlanId = clientPlanId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getFrequencyId() { return frequencyId; }
    public void setFrequencyId(Integer frequencyId) { this.frequencyId = frequencyId; }

    public Boolean getIsCustomized() { return isCustomized; }
    public void setIsCustomized(Boolean isCustomized) { this.isCustomized = isCustomized; }

    public Integer getWorkoutSplitId() { return workoutSplitId; }
    public void setWorkoutSplitId(Integer workoutSplitId) { this.workoutSplitId = workoutSplitId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
