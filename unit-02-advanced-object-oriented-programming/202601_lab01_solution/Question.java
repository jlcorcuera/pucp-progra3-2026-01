public abstract class Question implements Printable{
    private int code;
    protected String prompt;

    public Question(int code, String prompt) {
        this.prompt = prompt;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getPrompt() {
        return prompt;
    }
}
