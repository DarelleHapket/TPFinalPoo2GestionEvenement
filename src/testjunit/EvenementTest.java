package testjunit;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modele.*;
import exceptions.CapaciteMaximalAtteinteException;
import java.time.LocalDateTime;

/**
 * Tests unitaires pour la classe Evenement et ses sous-classes
 */
public class EvenementTest {
    
    private Concert concert;
    private Conference conference;
    private Participant participant1;
    private Participant participant2;
    
    @BeforeEach
    void setUp() {
        // Préparation avant chaque test
        concert = new Concert("C001", "Jazz Night", 
                LocalDateTime.of(2025, 5, 28, 20, 0), 
                "Yaoundé", 2, "Miles Davis", "Jazz"); // Capacité 2 pour tester
                
        conference = new Conference("CONF001", "IA Conference", 
                LocalDateTime.of(2025, 6, 2, 14, 0), 
                "ENSPY", 3, "Intelligence Artificielle");
                
        participant1 = new Participant("P001", "Alice Martin", "alice@email.com");
        participant2 = new Participant("P002", "Bob Johnson", "bob@email.com");
    }
    
    @Test
    void testCreationConcert() {
        // Test création d'un concert
        assertNotNull(concert);
        assertEquals("C001", concert.getId());
        assertEquals("Jazz Night", concert.getNom());
        assertEquals("Miles Davis", concert.getArtiste());
        assertEquals("Jazz", concert.getGenreMusical());
        assertEquals("Concert", concert.getTypeEvenement());
        assertEquals(0, concert.getNombreParticipants());
        assertFalse(concert.isAnnule());
    }
    
    @Test
    void testCreationConference() {
        // Test création d'une conférence
        assertNotNull(conference);
        assertEquals("CONF001", conference.getId());
        assertEquals("IA Conference", conference.getNom());
        assertEquals("Intelligence Artificielle", conference.getTheme());
        assertEquals("Conférence", conference.getTypeEvenement());
        assertEquals(0, conference.getNombreIntervenants());
        assertFalse(conference.isAnnule());
    }
    
    @Test
    void testAjouterParticipant() throws CapaciteMaximalAtteinteException {
        // Test ajout participant normal
        assertTrue(concert.ajouterParticipant(participant1));
        assertEquals(1, concert.getNombreParticipants());
        assertTrue(concert.getParticipants().contains(participant1));
        
        // Ajouter un deuxième participant
        assertTrue(concert.ajouterParticipant(participant2));
        assertEquals(2, concert.getNombreParticipants());
    }
    
    @Test
    void testAjouterParticipantNull() {
        // Test ajout participant null
        assertThrows(IllegalArgumentException.class, () -> {
            concert.ajouterParticipant(null);
        });
    }
    
    @Test
    void testCapaciteMaximaleAtteinte() throws CapaciteMaximalAtteinteException {
        // Remplir à capacité maximale (2)
        concert.ajouterParticipant(participant1);
        concert.ajouterParticipant(participant2);
        
        // Tenter d'ajouter un 3ème participant
        Participant participant3 = new Participant("P003", "Charlie", "charlie@email.com");
        
        assertThrows(CapaciteMaximalAtteinteException.class, () -> {
            concert.ajouterParticipant(participant3);
        });
    }
    
    @Test
    void testParticipantDejaInscrit() throws CapaciteMaximalAtteinteException {
        // Ajouter participant une première fois
        assertTrue(concert.ajouterParticipant(participant1));
        
        // Tenter d'ajouter le même participant
        assertFalse(concert.ajouterParticipant(participant1));
        assertEquals(1, concert.getNombreParticipants()); // Toujours 1
    }
    
    @Test
    void testSupprimerParticipant() throws CapaciteMaximalAtteinteException {
        // Ajouter puis supprimer
        concert.ajouterParticipant(participant1);
        assertEquals(1, concert.getNombreParticipants());
        
        assertTrue(concert.supprimerParticipant(participant1));
        assertEquals(0, concert.getNombreParticipants());
        assertFalse(concert.getParticipants().contains(participant1));
    }
    
    @Test
    void testAnnulerEvenement() throws CapaciteMaximalAtteinteException {
        // Ajouter participant puis annuler événement
        concert.ajouterParticipant(participant1);
        assertFalse(concert.isAnnule());
        
        concert.annuler();
        assertTrue(concert.isAnnule());
        
        // Tenter d'ajouter participant à événement annulé
        assertThrows(IllegalStateException.class, () -> {
            concert.ajouterParticipant(participant2);
        });
    }
    
    @Test
    void testEvenementComplet() throws CapaciteMaximalAtteinteException {
        assertFalse(concert.estComplet());
        
        concert.ajouterParticipant(participant1);
        assertFalse(concert.estComplet());
        
        concert.ajouterParticipant(participant2);
        assertTrue(concert.estComplet()); // Capacité = 2
    }
    
    @Test
    void testAjouterIntervenant() {
        Intervenant intervenant1 = new Intervenant();
        intervenant1.setNom("Dr. Smith");
        intervenant1.setSpecialite("Machine Learning");
        
        assertTrue(conference.ajouterIntervenant(intervenant1));
        assertEquals(1, conference.getNombreIntervenants());
        
        // Tenter d'ajouter le même intervenant
        assertFalse(conference.ajouterIntervenant(intervenant1));
        assertEquals(1, conference.getNombreIntervenants());
    }
    
    @Test
    void testAjouterIntervenantNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            conference.ajouterIntervenant(null);
        });
    }
}