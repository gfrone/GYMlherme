public class ClientWorkout {
    private Integer id;
    private String clientCpf;
    private Integer clientPlanId;
    private Integer programId;      // nullable — null = dia avulso, número = pertence a um programa
    private String name;
    private Boolean isCustomized;
    private Integer workoutTemplateId; // nullable — null = do zero, número = copiado de template
    private String createdBy;

    public ClientWorkout() {}

    // Construtor completo (busca do banco)
    public ClientWorkout(Integer id, String clientCpf, Integer clientPlanId, Integer programId, String name, Boolean isCustomized, Integer workoutTemplateId, String createdBy) {
        this.id = id;
        this.clientCpf = clientCpf;
        this.clientPlanId = clientPlanId;
        this.programId = programId;
        this.name = name;
        this.isCustomized = isCustomized;
        this.workoutTemplateId = workoutTemplateId;
        this.createdBy = createdBy;
    }

    // Construtor para inserção (banco gera o ID)
    public ClientWorkout(String clientCpf, Integer clientPlanId, Integer programId, String name, Boolean isCustomized, Integer workoutTemplateId, String createdBy) {
        this.clientCpf = clientCpf;
        this.clientPlanId = clientPlanId;
        this.programId = programId;
        this.name = name;
        this.isCustomized = isCustomized;
        this.workoutTemplateId = workoutTemplateId;
        this.createdBy = createdBy;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    public Integer getClientPlanId() { return clientPlanId; }
    public void setClientPlanId(Integer clientPlanId) { this.clientPlanId = clientPlanId; }

    public Integer getProgramId() { return programId; }
    public void setProgramId(Integer programId) { this.programId = programId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsCustomized() { return isCustomized; }
    public void setIsCustomized(Boolean isCustomized) { this.isCustomized = isCustomized; }

    public Integer getWorkoutTemplateId() { return workoutTemplateId; }
    public void setWorkoutTemplateId(Integer workoutTemplateId) { this.workoutTemplateId = workoutTemplateId; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
