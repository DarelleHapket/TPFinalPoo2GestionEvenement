package testjunit;


	import static org.junit.jupiter.api.Assertions.*;
	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;
	import org.junit.jupiter.api.AfterEach;

	import modele.*;
	import exceptions.CapaciteMaximalAtteinteException;
	import java.time.LocalDateTime;
	import java.io.ByteArrayOutputStream;
	import java.io.PrintStream;

	public class ObserverTest {
	    
	    private Concert concert;
	    private Participant participant1;
	    private Participant participant2;
	    private ByteArrayOutputStream outputStream;
	    private PrintStream originalOut;
	    
	    @BeforeEach
	    void setUp() {
	        concert = new Concert("C001", "Test Concert", 
	                LocalDateTime.of(2025, 5, 28, 20, 0), 
	                "Yaoundé", 100, "Test Artist", "Jazz");
	                
	        participant1 = new Participant("P001", "Alice", "alice@email.com");
	        participant2 = new Participant("P002", "Bob", "bob@email.com");
	        
	        // Capturer la sortie console pour vérifier les notifications
	        outputStream = new ByteArrayOutputStream();
	        originalOut = System.out;
	        System.setOut(new PrintStream(outputStream));
	    }
	    
	    @AfterEach
	    void tearDown() {
	        // Restaurer la sortie console
	        System.setOut(originalOut);
	    }
	    
	    @Test
	    void testNotificationAnnulation() throws CapaciteMaximalAtteinteException {
	        // Ajouter participants (ils deviennent observateurs automatiquement)
	        concert.ajouterParticipant(participant1);
	        concert.ajouterParticipant(participant2);
	        
	        // Vider le buffer de sortie
	        outputStream.reset();
	        
	        // Annuler l'événement (doit déclencher notifications)
	        concert.annuler();
	        
	        String output = outputStream.toString();
	        
	        // Vérifier que les notifications ont été envoyées
	        assertTrue(output.contains("Alice"));
	        assertTrue(output.contains("alice@email.com"));
	        assertTrue(output.contains("ANNULATION"));
	        assertTrue(output.contains("Test Concert"));
	        assertTrue(output.contains("Bob"));
	        assertTrue(output.contains("bob@email.com"));
	    }
	    
	    @Test 
	    void testAjoutParticipantDevientObservateur() throws CapaciteMaximalAtteinteException {
	        // Ajouter participant
	        concert.ajouterParticipant(participant1);
	        
	        // Vérifier qu'il est dans la liste des participants
	        assertTrue(concert.getParticipants().contains(participant1));
	        assertEquals(1, concert.getNombreParticipants());
	        
	        // Tester notification (le participant doit recevoir)
	        outputStream.reset();
	        concert.annuler();
	        
	        String output = outputStream.toString();
	        assertTrue(output.contains("Alice"));
	    }
	    
	    @Test
	    void testNotificationSansParticipants() {
	        // Annuler événement sans participants
	        outputStream.reset();
	        concert.annuler();
	        
	        String output = outputStream.toString();
	        
	        // Pas de notifications envoyées (pas de participants)
	        assertFalse(output.contains(" Notification pour"));
	    }
	    
	    @Test
	    void testEvenementAnnule() throws CapaciteMaximalAtteinteException {
	        concert.ajouterParticipant(participant1);
	        concert.annuler();
	        
	        // Vérifier que l'événement est bien annulé
	        assertTrue(concert.isAnnule());
	        
	        // Tenter d'ajouter participant à événement annulé
	        assertThrows(IllegalStateException.class, () -> {
	            concert.ajouterParticipant(participant2);
	        });
	    }
	}
	
	

