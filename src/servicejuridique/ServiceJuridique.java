package servicejuridique;

import java.text.ParseException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import donnes.date.DateConvention;
import donnes.preconvention.PreConvention;

/**
 *
 * @author hugo
 */
public class ServiceJuridique {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NamingException, JMSException, ParseException {
        //Tests date
        /*DateConvention dDeb, dFin;
        /*dDeb = new DateConvention("01/06/2018");
        dFin = new DateConvention("01/07/2018");
        System.out.println("Moins de 6 mois");
        System.out.println(ServiceJuridique.estBonneDuree(dDeb,dFin));
        dDeb = new DateConvention("01/06/2018");
        dFin = new DateConvention("15/07/2018");
        System.out.println("Moins de 6 mois");
        System.out.println(ServiceJuridique.estBonneDuree(dDeb,dFin));
        dDeb = new DateConvention("15/06/2018");
        dFin = new DateConvention("01/07/2018");
        System.out.println("Moins de 6 mois");
        System.out.println(ServiceJuridique.estBonneDuree(dDeb,dFin));
        dDeb = new DateConvention("15/06/2018");
        dFin = new DateConvention("30/06/2018");
        System.out.println("Moins de 6 mois");
        System.out.println(ServiceJuridique.estBonneDuree(dDeb,dFin));
        
        dDeb = new DateConvention("01/06/2018");
        dFin = new DateConvention("01/12/2018");
        System.out.println("6 mois pile");
        System.out.println(ServiceJuridique.estBonneDuree(dDeb,dFin));
        System.out.println("nb mois " );
        System.out.println("nb jours " + ServiceJuridique.estBonneDuree(dDeb,dFin));
        
        /*dDeb = new DateConvention("01/06/2018");
        dFin = new DateConvention("01/06/2019");
        System.out.println("Plus de 6 mois");
        System.out.println(ServiceJuridique.estBonneDuree(dDeb,dFin));*/
        
        //test receiver
        ServiceJuridique j = new ServiceJuridique();
        j.recevoir();
    }
    
    public PreConvention recevoir() throws NamingException{
        //System.setProperty
        System.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
        System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
        InitialContext context = new InitialContext();
        
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "jms/__defaultConnectionFactory";
        String destName = "ConventionEnCours";
        Destination dest = null;
        int count = 100;
        Session session = null;
        MessageConsumer receiver = null;
        
        //Toutes les connections sont gérées par le serveur 
        try {
            // look up the ConnectionFactory
            factory = (ConnectionFactory) context.lookup(factoryName);

            // look up the Destination
            dest = (Destination) context.lookup(destName);

            // create the connection
            connection = factory.createConnection();

            // create the session
            session = connection.createSession(
                    false, Session.AUTO_ACKNOWLEDGE);

            // create the receiver
            //je veux recevoir toutes les conventions
            receiver = session.createConsumer(dest);

            // start the connection, to enable message receipt
            connection.start();

            for (int i = 0; i < count; ++i) {
                Message message = receiver.receive();
                if (message instanceof ObjectMessage) {
                    //on récupère le message
                    ObjectMessage flux = (ObjectMessage) message;
                    //on récupère l'objet dans le message
                    Object preconvention = flux.getObject();
                    //on récupère la pré-conv dans l'objet
                    if (preconvention instanceof PreConvention) {
                        PreConvention convention = (PreConvention) preconvention;
                        
                        //Remplacer ci-dessous et afficher dans l'interface graphique
                        System.out.println("Received: " + convention + " " + message.getStringProperty("date"));
                        return convention;
                        
                    }

                } else if (message != null) {
                    System.out.println("Pas de préconvention reçue");
                }
            }
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        } finally {
            // close the context
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException exception) {
                    exception.printStackTrace();
                }
            }

            // close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static boolean estBonneDuree(DateConvention dDeb, DateConvention dFin){
        if(DateConvention.nbMois(dDeb.getDate(), dFin.getDate())>6)
            return false;
        else if((DateConvention.nbMois(dDeb.getDate(), dFin.getDate()) == 6 && DateConvention.nbJours(dDeb.getDate(), dFin.getDate()) > 0))
            return false;
        return true;
    }
    
    public void envoyer (PreConvention pc) throws NamingException{
        
    }
    
    
    
}
