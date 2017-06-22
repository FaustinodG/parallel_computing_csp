import java.io.Serializable;

/**
 * Created by Faustino on 22-6-2017.
 */
public class Student implements Serializable {

    private int studentNumber;
    private double grade;

    public Student(int count, double grade) {
        this.studentNumber = (500600000 + count);
        this.grade = grade;
    }

    public int getStudentNumber(){
        return studentNumber;
    }

    public double getGrade(){
        return grade;
    }
}
