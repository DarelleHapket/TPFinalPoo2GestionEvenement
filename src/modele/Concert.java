package modele;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe représentant un concert
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Concert extends Evenement {
    private String artiste;
    private String genreMusical;
    
    // Constructeur par défaut
    public Concert() {
        super();
    }
    
    // Constructeur avec paramètres
    public Concert(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String artiste, String genreMusical) {
        super(id, nom, date, lieu, capaciteMax);
        this.artiste = artiste;
        this.genreMusical = genreMusical;
    }
    

    // Implémentation de la méthode abstraite afficherDetails
    @Override
    public void afficherDetails() {
        // Affiche d'abord les détails de base de la classe mère
        afficherDetailsBase();
        
        // Puis affiche les détails spécifiques au concert
        System.out.println("Artiste: " +artiste);
        System.out.println("Genre musical: " + genreMusical);
        System.out.println("");
    }
    
    // Getters et Setters
    public String getArtiste() {
        return artiste;
    }
    
    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }
    
    public String getGenreMusical() {
        return genreMusical;
    }
    
    public void setGenreMusical(String genreMusical) {
        this.genreMusical = genreMusical;
    }

	@Override
	
	    public String getTypeEvenement() {
	        return "Concert";
	    }
}