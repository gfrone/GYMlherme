public class ClientWorkoutExercise {
    private Integer id;
    private Integer exerciseId;
    private String name;
    private String musclesWorked;
    private Integer sets;
    private Integer repsMin;
    private Integer repsMax;
    private Double loadKg;
    private Double restMinutes;
    private Integer sortOrder;

    // Construtor completo — usado ao buscar do banco com JOIN (inclui nome/músculo)
    public ClientWorkoutExercise(Integer id, Integer exerciseId, String name, String musclesWorked,
                                  Integer sets, Integer repsMin, Integer repsMax, Double restMinutes) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.name = name;
        this.musclesWorked = musclesWorked;
        this.sets = sets;
        this.repsMin = repsMin;
        this.repsMax = repsMax;
        this.restMinutes = restMinutes;
    }

    // Construtor para inserção — usado ao montar um exercício antes de salvar
    public ClientWorkoutExercise(Integer sets, Integer repsMin, Integer repsMax,
                                  Double loadKg, Double restMinutes, Integer sortOrder) {
        this.sets = sets;
        this.repsMin = repsMin;
        this.repsMax = repsMax;
        this.loadKg = loadKg;
        this.restMinutes = restMinutes;
        this.sortOrder = sortOrder;
    }

    public Integer getId() { return id; }
    public Integer getExerciseId() { return exerciseId; }
    public String getName() { return name; }
    public String getMusclesWorked() { return musclesWorked; }
    public Integer getSets() { return sets; }
    public Integer getRepsMin() { return repsMin; }
    public Integer getRepsMax() { return repsMax; }
    public Double getLoadKg() { return loadKg; }
    public Double getRestMinutes() { return restMinutes; }
    public Integer getSortOrder() { return sortOrder; }
}
