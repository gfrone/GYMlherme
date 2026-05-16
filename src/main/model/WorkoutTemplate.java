package src.main.model;
public class WorkoutTemplate {
    private Integer id;
    private Integer splitId;
    private String name;
    private Integer sortOrder;

    public WorkoutTemplate() {}

    public WorkoutTemplate(Integer id, Integer id_split, String name, Integer sortOrder) {
        this.id = id;
        this.splitId = id_split;
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public WorkoutTemplate(Integer id_split, String name, Integer sortOrder) {
        this.splitId = id_split;
        this.name = name;
        this.sortOrder = sortOrder;
    } 

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSplitId() { return splitId; }
    public void setSplitID(Integer splitID) { this.splitId = splitID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
