package testjunit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modele.*;
import exceptions.EvenementDejaExistanteException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GestionEvenementsTest {
    
    private GestionEvenements gestion;
    private Concert concert1;
    private Conference conference1;
    
    @BeforeEach
    void setUp() throws EvenementDejaExistanteException {
        gestion = GestionEvenements.getInstance();
        gestion.viderTousLesEvenements(); // Reset pour chaque test
        
        concert1 = new Concert("C001", "Jazz Night", 
                LocalDateTime.of(2025, 5, 28, 20, 0), 
                "Yaoundé", 100, "Miles Davis", "Jazz");
                
        conference1 = new Conference("CONF001", "IA Conference", 
                LocalDateTime.of(2025, 6, 2, 14, 0), 
                "ENSPY", 150, "Intelligence Artificielle");
    }
    
    @Test
    void testSingleton() {
        // Vérifier que getInstance retourne toujours la même instance
        GestionEvenements gestion1 = GestionEvenements.getInstance();
        GestionEvenements gestion2 = GestionEvenements.getInstance();
        
        assertSame(gestion1, gestion2); // Même référence d'objet
    }
    
    @Test
    void testAjouterEvenement() throws EvenementDejaExistanteException {
        // Test ajout normal
        assertTrue(gestion.ajouterEvenement(concert1));
        assertEquals(1, gestion.getNombreEvenements());
        assertTrue(gestion.evenementExiste("C001"));
    }
    
    @Test
    void testAjouterEvenementDuplique() throws EvenementDejaExistanteException {
        // Ajouter événement une fois
        gestion.ajouterEvenement(concert1);
        
        // Tenter d'ajouter le même ID
        Concert concert2 = new Concert("C001", "Autre Concert", 
                LocalDateTime.now(), "Douala", 50, "Autre", "Rock");
        
        assertThrows(EvenementDejaExistanteException.class, () -> {
            gestion.ajouterEvenement(concert2);
        });
    }
    
    @Test
    void testAjouterEvenementNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestion.ajouterEvenement(null);
        });
    }
    
    @Test
    void testRechercherEvenement() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        
        Evenement trouve = gestion.rechercherEvenement("C001");
        assertNotNull(trouve);
        assertEquals("Jazz Night", trouve.getNom());
        
        // Test ID inexistant
        assertNull(gestion.rechercherEvenement("INEXISTANT"));
    }
    
    @Test
    void testRechercherParNom() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        gestion.ajouterEvenement(conference1);
        
        // Recherche partielle
        List<Evenement> resultats = gestion.rechercherEvenementParNom("jazz");
        assertEquals(1, resultats.size());
        assertEquals("Jazz Night", resultats.get(0).getNom());  // ← Cette ligne était coupée
        
        // Recherche vide
        List<Evenement> vide = gestion.rechercherEvenementParNom("");
        assertEquals(0, vide.size());
    }
        
    @Test
    void testRechercherParNom1() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        gestion.ajouterEvenement(conference1);
        
        // Recherche partielle
        List<Evenement> resultats = gestion.rechercherEvenementParNom("jazz");
        assertEquals(1, resultats.size());
        assertEquals("Jazz Night", resultats.get(0).getNom());
        
        // Recherche vide
        List<Evenement> vide = gestion.rechercherEvenementParNom("");
        assertEquals(0, vide.size());
    }
    
    @Test
    void testRechercherParType() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        gestion.ajouterEvenement(conference1);
        
        List<Evenement> concerts = gestion.rechercherEvenementParType("Concert");
        assertEquals(1, concerts.size());
        
        List<Evenement> conferences = gestion.rechercherEvenementParType("Conférence");
        assertEquals(1, conferences.size());
    }
    
    @Test
    void testCompterEvenementsParType() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        gestion.ajouterEvenement(conference1);
        
        Map<String, Long> comptage = gestion.compterEvenementsParType();
        assertEquals(2, comptage.size());
        assertEquals(1L, comptage.get("Concert"));
        assertEquals(1L, comptage.get("Conférence"));
    }
    
    @Test
    void testGetEvenementsActifs() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        gestion.ajouterEvenement(conference1);
        
        // Annuler un événement
        concert1.annuler();
        
        List<Evenement> actifs = gestion.getEvenementsActifs();
        assertEquals(1, actifs.size());
        assertEquals("IA Conference", actifs.get(0).getNom());
    }
    
    @Test
    void testSupprimerEvenement() throws EvenementDejaExistanteException {
        gestion.ajouterEvenement(concert1);
        assertEquals(1, gestion.getNombreEvenements());
        
        Evenement supprime = gestion.supprimerEvenement("C001");
        assertNotNull(supprime);
        assertEquals(0, gestion.getNombreEvenements());
        assertFalse(gestion.evenementExiste("C001"));
    }
}
        