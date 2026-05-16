package src.main.model;
import java.time.LocalDate;

public class Client {
    private String cpf;
    private String fullName;
    private LocalDate birthDate;
    private Boolean isActive; // false = mensalidade vencida ou aluno removido

    public Client() {}

    // Construtor para cadastro (isActive começa true por padrão)
    public Client(String cpf, String fullName, LocalDate birthDate) {
        this.cpf = cpf;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.isActive = true;
    }

    // Construtor completo (busca do banco)
    public Client(String cpf, String fullName, LocalDate birthDate, Boolean isActive) {
        this.cpf = cpf;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.isActive = isActive;
    }

    public String getCpf() { return cpf; }
    public void setCPF(String cpf) { this.cpf = cpf; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
