public class AssessmentItem {

    private Question question;
    private double score;

    public AssessmentItem(Question question, double score) {
        this.question = question;
        this.score = score;
    }

    public Question getQuestion() {
        return question;
    }

    public double getScore() {
        return score;
    }
}
