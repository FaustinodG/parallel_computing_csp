import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

/**
 * Created by Faustino on 22-6-2017.
 */
public class SortMessage {

    public static void main(String[] args) {

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
                        System.out.println("Students received: " + students.size());
                        for (Student student : students) {
                            System.out.println(student.getStudentNumber() + " - " + student.getGrade());
                        }
                        System.out.println("---");
                        //TODO SORT

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

//    private static class CustomMessageConsumer implements MessageListener {
//
//        @Override
//        public void onMessage(Message message) {
//            try {
//                if (message instanceof ObjectMessage) {
//                    ObjectMessage objectMessage = (ObjectMessage) message;
//                    ArrayList<Student> students = (ArrayList<Student>) objectMessage.getObject();
//                }
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
