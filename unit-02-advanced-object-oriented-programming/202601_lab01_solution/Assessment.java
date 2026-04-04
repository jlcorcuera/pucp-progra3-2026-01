import java.util.Date;
import java.util.List;

public class Assessment {

    private int durationInMinutes;
    private Date startDate;
    private List<Teacher> teachers;
    private List<AssessmentItem> assessmentItems;
    private double finalScore;

    public Assessment(int durationInMinutes, Date startDate, List<Teacher> teachers, List<AssessmentItem> assessmentItems) {
        this.durationInMinutes = durationInMinutes;
        this.startDate = startDate;
        this.teachers = teachers;
        this.assessmentItems = assessmentItems;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<AssessmentItem> getAssessmentItems() {
        return assessmentItems;
    }
}
