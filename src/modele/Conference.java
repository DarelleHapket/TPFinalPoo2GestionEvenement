package modele;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conference extends Evenement {
    private String theme;
    private List<Intervenant> intervenants;
    
    // Constructeur par défaut
    public Conference() {
        super();
        this.intervenants = new ArrayList<>();
    }
    
    // Constructeur avec paramètres
    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String theme) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = new ArrayList<>();
    }
    
    // Méthode pour ajouter un intervenant
    public boolean ajouterIntervenant(Intervenant intervenant) {
        if (intervenant == null) {
            throw new IllegalArgumentException("L'intervenant ne peut pas être null");
        }
        
        if (intervenants.contains(intervenant)) {
            return false; 
        }
        
        return intervenants.add(intervenant);
    }
    
    // Méthode pour supprimer un intervenant
    public boolean supprimerIntervenant(Intervenant intervenant) {
        return intervenants.remove(intervenant);
    }
    
  
    
    // Implémentation de la méthode abstraite afficherDetails
    @Override
    public void afficherDetails() {
        // Affiche d'abord les détails de base de la classe mère
        afficherDetailsBase();
        
        // Puis affiche les détails spécifiques à la conférence
        System.out.println("Thème: " + theme);
        System.out.println("Nombre d'intervenants: " + intervenants.size());
        if (!intervenants.isEmpty()) {
            System.out.println("Intervenants:");
            for (Intervenant intervenant : intervenants) {
                System.out.println("  - " + intervenant.getNom() + " (" + intervenant.getSpecialite() + ")");
            }
        }
        System.out.println("");
    }
    

    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public List<Intervenant> getIntervenants() {
        return new ArrayList<>(intervenants); // Retourne une copie pour protéger l'encapsulation
    }
    
    public void setIntervenants(List<Intervenant> intervenants) {
        this.intervenants = new ArrayList<>(intervenants);
    }
    
    public int getNombreIntervenants() {
        return intervenants.size();
    }


	   @Override
	    public String getTypeEvenement() {
	        return "Conférence";
	    }
}