package src.main.model;
public class WorkoutExerciseTemplate {
    private Integer id;
    private Integer workoutTemplateId;
    private Integer exerciseId;
    private Integer sets;
    private Integer repsMin;
    private Integer repsMax;
    private Double loadKg; // NUMERIC em SQL geralmente mapeia para Double ou BigDecimal em Java
    private Double restingMinutes;
    private Integer sortOrder;

    public WorkoutExerciseTemplate() {}

    // Construtor completo (com ID, usado ao resgatar do banco)
    public WorkoutExerciseTemplate(Integer id, Integer workoutTemplateId, Integer exerciseId, Integer sets, Integer repsMin, Integer repsMax, Double loadKg, Double restingMinutes, Integer sortOrder) {
        this.id = id;
        this.workoutTemplateId = workoutTemplateId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.repsMin = repsMin;
        this.repsMax = repsMax;
        this.loadKg = loadKg;
        this.restingMinutes = restingMinutes;
        this.sortOrder = sortOrder;
    }

    // Construtor para inserir um novo registro (sem ID)
    public WorkoutExerciseTemplate(Integer workoutTemplateId, Integer exerciseId, Integer sets, Integer repsMin, Integer repsMax, Double loadKg, Double restingMinutes, Integer sortOrder) {
        this.workoutTemplateId = workoutTemplateId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.repsMin = repsMin;
        this.repsMax = repsMax;
        this.loadKg = loadKg;
        this.restingMinutes = restingMinutes;
        this.sortOrder = sortOrder;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getWorkoutTemplateId() { return workoutTemplateId; }
    public void setWorkoutTemplateId(Integer workoutTemplateId) { this.workoutTemplateId = workoutTemplateId; }

    public Integer getExerciseId() { return exerciseId; }
    public void setExerciseId(Integer exerciseId) { this.exerciseId = exerciseId; }

    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }

    public Integer getRepsMin() { return repsMin; }
    public void setRepsMin(Integer repsMin) { this.repsMin = repsMin; }

    public Integer getRepsMax() { return repsMax; }
    public void setRepsMax(Integer repsMax) { this.repsMax = repsMax; }

    public Double getLoadKg() { return loadKg; }
    public void setLoadKg(Double loadKg) { this.loadKg = loadKg; }

    public Double getRestingMinutes() { return restingMinutes; }
    public void setRestingMinutes(Double restingMinutes) { this.restingMinutes = restingMinutes; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
