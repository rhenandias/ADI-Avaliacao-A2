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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class Produtor {

    public static void main(String[] args) throws Exception {

    	
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection("user", "senha");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination topico = (Destination) context.lookup("pedidos");
        MessageProducer producer = session.createProducer(topico);
        
        Destination fila = (Destination) context.lookup("erros");
		MessageProducer producerErros = session.createProducer(fila);

    	// Criar Lista de Erros
    	List<String> ListaErros = new ArrayList<>();
    	ListaErros.add("ERR");
    	ListaErros.add("WARN");
    	ListaErros.add("DEBUG");
    	ListaErros.add("");
    	
    	// Criar Lista de Promoções
    	List<String> ListaPromocoes = new ArrayList<>();
    	ListaPromocoes.add("BlacKFriday");
    	ListaPromocoes.add("DiaDosPais");
    	ListaPromocoes.add("DiaDasMaes");
    	ListaPromocoes.add("DiaDasCriancas");
        
    	// Inicializa objeto para gerar números aleatórios
    	Random random = new Random();
    	
    	int contador = 0;
    	int contadorErr = 0;
    	int contadorWarn = 0;
    	int contadorDebug = 0;
    
        for(int i = 0; i < 1000; i++) {
        	// Sorteia um erro da lista de erros
        	String erroSorteado = ListaErros.get(random.nextInt(4));
        	
        	switch(erroSorteado) {
        	case "ERR":
        		Message messageErr = session.createTextMessage("ERR | Mensagem de Erro");
        		producerErros.send(messageErr, DeliveryMode.NON_PERSISTENT,9,300000);
        		contadorErr++;
        		break;
        		
        	case "WARN":
        		Message messageWarn = session.createTextMessage("WARN | Mensagem de Warn");
        		producerErros.send(messageWarn, DeliveryMode.NON_PERSISTENT,1,300000);
        		contadorWarn++;
        		break;
        		
        	case "DEBUG":
        		Message messageDebug = session.createTextMessage("DEBUG | Mensagem de Debug");
        		producerErros.send(messageDebug, DeliveryMode.NON_PERSISTENT,4,300000);
        		contadorDebug++;
        		break;
        		
        	case "":
        		// Cria uma mensagem
        		Message message = session.createTextMessage("<pedido><id>Dados dos Pedido</id></pedido>");
        	
        		// Sorteia promoção
        		String promoSorteada = ListaPromocoes.get(random.nextInt(4));
        		
        		if(promoSorteada.equals(new String("DiaDasCriancas"))) {
        			contador++;
        		}
        		
        		// Configura a flag da mensagem conforme a promoção sorteada
        		message.setBooleanProperty(promoSorteada, true);
        		
        		// Envia a mensagem
        		producer.send(message);
        		
        		break;
        	}
        }
        
        session.close();
        connection.close();
        context.close();
        
        System.out.println("ATENÇÃO - CONSUMIDOR: Enviei " + Integer.toString(contador) + " mensagens");
        System.out.println("ATENÇÃO - CONSUMIDOR: Errs " + Integer.toString(contadorErr));
        System.out.println("ATENÇÃO - CONSUMIDOR: Warns " + Integer.toString(contadorWarn));
        System.out.println("ATENÇÃO - CONSUMIDOR: Debugs " + Integer.toString(contadorDebug));
    }
}