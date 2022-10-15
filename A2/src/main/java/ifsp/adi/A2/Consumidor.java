/**
 * 
 * Membros:
 * 
 * João Victor de Oliveira - GU3013197
 * Rhenan Dias Morais - GU3009254
 * Vicenzo Pizzo - GU3011241
 * 
 */

package ifsp.adi.A2;

import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;


public class Consumidor {
	
	static int contador;

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        
        Connection connection = factory.createConnection("user", "senha"); 
        
        //criação da conexão
        connection.setClientID("loja");
        connection.start();
        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        //Destination topico = (Destination) context.lookup("loja");
        Topic topico = (Topic) context.lookup("pedidos");
        
        MessageConsumer consumer = session.createDurableSubscriber(topico, "Promocao", "DiaDasCriancas=true", false);


        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
            	
            	
                TextMessage textMessage = (TextMessage)message;

                try {
                    System.out.println(textMessage.getText());
                    
                    contador++;

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        });


        new Scanner(System.in).nextLine();
        
        System.out.println("ATENÇÃO - Recebi no total " + Integer.toString(contador) + " mensagens");

        session.close();
        connection.close();
        context.close();
    }
}