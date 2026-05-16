import java.time.LocalDate;

public class ClientPlan {
    private Integer id;
    private String clientCpf;
    private Integer planId;
    // frequency_id foi removido daqui — a frequência de treino pertence ao programa
    // (client_workout_program), não à assinatura. O aluno escolhe após a matrícula.
    private LocalDate startDate;
    private LocalDate endDate;
    private String cardNumber;
    private String cardExpire;
    private Integer trainerId;

    public ClientPlan() {}

    // Construtor completo — usado ao resgatar do banco (já tem ID)
    public ClientPlan(Integer id, String clientCpf, Integer planId, LocalDate startDate, LocalDate endDate, String cardNumber, String cardExpire, Integer trainerId) {
        this.id = id;
        this.clientCpf = clientCpf;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cardNumber = cardNumber;
        this.cardExpire = cardExpire;
        this.trainerId = trainerId;
    }

    // Construtor para novos cadastros (o banco gera o ID)
    public ClientPlan(String clientCpf, Integer planId, LocalDate startDate, LocalDate endDate, String cardNumber, String cardExpire, Integer trainerId) {
        this.clientCpf = clientCpf;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cardNumber = cardNumber;
        this.cardExpire = cardExpire;
        this.trainerId = trainerId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    public Integer getPlanId() { return planId; }
    public void setPlanId(Integer planId) { this.planId = planId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardExpire() { return cardExpire; }
    public void setCardExpire(String cardExpire) { this.cardExpire = cardExpire; }

    public Integer getTrainerId() { return trainerId; }
    public void setTrainerId(Integer trainerId) { this.trainerId = trainerId; }
}
