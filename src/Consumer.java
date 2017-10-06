import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

/**
 * Created by Faustino on 3-10-2017.
 */
public class Consumer {

    public static void main(String[] args) throws JMSException {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination producerDestination = session.createQueue(Constants.SORTING_QUEUE);
            MessageConsumer consumer = session.createConsumer(producerDestination);
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof ObjectMessage) {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        ArrayList<Student> students = (ArrayList<Student>) objectMessage.getObject();

                        System.out.println("Students received in Consumer: " + students.size());
                        for (Student student : students) {
                            System.out.println(student.getStudentNumber() + " - " + student.getGrade());
                        }

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
}
