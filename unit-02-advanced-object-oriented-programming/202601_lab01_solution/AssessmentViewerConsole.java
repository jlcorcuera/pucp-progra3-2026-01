import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class AssessmentViewerConsole {

    private AssessmentViewerConsole() {
    }

    public static void show(Assessment assessment) {
        System.out.println("Inicio de examen ================");
        System.out.println("Duracion: " + assessment.getDurationInMinutes() + " minutos");
        System.out.println("Fecha Hora Inicio: " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(assessment.getStartDate()));
        System.out.println();

        List<AssessmentItem> assessmentItemList = assessment.getAssessmentItems();
        for(int i = 0; i < assessmentItemList.size(); i++) {
            AssessmentItem assessmentItem = assessmentItemList.get(i);
            Question question = assessmentItem.getQuestion();
            System.out.println("Pregunta " + (i + 1) + ") " + question.devolverDatos());
            System.out.println();
        }
    }

}
