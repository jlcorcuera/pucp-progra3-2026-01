public class Teacher {

    private String pucpCode;
    private String firstName;
    private String lastName;

    public Teacher(String pucpCode, String firstName, String lastName) {
        this.pucpCode = pucpCode;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getPucpCode() {
        return pucpCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
