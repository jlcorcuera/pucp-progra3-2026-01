import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionManager {

    private List<Question> questions = new ArrayList<Question>();

    public void add(Question question) {
        this.questions.add(question);
    }

    public List<Question> selectRandomN(int n) {
        Random rand = new Random();
        List<Question> toReturn = new ArrayList<Question>();
        int i = 0;
        while (i < n) {
            Question item = questions.get(rand.nextInt(questions.size()));
            if (!toReturn.contains(item)) {
                i++;
                toReturn.add(item);
            }
        }
        return toReturn;
    }
}
