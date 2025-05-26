package modele;

import java.util.ArrayList;
import java.util.List;

public class Organisateur extends Participant {
    private List<Evenement> evenementsOrganises;
    
    // Constructeur par défaut
    public Organisateur() {
        super();
        this.evenementsOrganises = new ArrayList<>();
    }
    
    // Constructeur avec paramètres
    public Organisateur(String id, String nom, String email) {
        super(id, nom, email);
        this.evenementsOrganises = new ArrayList<>();
    }
    
    // Méthode pour ajouter un événement organisé
    public boolean ajouterEvenementOrganise(Evenement evenement) {
        if (evenement == null) {
            throw new IllegalArgumentException("L'événement ne peut pas être null");
        }
        
        if (evenementsOrganises.contains(evenement)) {
            return false; // Événement déjà dans la liste
        }
        
        return evenementsOrganises.add(evenement);
    }
    
    // Méthode pour supprimer un événement organisé
    public boolean supprimerEvenementOrganise(Evenement evenement) {
        return evenementsOrganises.remove(evenement);
    }
    
    // Méthode pour obtenir le nombre d'événements organisés
    public int getNombreEvenementsOrganises() {
        return evenementsOrganises.size();
    }
    
    // Méthode pour afficher tous les événements organisés
    public void afficherEvenementsOrganises() {
        System.out.println("=== Événements organisés par " + getNom() + " ===");
        if (evenementsOrganises.isEmpty()) {
            System.out.println("Aucun événement organisé");
        } else {
            for (int i = 0; i < evenementsOrganises.size(); i++) {
                Evenement evt = evenementsOrganises.get(i);
                System.out.println((i + 1) + ". " + evt.getNom() + " (" + evt.getTypeEvenement() + ")");
            }
        }
        System.out.println("");
    }
    
    // Getters et Setters
    public List<Evenement> getEvenementsOrganises() {
        return new ArrayList<>(evenementsOrganises); // Retourne une copie pour protéger l'encapsulation
    }
    
    public void setEvenementsOrganises(List<Evenement> evenementsOrganises) {
        this.evenementsOrganises = new ArrayList<>(evenementsOrganises);
    }
    
    // Redéfinition de toString pour inclure les informations d'organisateur
    @Override
    public String toString() {
        return "Organisateur{" +
                "id='" + getId() + '\'' +
                ", nom='" + getNom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", nombreEvenementsOrganises=" + evenementsOrganises.size() +
                '}';
    }
}