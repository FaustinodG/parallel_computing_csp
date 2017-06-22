import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Faustino on 22-6-2017.
 */
public class Producer {

    public static void main(String[] args) throws JMSException {


        ArrayList<Student> students = generateStudents(1000);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?trace=true");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("QueueFromJava1");
        
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        ObjectMessage message = session.createObjectMessage();

        message.setObject(students);
        producer.send(message);

        System.out.println("Sent:");
        printStudents(students);

        session.close();
        connection.close();


    }

    private static ArrayList<Student> generateStudents(int count) {
        ArrayList<Student> studentList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Student student = new Student(i, generateRandomGrade());
            studentList.add(student);
        }
        return studentList;
    }

    private static void printStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println(student.getStudentNumber() + " - " + student.getGrade());
        }
        System.out.println("---");
    }

    private static double generateRandomGrade() {
        int result = 0;
        double grade = 0;
        Random r = new Random();
        int low = 10;
        int high = 101;
        result = r.nextInt(high - low) + low;
        grade = (double) result / 10;
        return grade;
    }
}
