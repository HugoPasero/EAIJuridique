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
import java.util.HashMap;

/**
 *
 * @author hugo
 */
public class ServiceJuridique {
    private HashMap<Long, PreConvention> conv;

    public ServiceJuridique(HashMap<Long, PreConvention> conv) throws NamingException {
            if (conv == null){
                this.conv = new HashMap();
            }else{
                this.conv = conv;
            }
            //this.conv = new HashMap();
            this.recevoir();      
    }

    public HashMap<Long, PreConvention> getConv() {
        return conv;
    }

    public void setConv(HashMap<Long, PreConvention> conv) {
        this.conv = conv;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NamingException, JMSException, ParseException {
        //test receiver
        /*ServiceJuridique j = new ServiceJuridique();
        j.recevoir();*/
    }
    
    public HashMap<Long, PreConvention> recevoir() throws NamingException{
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
        //nombre de messages que l'on reçoit
        int count = 1;
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

            //while(true) {
            for(int i = 0; i < count; ++i){
                Message message = receiver.receive();
                if (message instanceof ObjectMessage) {
                    //on récupère le message
                    ObjectMessage flux = (ObjectMessage) message;
                    //on récupère l'objet dans le message
                    Object preconvention = flux.getObject();
                    //on récupère la pré-conv dans l'objet
                    if (preconvention instanceof PreConvention) {
                        PreConvention convention = (PreConvention) preconvention;
                        
                        if(!conv.containsKey(convention.getId()))
                            conv.put(convention.getId(), convention);
                        //Remplacer ci-dessous et afficher dans l'interface graphique
                        //System.out.println("Received: " + convention + " " + message.getStringProperty("date"));
                        //return convention;
                        
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
        return conv;
    }
    
    public static boolean estBonneDuree(DateConvention dDeb, DateConvention dFin){
        if(DateConvention.nbMois(dDeb.getDate(), dFin.getDate())>6)
            return false;
        else if((DateConvention.nbMois(dDeb.getDate(), dFin.getDate()) == 6 && DateConvention.nbJours(dDeb.getDate(), dFin.getDate()) > 0))
            return false;
        return true;
    }
    
    public boolean aExistenceJuridique(String numSIREN){
        //tester si l'entreprise a une existence juridique
        return false;
    }
    
    public boolean aAssuranceValide(String compagnie, String numAssurance, DateConvention dateDeb, DateConvention dateFin){
        //tester si l'étudiant a bien le contrat numAssurance auprès de compagnie sur la période dateDeb-dateFin
        return false;
    }
    
    public void envoyer (PreConvention pc, boolean validite) throws NamingException{
        
    }
    
    
    
}
