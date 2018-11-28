package servicejuridique;

import com.google.gson.Gson;
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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.MessageProducer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sources.siren.Records;
import sources.siren.SirenPOJO;

/**
 *
 * @author marieroca
 */
public class ServiceJuridique {
    private HashMap<Long, PreConvention> conv;

    /**
     * Constructeur du service juridique
     * @param conv map des pré conventions en cours
     * @throws NamingException 
     */
    public ServiceJuridique(HashMap<Long, PreConvention> conv) throws NamingException {
            if (conv == null){
                this.conv = new HashMap();
            }else{
                this.conv = conv;
            }
            //this.conv = new HashMap();
            this.recevoir();      
    }

    /**
     * Getter de la map des pré conventions
     * @return map (id, pré convention)
     */
    public HashMap<Long, PreConvention> getConv() {
        return conv;
    }

    /**
     * Setter de la map des pré conventions
     * @param conv map (id, pré convention)
     */
    public void setConv(HashMap<Long, PreConvention> conv) {
        this.conv = conv;
    }
    
    /**
     * Le main nous a permit de tester les différentes méthodes implémentées
     * @param args the command line arguments
     * @throws javax.naming.NamingException
     * @throws javax.jms.JMSException
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws NamingException, JMSException, ParseException {
        //test receiver
        //ServiceJuridique j = new ServiceJuridique(null);
        //try {
            //j.recevoir();
            //System.out.println(j.aExistenceJuridique("552100554"));
        //} catch (IOException ex) {
            //Logger.getLogger(ServiceJuridique.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
    
    /**
     * Méthode permettant de recevoir les messages JMS emits par le serveur web (préconventions à valider)
     * via le sujet de discussion "PreConvention"
     * @return map (id, pré convention)
     * @throws NamingException 
     */
    public HashMap<Long, PreConvention> recevoir() throws NamingException{
        //System.setProperty
        System.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
        System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
        InitialContext context = new InitialContext();
        
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "jms/__defaultConnectionFactory";
        String destName = "PreConvention";
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
    
    /**
     * Méthode permettant de savoir si la durée du stage est valide (6 mois)
     * @param dDeb date de début de la période
     * @param dFin date de fin de la période
     * @return true si la période est valide (6 mois max)
     */
    public static boolean estBonneDuree(DateConvention dDeb, DateConvention dFin){
        if(DateConvention.nbMois(dDeb.getDate(), dFin.getDate())>6)
            return false;
        else if((DateConvention.nbMois(dDeb.getDate(), dFin.getDate()) == 6 && DateConvention.nbJours(dDeb.getDate(), dFin.getDate()) > 0))
            return false;
        return true;
    }
    
    /**
     * Vérifie si une entreprise a une existence juridique à partir d'un numéro de SIREN
     * @param numSIREN
     * @return true si l'entreprise existe
     * @throws java.io.IOException
     */
    public boolean aExistenceJuridique(String numSIREN) throws IOException{
        //tester si l'entreprise a une existence juridique
        // I/O clavier/écran
        //BufferedReader inClavier = new BufferedReader(new InputStreamReader(System.in));
        //PrintStream outEcran = new PrintStream(System.out);

        // I/O JSON
        Gson gson = new Gson();

        // TOKEN BEARER a récuperer sur INSEE
        String token = "Bearer 87faafeb-b34f-39d4-8cc0-cb9e7a15a8d9";
        // URI Service INSEE
        String uri = "http://data.opendatasoft.com/api/records/1.0/search/?dataset=sirene%40public";

        // a ajuster selon requete voir mode emploi INSEE
        String query = "&lang=fr";

        // SIREN a chercher
        //outEcran.print("Code SIREN a rechercher : ");
        //String siren = inClavier.readLine();
        // Pour info siren = "552100554"; //PEUGEOT.
        String siren = numSIREN;
        
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(uri + "&q=" + siren + query);

        //WebResource webResource = client.resource(uri + siren + query);
        //System.out.println("uri appel: " + uri + "&q=" + siren + query);

        Invocation.Builder invocationBuilder = wt.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        String reponse = response.readEntity(String.class);

        // Convertisseur JSON
        SirenPOJO model = gson.fromJson(reponse, SirenPOJO.class);

        //System.out.println("Résultat: " + response.getStatus());
        Records [] rec = model.getRecords();
        
        //Si rien est trouvé : taille == 0 => retourne faux, sinon vrai (entreprise existante)
        return rec.length != 0; 
        
        //System.out.println("Rien n'a été trouvé. Mauvais SIREN ?");
        //for(int i = 0; i < rec.length ; i++)
        //System.out.println("Raison sociale : " + model.getRecords()[i].getFields().getL1_normalisee() + ", Date création entité : " + model.getRecords()[i].getFields().getDcren() + ", Activité : " + model.getRecords()[i].getFields().getActivite());
        //return false;
    }
    
    /**
     * Vérifie si le contrat d'assurance (responsabilité civile) est valide
     * @param compagnie nom de la compagnie d'assurance
     * @param numAssurance numero de contrat de l'étudiant
     * @param dateDeb date de début de la période de stage
     * @param dateFin date de fin de la période de stage
     * @return true si le contrat est valide/ false si inexistant ou non valide
     */
    public boolean aAssuranceValide(String compagnie, String numAssurance, DateConvention dateDeb, DateConvention dateFin){
        //tester si l'étudiant a bien le contrat numAssurance auprès de compagnie sur la période dateDeb-dateFin
        //Faire API en node qui retourne tout le temps true
        return true;
    }
    
    /**
     * Méthode permettant d'envoyer les messages JMS (préconventions validées ou refusées) du service juridique
     * vers le service des stages via la file ConventionEnCours
     * @param pc préconvention à envoyer
     * @param validite statut de la validité de la préconvention
     * @throws NamingException
     * @throws InterruptedException 
     */
    public void envoyer (PreConvention pc, boolean validite) throws NamingException, InterruptedException{
        System.setProperty("java.naming.factory.initial","com.sun.enterprise.naming.SerialInitContextFactory"); 
        System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        System.setProperty("org.omg.CORBA.ORBInitialPort", "3700"); 
        InitialContext context = new InitialContext();
        
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "jms/__defaultConnectionFactory";
        String destName = "ConventionEnCours";
        Destination dest = null;
        int count = 1;
        Session session = null;
        MessageProducer sender = null;
        
        pc.setValidite(validite);
        
        try {
            // create the JNDI initial context.
            context = new InitialContext();

            // look up the ConnectionFactory
            factory = (ConnectionFactory) context.lookup(factoryName);

            // look up the Destination
            dest = (Destination) context.lookup(destName);

            // create the connection
            connection = factory.createConnection();

            // create the session
            session = connection.createSession(
                false, Session.AUTO_ACKNOWLEDGE);

            // create the sender
            sender = session.createProducer(dest);

            // start the connection, to enable message sends
            connection.start();
            //while(true){
                //for (int i = 0; i < count; i++) {
                    ObjectMessage message = session.createObjectMessage();
                    message.setObject(pc);
                    sender.send(message);
                    
                    //System.out.println("Sent: " + message.getObject() );
                //}
                //Thread.sleep(1000);
          //  }
            
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
    }
    
    
    
}
