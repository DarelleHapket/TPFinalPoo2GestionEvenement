package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import modele.Evenement;
import modele.GestionEvenements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Service de sérialisation/désérialisation JSON pour les événements
 */
public class SerializationService {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        // Configuration pour gérer LocalDateTime automatiquement
        objectMapper.findAndRegisterModules();
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    /**
     * Sauvegarde tous les événements en JSON
     * @param nomFichier Nom du fichier de sauvegarde
     * @return true si succès, false sinon
     */
    public static boolean sauvegarderEvenements(String nomFichier) {
        try {
            GestionEvenements gestion = GestionEvenements.getInstance();
            List<Evenement> evenements = gestion.getTousLesEvenements();
            
            File fichier = new File("data/" + nomFichier);
            // Créer le dossier data s'il n'existe pas
            fichier.getParentFile().mkdirs();
            
            objectMapper.writeValue(fichier, evenements);
            System.out.println("Événements sauvegardés dans : " + fichier.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            System.err.println(" Erreur lors de la sauvegarde : " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Charge les événements depuis un fichier JSON
     * @param nomFichier Nom du fichier à charger
     * @return Liste des événements chargés
     */
    public static List<Evenement> chargerEvenements(String nomFichier) {
        try {
            File fichier = new File("data/" + nomFichier);
            
            if (!fichier.exists()) {
                System.out.println("⚠ Fichier " + nomFichier + " introuvable. Retour d'une liste vide.");
                return new ArrayList<>();
            }
            
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<Evenement> evenements = objectMapper.readValue(fichier, 
                typeFactory.constructCollectionType(List.class, Evenement.class));
            
            System.out.println(" " + evenements.size() + " événements chargés depuis : " + fichier.getAbsolutePath());
            return evenements;
            
        } catch (IOException e) {
            System.err.println(" Erreur lors du chargement : " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Restaure tous les événements dans le gestionnaire
     * @param nomFichier Nom du fichier à restaurer
     * @return Nombre d'événements restaurés
     */
    public static int restaurerEvenements(String nomFichier) {
        List<Evenement> evenements = chargerEvenements(nomFichier);
        GestionEvenements gestion = GestionEvenements.getInstance();
        
        int compteur = 0;
        for (Evenement evenement : evenements) {
            try {
                if (gestion.ajouterEvenement(evenement)) {
                    compteur++;
                }
            } catch (Exception e) {
                System.err.println("⚠ Impossible d'ajouter l'événement " + evenement.getNom() + " : " + e.getMessage());
            }
        }
        
        System.out.println(" " + compteur + " événements restaurés dans le gestionnaire");
        return compteur;
    }
}