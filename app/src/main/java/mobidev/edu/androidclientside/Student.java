package mobidev.edu.androidclientside;

/**
 * Created by courtneyngo on 11/19/15.
 */

public class Student {

    private String firstName;
    private String lastName;
    private int idNumber;

    public Student(){}

    public Student(String firstName, String lastName, int idNumber) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getIdNumber() {
        return idNumber;
    }
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

}

