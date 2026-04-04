import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        QuestionManager questionManager = new QuestionManager();

        SingleChoiceQuestion sc1 = new SingleChoiceQuestion(1, "1 + 1?", List.of("1", "2", "11", "0"), 2);
        SingleChoiceQuestion sc2 = new SingleChoiceQuestion(2, "2*4?", List.of("24", "2", "8", "16"), 3);
        MultipleChoiceQuestion mc1 = new MultipleChoiceQuestion(3, "is a planet?", List.of("Earth", "Moon", "Europe", "Mars"), List.of(1, 4));

        questionManager.add(sc1);
        questionManager.add(sc2);
        questionManager.add(mc1);

        // 2. Registro de Profesores
        Teacher teacher1 = new Teacher("11111111", "Freddy", "Paz");
        Teacher teacher2 = new Teacher("22222222", "Jose", "Corcuera");

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);

        // 3. Selección de preguntas para el examen y asignación de puntajes
        // Se usa AssessmentItem para vincular la pregunta con su peso específico
        List<Question> questionsSelected = questionManager.selectRandomN(3);
        List<AssessmentItem> questionsForAssessment = new ArrayList<>();

        // Ejemplo: La misma pregunta puede tener distintos puntajes en diferentes contextos
        questionsForAssessment.add(new AssessmentItem(questionsSelected.get(0), 3.0));
        questionsForAssessment.add(new AssessmentItem(questionsSelected.get(1), 2.5));
        questionsForAssessment.add(new AssessmentItem(questionsSelected.get(2), 1.5));

        // 4. Creación del Examen (Assessment) con su metadata
        Assessment assessment = new Assessment(
                120,                // Duración en minutos
                new Date(),         // Fecha y hora de inicio
                teachers,           // Lista de profesores
                questionsForAssessment // Lista de ítems (Pregunta + Puntaje)
        );

        // 5. Ejecución de la evaluación
        AssessmentViewerConsole.show(assessment);
    }
}

