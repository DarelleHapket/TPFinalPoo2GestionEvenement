package test;



import modele.*;

import utils.SerializationService;
import exceptions.EvenementDejaExistanteException;

import java.time.LocalDateTime;

public class TestSerialization {
    public static void main(String[] args) {
        System.out.println("= TEST DE SÉRIALISATION =\n");
        
        try {
            // Créer quelques événements
            System.out.println("1️ Création d'événements...");
            
            Concert concert = new Concert("C001", "Jazz Night", 
                LocalDateTime.of(2025, 5, 28, 20, 0), 
                "Yaoundé", 100, "Miles Davis", "Jazz");
                
            Conference conf = new Conference("CONF001", "IA et Avenir", 
                LocalDateTime.of(2025, 6, 2, 14, 0), 
                "ENSPY", 150, "Intelligence Artificielle");
            
            // . Les ajouter au gestionnaire
            GestionEvenements gestion = GestionEvenements.getInstance();
            gestion.ajouterEvenement(concert);
            gestion.ajouterEvenement(conf);
            
            System.out.println(" " + gestion.getNombreEvenements() + " événements créés");
            
            // ici je  SAUVEGARDE dans un fichier JSON
            System.out.println("\n2️ Sauvegarde en JSON...");
            boolean succes = SerializationService.sauvegarderEvenements("test_evenements.json");
            
            if (succes) {
                System.out.println(" Sauvegarde réussie !");
                
                // . Vider le gestionnaire
                System.out.println("\n3️ Simulation fermeture programme...");
                gestion.viderTousLesEvenements();
                System.out.println("Gestionnaire vidé : " + gestion.getNombreEvenements() + " événements");
                
                // . RECHARGEment depuis le fichier JSON
                System.out.println("\n4️ Rechargement depuis JSON...");
                int restaures = SerializationService.restaurerEvenements("test_evenements.json");
                System.out.println(" " + restaures + " événements restaurés !");
                
                //  Vérifier que tout est revenu
                System.out.println("\n5️ Vérification :");
                for (Evenement evt : gestion.getTousLesEvenements()) {
                    System.out.println("  - " + evt.getNom() + " (" + evt.getTypeEvenement() + ")");
                }
                
            }
            
        } catch (EvenementDejaExistanteException e) {
            System.out.println(" " + e.getMessage());
        }
        
        System.out.println("\n= FIN DU TEST =");
    }
}
