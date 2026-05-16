public class Trainer {
    private Integer id;
    private String name;
    private String specialization;

    public Trainer(){}

    public Trainer(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public String getName(){
        return this.name;
    }
    public String getSpecialization(){
        return this.specialization;
    }
    public Integer getId(){
        return id;
    }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecialization(String spec) { this.specialization = spec; }
}
