package modele;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import exceptions.CapaciteMaximalAtteinteException;
import patterns.observer.EvenementObservable;  //  Ajouter
import patterns.observer.ParticipantObserver;
//pour pouvoir ajouter un champs automatiquement dans le  fichier .json type:concert
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "typeEvenement"  // ← Utiliser le champ existant !
	)

@JsonSubTypes({
    @JsonSubTypes.Type(value = Concert.class, name = "Concert"),      // ← Déjà bon
    @JsonSubTypes.Type(value = Conference.class, name = "Conférence") // ← "Conférence" avec accent !
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Evenement implements EvenementObservable {
    protected String id;
    protected String nom;
    @JsonIgnore  // Ignore le LocalDateTime original pour Jackson
    protected LocalDateTime date;
    protected String lieu;
    protected int capaciteMax;
    protected List<Participant> participants;
    @JsonIgnore  // Ne pas sérialiser les observateurs
    protected List<ParticipantObserver> observers; 
    protected boolean annule;
    
    // Constructeur par défaut
    public Evenement() {
        this.participants = new ArrayList<>();
        this.annule = false;
        this.observers = new ArrayList<>();
    }
    
    // Constructeur avec paramètres
    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.participants = new ArrayList<>();
        this.annule = false;
        this.observers = new ArrayList<>();
    }
    
    // methode ajouter
    public boolean ajouterParticipant(Participant participant) throws CapaciteMaximalAtteinteException {
        if (participant == null) {
            throw new IllegalArgumentException("Le participant ne peut pas être null");
        }
        
        if (annule) {
            throw new IllegalStateException("Impossible d'ajouter un participant à un événement annulé");
        }
        
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaximalAtteinteException("Capacité maximale atteinte (" + capaciteMax + "). Impossible d'ajouter le participant.");
 
        }
        
        if (participants.contains(participant)) {
            return false; // Participant déjà inscrit
        }
        
        boolean resultat = participants.add(participant);

        // Le participant devient automatiquement observateur
        if (resultat) {
            ajouterObserver(participant); 
        }

        return resultat;
    }
    
    public boolean supprimerParticipant(Participant participant) {
        return participants.remove(participant);
    }
    
    //methode annuler
    public void annuler() {
        this.annule = true;
        // Notifier tous les participants de l'annulation
        notifierObservers("ANNULATION : L'événement '" + nom + "' a été annulé.");
    }
    
    //methode afficher
    protected void afficherDetailsBase() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("=== Détails de l'événement ===");
        System.out.println("ID: " + id);
        System.out.println("Nom: " + nom);
        System.out.println("Date: " + (date != null ? date.format(formatter) : "Non définie"));
        System.out.println("Lieu: " + lieu);
        System.out.println("Capacité maximale: " + capaciteMax);
        System.out.println("Participants inscrits: " + participants.size());
        System.out.println("Statut: " + (annule ? "ANNULÉ" : "PROGRAMMÉ"));
    }
    
    //methode afficher
    public abstract void afficherDetails();
    
    @Override
    public void ajouterObserver(ParticipantObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void supprimerObserver(ParticipantObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifierObservers(String message) {
        for (ParticipantObserver observer : observers) {
            observer.recevoirNotification(message);
        }
    }
    
    // === MÉTHODES JSON POUR SÉRIALISATION ===
    
    @JsonProperty("date")  // Utilise le String pour JSON
    public String getDateForJson() {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    @JsonProperty("date") 
    public void setDateForJson(String dateStr) {
        this.date = dateStr != null ? LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
    
    // === GETTERS ET SETTERS STANDARD ===
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getLieu() {
        return lieu;
    }
    
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    
    public int getCapaciteMax() {
        return capaciteMax;
    }
    
    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }
    
    public List<Participant> getParticipants() {
        return new ArrayList<>(participants); 
    }
    
    public boolean isAnnule() {
        return annule;
    }
    
    public int getNombreParticipants() {
        return participants.size();
    }
    
    public boolean estComplet() {
        return participants.size() >= capaciteMax;
    }

    public abstract String getTypeEvenement();
}