import java.util.List;

public class SingleChoiceQuestion extends Question {

    private List<String> availableOptions;
    private int correctOption;

    public SingleChoiceQuestion(int code, String prompt, List<String> availableOptions, int correctOption) {
        super(code, prompt);
        this.availableOptions = availableOptions;
        this.correctOption = correctOption;
    }

    public List<String> getAvailableOptions() {
        return availableOptions;
    }

    public String devolverDatos() {
        String toReturn = prompt + "\n";
        for(int i = 0; i < availableOptions.size(); i++) {
            toReturn += (i + 1) + ". " + availableOptions.get(i) + "\n";
        }
        toReturn += "Ingrese su respuesta:";
        return toReturn;
    }

}
