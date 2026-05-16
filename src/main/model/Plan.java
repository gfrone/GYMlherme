package src.main.model;
public class Plan {
    private Integer id;
    private String planName;
    // Todos os planos podem escolher frequência — não existe mais a flag canChooseFrequency.
    // can_customize_workout = false → plano básico (só treinos default).
    // can_customize_workout = true  → plano normal/premium (pode criar splits próprios).
    private boolean canCustomizeWorkout;
    private boolean includeTrainer;

    public Plan() {}

    public Plan(Integer id, String planName, boolean canCustomizeWorkout, boolean includeTrainer) {
        this.id = id;
        this.planName = planName;
        this.canCustomizeWorkout = canCustomizeWorkout;
        this.includeTrainer = includeTrainer;
    }

    public Plan(String planName, boolean canCustomizeWorkout, boolean includeTrainer) {
        this.planName = planName;
        this.canCustomizeWorkout = canCustomizeWorkout;
        this.includeTrainer = includeTrainer;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public boolean isCanCustomizeWorkout() { return canCustomizeWorkout; }
    public void setCanCustomizeWorkout(boolean canCustomizeWorkout) { this.canCustomizeWorkout = canCustomizeWorkout; }

    public boolean isIncludeTrainer() { return includeTrainer; }
    public void setIncludeTrainer(boolean includeTrainer) { this.includeTrainer = includeTrainer; }
}
