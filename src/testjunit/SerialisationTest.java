package testjunit;


	import static org.junit.jupiter.api.Assertions.*;
	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;
	import org.junit.jupiter.api.AfterEach;

	import modele.*;
	import utils.SerializationService;
	import exceptions.EvenementDejaExistanteException;

import java.time.LocalDateTime;
	import java.io.File;

	public class SerialisationTest {
	    
	    private GestionEvenements gestion;
	    private Concert concert;
	    private Conference conference;
	    private String fichierTest = "test_serialization.json";
	    
	    @BeforeEach
	    void setUp() throws EvenementDejaExistanteException {
	        gestion = GestionEvenements.getInstance();
	        gestion.viderTousLesEvenements();
	        
	        concert = new Concert("C001", "Test Concert", 
	                LocalDateTime.of(2025, 5, 28, 20, 0), 
	                "Yaoundé", 100, "Test Artist", "Jazz");
	                
	        conference = new Conference("CONF001", "Test Conference", 
	                LocalDateTime.of(2025, 6, 2, 14, 0), 
	                "ENSPY", 150, "Test Theme");
	    }
	    
	    @AfterEach
	    void cleanup() {
	        // Nettoyer le fichier de test
	        File file = new File("data/" + fichierTest);
	        if (file.exists()) {
	            file.delete();
	        }
	    }
	    
	    @Test
	    void testSauvegarderEvenements() throws EvenementDejaExistanteException {
	        // Ajouter événements
	        gestion.ajouterEvenement(concert);
	        gestion.ajouterEvenement(conference);
	        
	        // Sauvegarder
	        boolean succes = SerializationService.sauvegarderEvenements(fichierTest);
	        assertTrue(succes);
	        
	        // Vérifier que le fichier existe
	        File file = new File("data/" + fichierTest);
	        assertTrue(file.exists());
	        assertTrue(file.length() > 0);
	    }
	    
	    @Test
	    void testChargerEvenements1() throws EvenementDejaExistanteException {
	        // Préparer données
	        gestion.ajouterEvenement(concert);
	        gestion.ajouterEvenement(conference);
	        SerializationService.sauvegarderEvenements(fichierTest);  // ← Cette ligne était coupée
	        
	        // Vider gestionnaire
	        gestion.viderTousLesEvenements();
	        assertEquals(0, gestion.getNombreEvenements());
	        
	        // Restaurer depuis fichier
	        int restaures = SerializationService.restaurerEvenements(fichierTest);
	        assertEquals(2, restaures);
	        assertEquals(2, gestion.getNombreEvenements());
	        
	        // Vérifier données
	        Evenement concertRestaure = gestion.rechercherEvenement("C001");
	        assertNotNull(concertRestaure);
	        assertEquals("Test Concert", concertRestaure.getNom());
	    }
	    @Test
	    void testChargerEvenements() throws EvenementDejaExistanteException {
	        // Préparer données
	        gestion.ajouterEvenement(concert);
	        gestion.ajouterEvenement(conference);
	        SerializationService.sauvegarderEvenements(fichierTest);
	        
	        // Vider gestionnaire
	        gestion.viderTousLesEvenements();
	        assertEquals(0, gestion.getNombreEvenements());
	        
	        // Restaurer depuis fichier
	        int restaures = SerializationService.restaurerEvenements(fichierTest);
	        assertEquals(2, restaures);
	        assertEquals(2, gestion.getNombreEvenements());
	        
	        // Vérifier données
	        Evenement concertRestaure = gestion.rechercherEvenement("C001");
	        assertNotNull(concertRestaure);
	        assertEquals("Test Concert", concertRestaure.getNom());
	    }
	    
	    @Test
	    void testFichierInexistant() {
	        // Tenter de charger un fichier qui n'existe pas
	        int restaures = SerializationService.restaurerEvenements("fichier_inexistant.json");
	        assertEquals(0, restaures);
	    }
	    
	    @Test
	    void testCycleCompletSauvegardeFichier() throws EvenementDejaExistanteException {
	        // Test complet : créer → sauvegarder → vider → restaurer → vérifier
	        
	        // 1. Créer événements avec participants
	        gestion.ajouterEvenement(concert);
	        Participant participant = new Participant("P001", "Test User", "test@email.com");
	        
	        try {
	            concert.ajouterParticipant(participant);
	        } catch (Exception e) {
	            // Ignore pour le test
	        }
	        
	        // 2. Sauvegarder
	        assertTrue(SerializationService.sauvegarderEvenements(fichierTest));
	        
	        // 3. Vider
	        gestion.viderTousLesEvenements();
	        assertEquals(0, gestion.getNombreEvenements());
	        
	        // 4. Restaurer
	        int restaures = SerializationService.restaurerEvenements(fichierTest);
	        assertEquals(1, restaures);
	        
	        // 5. Vérifier intégrité
	        Evenement eventRestaure = gestion.rechercherEvenement("C001");
	        assertNotNull(eventRestaure);
	        assertEquals("Test Concert", eventRestaure.getNom());
	        assertEquals("Yaoundé", eventRestaure.getLieu());
	    }
	}
	        

