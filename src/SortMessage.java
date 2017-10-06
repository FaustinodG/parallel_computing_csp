import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faustino on 22-6-2017.
 */
public class SortMessage {

    public static void main(String[] args) throws JMSException{

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination producerDestination = session.createQueue(Constants.PRODUCER_QUEUE);
            MessageConsumer consumer = session.createConsumer(producerDestination);
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof ObjectMessage) {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        ArrayList<Student> students = (ArrayList<Student>) objectMessage.getObject();

                        students.remove(students.size()-1);
                        System.out.println("Students received in sorter: " + students.size());
                        for (Student student : students) {
                            System.out.println(student.getStudentNumber() + " - " + student.getGrade());
                        }





                        Destination destination = session.createQueue(Constants.SORTING_QUEUE);
                        MessageProducer producer = session.createProducer(destination);
                        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                        ObjectMessage sortedMessage = session.createObjectMessage();
                        sortedMessage.setObject(students);
                        producer.send(message);

                        connection.close();
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private static void mergeSort(List<Student> students) {
        if (students.size() >= 2) {
            List<Student> left  = students.subList(0, students.size()/2);
            List<Student> right = students.subList(students.size()/2, students.size());
            mergeSort(left);
            mergeSort(right);
            merge(left, right, students);
        }
    }

    public static void merge(List<Student> left, List<Student> right, List<Student> sorted) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (i2 >= right.size() || (i1 < left.size() && left.get(i1).getGrade() < right.get(i2).getGrade())) {
                sorted.set(i, left.get(i1));
                i1++;
            } else {
                sorted.set(i, right.get(i2));
                i2++;
            }
        }
    }



}
