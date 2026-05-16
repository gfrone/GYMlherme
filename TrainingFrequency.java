public class TrainingFrequency {
    private Integer id;
    private int daysPerWeek;

    public TrainingFrequency() {}

    public TrainingFrequency(Integer id, int daysPerWeek) {
        this.id = id;
        this.daysPerWeek = daysPerWeek;
    }

    public TrainingFrequency(int daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public int getDaysPerWeek() { return this.daysPerWeek; }
    public void setDaysPerWeek(int daysPerWeek) { this.daysPerWeek = daysPerWeek; }
}
