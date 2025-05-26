package modele;
import patterns.observer.ParticipantObserver; // ✅ Ajouter

import java.util.Objects;


public class Participant implements ParticipantObserver {
private String id;
private String nom;
private String email;

// Constructeur par défaut
public Participant() {
}

// Constructeur avec paramètres
public Participant(String id, String nom, String email) {
this.id = id;
this.nom = nom;
this.email = email;
}

// Getters et Setters
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

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

// Méthode toString pour affichage
@Override
public String toString() {
return "Participant{" +
"id='" + id + '\'' +
", nom='" + nom + '\'' +
", email='" + email + '\'' +
'}';
}

// Méthodes equals et hashCode pour comparaison
// "this" = l'objet actuel
// "o" = l'autre objet qu'on compare
// "==" vérifie si c'est exactement le même objet en mémoire
@Override
public boolean equals(Object o) {
if (this == o) return true;
if (o == null || getClass() != o.getClass()) return false;
Participant that = (Participant) o;
return Objects.equals(id, that.id);
}

// Si equals() dit que deux objets sont égaux, alors hashCode() doit retourner le même nombre pour les deux.
@Override
public int hashCode() {
return Objects.hash(id);
}

@Override
public void recevoirNotification(String message) {
System.out.println("📧 Notification pour " + nom + " (" + email + "): " + message);
}
}