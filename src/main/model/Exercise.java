package src.main.model;
public class Exercise {
    private Integer id;
    private Integer exerciceNumber;
    private String name;
    private String musclesWorked;

    public Exercise(){}

    public Exercise(Integer en, String name, String mW){
        this.exerciceNumber = en;
        this.name = name;
        this.musclesWorked = mW;
    }

    public Integer getId(){return id;}
    public String getName(){return name; }
    public String getMusclesWorked(){ return musclesWorked;}
    public Integer getExerciseNumber() {return this.exerciceNumber;}

    public void setId(Integer id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setMuscleWorked(String mw){
        this.musclesWorked = mw;
    }
    
}
