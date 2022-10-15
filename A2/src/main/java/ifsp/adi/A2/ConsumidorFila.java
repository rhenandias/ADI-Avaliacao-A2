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
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class ConsumidorFila {

	static int contadorErr;
	static int contadorWarn;
	static int contadorDebug;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		 InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
   
        Connection connection = factory.createConnection("user", "senha"); 

        connection.setClientID("loja"); 
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination fila = (Destination) context.lookup("erros");
		MessageConsumer consumer = session.createConsumer(fila );
		
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {

				TextMessage textMessage = (TextMessage)message;
				try {
					String mensagem = textMessage.getText();
					
					if(mensagem.indexOf("ERR") > -1) contadorErr++; 
					if(mensagem.indexOf("WARN") > -1) contadorWarn++; 
					if(mensagem.indexOf("DEBUG") > -1) contadorDebug++; 
					
					System.out.println(mensagem);
					
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			
		});
		
				
		new Scanner(System.in).nextLine();
		
		System.out.println("ATENÇÃO - Consumidor Fila: Recebi " + Integer.toString(contadorErr) + " erros");
		System.out.println("ATENÇÃO - Consumidor Fila: Recebi " + Integer.toString(contadorWarn) + " warns");
		System.out.println("ATENÇÃO - Consumidor Fila: Recebi " + Integer.toString(contadorDebug) + " debugs");
		
		session.close();
		connection.close();
		context.close();
	}
}