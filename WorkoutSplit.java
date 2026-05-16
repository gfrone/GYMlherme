public class WorkoutSplit {
    private Integer id;
    private Integer frequencyId;
    // null  = split default criado pelo admin, visível a todos.
    // cpf   = split customizado criado pelo próprio aluno (plano normal/premium).
    private String clientCpf;
    private String splitName;
    private String description;

    public WorkoutSplit() {}

    // Construtor completo — usado ao resgatar do banco
    public WorkoutSplit(Integer id, Integer frequencyId, String clientCpf, String splitName, String description) {
        this.id = id;
        this.frequencyId = frequencyId;
        this.clientCpf = clientCpf;
        this.splitName = splitName;
        this.description = description;
    }

    // Construtor para split default do admin (client_cpf fica null)
    public WorkoutSplit(Integer frequencyId, String splitName, String description) {
        this.frequencyId = frequencyId;
        this.clientCpf = null;
        this.splitName = splitName;
        this.description = description;
    }

    // Construtor para split customizado pelo aluno
    public WorkoutSplit(Integer frequencyId, String clientCpf, String splitName, String description) {
        this.frequencyId = frequencyId;
        this.clientCpf = clientCpf;
        this.splitName = splitName;
        this.description = description;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getFrequencyId() { return frequencyId; }
    public void setFrequencyId(Integer frequencyId) { this.frequencyId = frequencyId; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    public String getSplitName() { return splitName; }
    public void setSplitName(String name) { this.splitName = name; }

    public String getDescription() { return description; }
    public void setDescription(String desc) { this.description = desc; }
}
