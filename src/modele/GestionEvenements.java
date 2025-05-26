package modele;
import exceptions.EvenementDejaExistanteException;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Classe Singleton pour gérer les événements
 * Implémente le pattern Singleton pour s'assurer qu'il n'y a qu'une seule instance
 */
public class GestionEvenements {
    
    // Instance unique (Singleton)
    private static GestionEvenements instance;
    
    // Map contenant tous les événements, indexés par leur ID
    private Map<String, Evenement> evenements;
    
    // Constructeur privé pour empêcher l'instanciation directe
    private GestionEvenements() {
        this.evenements = new HashMap<>();
    }
    
    /**
     * Méthode pour obtenir l'instance unique (Singleton)
     * @return L'instance unique de GestionEvenements
     */
    public static synchronized GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }
    
    /**
     * Ajoute un événement au système
     * @param evenement L'événement à ajouter
     * @return true si l'événement a été ajouté, false s'il existe déjà
     */
    public boolean ajouterEvenement(Evenement evenement) throws EvenementDejaExistanteException {
        if (evenement == null) {
            throw new IllegalArgumentException("L'événement ne peut pas être null");
        }
        
        if (evenement.getId() == null || evenement.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'événement ne peut pas être null ou vide");
        }
        
        // Vérifier si un événement avec cet ID existe déjà
        if (evenements.containsKey(evenement.getId())) {
        	throw new EvenementDejaExistanteException("Un événement avec l'ID '" + evenement.getId() + "' existe déjà.");
        }
        
        evenements.put(evenement.getId(), evenement);
        return true;
    }

    /**
     * Supprime un événement du système
     * @param id L'ID de l'événement à supprimer
     * @return L'événement supprimé, ou null s'il n'existait pas
     */
    public Evenement supprimerEvenement(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID ne peut pas être null ou vide");
        }
        
        return evenements.remove(id);
    }
    
    /**
     * Recherche un événement par son ID
     * @param id L'ID de l'événement recherché
     * @return L'événement trouvé, ou null s'il n'existe pas
     */
    public Evenement rechercherEvenement(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        return evenements.get(id);
    }
    
    /**
     * Recherche des événements par nom (recherche partielle, insensible à la casse)
     * @param nom Le nom ou partie du nom à rechercher
     * @return Une liste des événements correspondants
     */
   
    public List<Evenement> rechercherEvenementParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String nomRecherche = nom.toLowerCase().trim();
        
        return evenements.values().stream()
            .filter(evenement -> evenement.getNom() != null)
            .filter(evenement -> evenement.getNom().toLowerCase().contains(nomRecherche))
            .collect(Collectors.toList());
    }
    
    /**
     * Recherche des événements par type
     * @param typeEvenement Le type d'événement (Concert, Conference, etc.)
     * @return Une liste des événements du type spécifié
     */
    public List<Evenement> rechercherEvenementParType(String typeEvenement) {
        List<Evenement> resultats = new ArrayList<>();
        
        if (typeEvenement == null || typeEvenement.trim().isEmpty()) {
            return resultats;
        }
        
        String typeRecherche = typeEvenement.toLowerCase().trim();
        
        for (Evenement evenement : evenements.values()) {
            if (evenement.getTypeEvenement() != null && 
                evenement.getTypeEvenement().toLowerCase().equals(typeRecherche)) {
                resultats.add(evenement);
            }
        }
        
        return resultats;
    }
    
    /**
     * Obtient tous les événements
     * @return Une liste de tous les événements
     */
    public List<Evenement> getTousLesEvenements() {
        return new ArrayList<>(evenements.values());
    }
    
    /**
     * Obtient le nombre total d'événements
     * @return Le nombre d'événements dans le système
     */
    public int getNombreEvenements() {
        return evenements.size();
    }
    
    /**
     * Vérifie si un événement existe
     * @param id L'ID de l'événement à vérifier
     * @return true si l'événement existe, false sinon
     */
    public boolean evenementExiste(String id) {
        return evenements.containsKey(id);
    }
    
    /**
     * Supprime tous les événements du système
     */
    public void viderTousLesEvenements() {
        evenements.clear();
    }
    
    /**
     * Affiche un résumé de tous les événements
     */
    public void afficherResumeEvenements() {
        System.out.println("=== Résumé des événements ===");
        System.out.println("Nombre total d'événements: " + evenements.size());
        
        if (evenements.isEmpty()) {
            System.out.println("Aucun événement enregistré.");
        } else {
            for (Evenement evenement : evenements.values()) {
                System.out.println("- " + evenement.getNom() + " (" + evenement.getTypeEvenement() + ") - ID: " + evenement.getId());
            }
        }
        System.out.println("============================");
    }
    /**
     * Compte le nombre d'événements par type (Concert, Conférence)
     * @return Map avec le type et le nombre d'événements
     */
    public Map<String, Long> compterEvenementsParType() {
        return evenements.values().stream()
            .collect(Collectors.groupingBy(
                Evenement::getTypeEvenement,
                Collectors.counting()
            ));
    }
    /**
     * Retourne la liste des événements non annulés
     * @return Liste des événements actifs
     */
    public List<Evenement> getEvenementsActifs() {
        return evenements.values().stream()
            .filter(evenement -> !evenement.isAnnule())
            .collect(Collectors.toList());
    }
    /**
     * Retourne les événements avec au moins X participants, triés par nombre de participants
     * @param minParticipants Nombre minimum de participants
     * @return Liste triée des événements
     */
    public List<Evenement> getEvenementsAvecMinParticipants(int minParticipants) {
        return evenements.values().stream()
            .filter(evenement -> evenement.getNombreParticipants() >= minParticipants)
            .sorted((e1, e2) -> Integer.compare(e2.getNombreParticipants(), e1.getNombreParticipants()))
            .collect(Collectors.toList());
    }
    
    /**
     * Trouve l'événement avec le plus de participants
     * @return L'événement le plus populaire ou null si aucun
     */
    public Evenement getEvenementLePlusPopulaire() {
        return evenements.values().stream()
            .max((e1, e2) -> Integer.compare(e1.getNombreParticipants(), e2.getNombreParticipants()))
            .orElse(null);
    }
}