package vue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modele.*;
import utils.SerializationService;
import java.time.LocalDateTime;

public class MainApplication extends Application {
    
    private GestionEvenements gestion = GestionEvenements.getInstance();
    private TextArea logArea;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(" Système de Gestion d'Événements - TP POO");
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Créer les onglets
        tabPane.getTabs().addAll(
            createAccueilTab(),
            createCreerEvenementTab(),
            createParticipantsTab(),
            createNotificationsTab()
        );
        
        Scene scene = new Scene(tabPane, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        // AJOUTER DES ÉVÉNEMENTS DE TEST POUR QUE L'INTERFACE FONCTIONNE
     
      
        
        // Charger les données au démarrage
        chargerDonnees();
        creerEvenementsDeTest();
        
    }
    
    // NOUVELLE MÉTHODE : Créer des événements de test
    private void creerEvenementsDeTest() {
        try {
            // Vérifier s'il y a déjà des événements
            if (gestion.getNombreEvenements() > 0) {
                System.out.println(" " + gestion.getNombreEvenements() + " événements déjà présents");
                return;
            }
            
            // Créer quelques événements de test
            Concert concert1 = new Concert("C001", "Jazz Night", 
                LocalDateTime.now().plusDays(7), "Yaoundé", 100, "Miles Davis", "Jazz");
                
            Conference conf1 = new Conference("CONF001", "IA Conference", 
                LocalDateTime.now().plusDays(14), "ENSPY", 150, "Intelligence Artificielle");
                
            Concert concert2 = new Concert("C002", "Rock Festival", 
                LocalDateTime.now().plusDays(21), "Douala", 200, "AC/DC Tribute", "Rock");
            
            // Les ajouter au gestionnaire
            gestion.ajouterEvenement(concert1);
            gestion.ajouterEvenement(conf1);
            gestion.ajouterEvenement(concert2);
            
            System.out.println(" " + gestion.getNombreEvenements() + " événements de test créés !");
            
        } catch (Exception e) {
            System.out.println(" Erreur création événements : " + e.getMessage());
        }
    }
    
    private Tab createAccueilTab() {
        Tab tab = new Tab(" Accueil");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Titre
        Label titre = new Label("Tableau de Bord");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Statistiques
        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
            createStatCard("Événements Total", () -> String.valueOf(gestion.getNombreEvenements())),
            createStatCard("Événements Actifs", () -> String.valueOf(gestion.getEvenementsActifs().size())),
            createStatCard("Concerts", () -> String.valueOf(gestion.rechercherEvenementParType("Concert").size())),
            createStatCard("Conférences", () -> String.valueOf(gestion.rechercherEvenementParType("Conférence").size()))
        );
        
        // Liste des événements
        Label listeLabel = new Label(" Événements Récents");
        listeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        ListView<String> listeEvenements = new ListView<>();
        listeEvenements.setPrefHeight(200);
        updateListeEvenements(listeEvenements);
        
        // Boutons d'action
        HBox boutonsBox = new HBox(10);
        Button rafraichirBtn = new Button(" Rafraîchir");
        Button sauvegarderBtn = new Button(" Sauvegarder");
        Button chargerBtn = new Button(" Charger");
        
        rafraichirBtn.setOnAction(e -> {
            updateListeEvenements(listeEvenements);
            updateStats(statsBox);
        });
        
        sauvegarderBtn.setOnAction(e -> {
            boolean succes = SerializationService.sauvegarderEvenements("evenements.json");
            showAlert(succes ? " Sauvegarde réussie !" : " Erreur de sauvegarde");
        });
        
        chargerBtn.setOnAction(e -> {
            int restaures = SerializationService.restaurerEvenements("evenements.json");
            showAlert(" " + restaures + " événements chargés !");
            updateListeEvenements(listeEvenements);
            updateStats(statsBox);
        });
        
        boutonsBox.getChildren().addAll(rafraichirBtn, sauvegarderBtn, chargerBtn);
        
        content.getChildren().addAll(titre, statsBox, listeLabel, listeEvenements, boutonsBox);
        tab.setContent(new ScrollPane(content));
        
        return tab;
    }
    
    private VBox createStatCard(String label, java.util.function.Supplier<String> valueSupplier) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #3498db; -fx-background-radius: 8px;");
        
        Label valueLabel = new Label(valueSupplier.get());
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        
        card.getChildren().addAll(valueLabel, textLabel);
        return card;
    }
    
    private Tab createCreerEvenementTab() {
        Tab tab = new Tab(" Créer Événement");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titre = new Label("Créer un Nouvel Événement");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Formulaire
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Concert", "Conférence");
        typeCombo.setPromptText("Choisir le type...");
        
        TextField idField = new TextField();
        idField.setPromptText("Ex: C001");
        
        TextField nomField = new TextField();
        nomField.setPromptText("Ex: Jazz Night");
        
        TextField lieuField = new TextField();
        lieuField.setPromptText("Ex: Yaoundé");
        
        TextField capaciteField = new TextField();
        capaciteField.setPromptText("Ex: 100");
        
        // Champs spécifiques
        TextField artisteField = new TextField();
        artisteField.setPromptText("Nom de l'artiste");
        artisteField.setVisible(false);
        
        TextField genreField = new TextField();
        genreField.setPromptText("Genre musical");
        genreField.setVisible(false);
        
        TextField themeField = new TextField();
        themeField.setPromptText("Thème de la conférence");
        themeField.setVisible(false);
        
        // Événement changement de type
        typeCombo.setOnAction(e -> {
            boolean isConcert = "Concert".equals(typeCombo.getValue());
            artisteField.setVisible(isConcert);
            genreField.setVisible(isConcert);
            themeField.setVisible(!isConcert);
        });
        
        form.add(new Label("Type:"), 0, 0);
        form.add(typeCombo, 1, 0);
        form.add(new Label("ID:"), 0, 1);
        form.add(idField, 1, 1);
        form.add(new Label("Nom:"), 0, 2);
        form.add(nomField, 1, 2);
        form.add(new Label("Lieu:"), 0, 3);
        form.add(lieuField, 1, 3);
        form.add(new Label("Capacité:"), 0, 4);
        form.add(capaciteField, 1, 4);
        form.add(new Label("Artiste:"), 0, 5);
        form.add(artisteField, 1, 5);
        form.add(new Label("Genre:"), 0, 6);
        form.add(genreField, 1, 6);
        form.add(new Label("Thème:"), 0, 7);
        form.add(themeField, 1, 7);
        
        Button creerBtn = new Button(" Créer l'Événement");
        creerBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px;");
        
        creerBtn.setOnAction(e -> {
            try {
                String type = typeCombo.getValue();
                String id = idField.getText();
                String nom = nomField.getText();
                String lieu = lieuField.getText();
                int capacite = Integer.parseInt(capaciteField.getText());
                
                if (type == null || id.isEmpty() || nom.isEmpty()) {
                    showAlert(" Veuillez remplir tous les champs obligatoires !");
                    return;
                }
                
                LocalDateTime date = LocalDateTime.now().plusDays(7); // Date dans 7 jours
                
                Evenement evenement;
                if ("Concert".equals(type)) {
                    String artiste = artisteField.getText();
                    String genre = genreField.getText();
                    evenement = new Concert(id, nom, date, lieu, capacite, artiste, genre);
                } else {
                    String theme = themeField.getText();
                    evenement = new Conference(id, nom, date, lieu, capacite, theme);
                }
                
                gestion.ajouterEvenement(evenement);
                showAlert(" Événement '" + nom + "' créé avec succès !");
                
                // Reset formulaire
                typeCombo.setValue(null);
                idField.clear();
                nomField.clear();
                lieuField.clear();
                capaciteField.clear();
                artisteField.clear();
                genreField.clear();
                themeField.clear();
                artisteField.setVisible(false);
                genreField.setVisible(false);
                themeField.setVisible(false);
                
            } catch (Exception ex) {
                showAlert(" Erreur : " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(titre, form, creerBtn);
        tab.setContent(new ScrollPane(content));
        
        return tab;
    }
    
    private Tab createParticipantsTab() {
        Tab tab = new Tab(" Participants");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titre = new Label("Gestion des Participants");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Sélection événement
        ComboBox<String> eventCombo = new ComboBox<>();
        updateEventCombo(eventCombo);
        eventCombo.setPromptText("Sélectionner un événement...");
        
        // Formulaire ajout participant
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField idField = new TextField();
        idField.setPromptText("ID participant");
        
        TextField nomField = new TextField();
        nomField.setPromptText("Nom complet");
        
        TextField emailField = new TextField();
        emailField.setPromptText("email@exemple.com");
        
        form.add(new Label("ID:"), 0, 0);
        form.add(idField, 1, 0);
        form.add(new Label("Nom:"), 0, 1);
        form.add(nomField, 1, 1);
        form.add(new Label("Email:"), 0, 2);
        form.add(emailField, 1, 2);
        
        Button ajouterBtn = new Button(" Ajouter Participant");
        ajouterBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        
        ajouterBtn.setOnAction(e -> {
            try {
                String eventId = eventCombo.getValue();
                if (eventId == null) {
                    showAlert(" Veuillez sélectionner un événement !");
                    return;
                }
                
                String id = idField.getText();
                String nom = nomField.getText();
                String email = emailField.getText();
                
                if (id.isEmpty() || nom.isEmpty() || email.isEmpty()) {
                    showAlert(" Veuillez remplir tous les champs !");
                    return;
                }
                
                Evenement evenement = gestion.rechercherEvenement(eventId.split(" - ")[0]);
                if (evenement != null) {
                    Participant participant = new Participant(id, nom, email);
                    evenement.ajouterParticipant(participant);
                    showAlert("Participant '" + nom + "' ajouté !\n(Il recevra automatiquement les notifications)");
                    
                    idField.clear();
                    nomField.clear();
                    emailField.clear();
                }
                
            } catch (Exception ex) {
                showAlert(" Erreur : " + ex.getMessage());
            }
        });
        
        Button rafraichirBtn = new Button(" Rafraîchir liste");
        rafraichirBtn.setOnAction(e -> updateEventCombo(eventCombo));
        
        HBox boutonsBox = new HBox(10);
        boutonsBox.getChildren().addAll(ajouterBtn, rafraichirBtn);
        
        content.getChildren().addAll(titre, 
            new Label("Événement:"), eventCombo,
            new Label("Nouveau Participant:"), form, boutonsBox);
        tab.setContent(new ScrollPane(content));
        
        return tab;
    }
    
    private Tab createNotificationsTab() {
        Tab tab = new Tab(" Test Observer");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label titre = new Label("Test du Pattern Observer");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label description = new Label("Testez le système de notifications automatiques :");
        
        ComboBox<String> eventCombo = new ComboBox<>();
        updateEventCombo(eventCombo);
        eventCombo.setPromptText("Sélectionner un événement à annuler...");
        
        Button annulerBtn = new Button(" Annuler Événement");
        annulerBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        
        Button rafraichirBtn = new Button(" Rafraîchir");
        
        // Zone de log pour voir les notifications
        logArea = new TextArea();
        logArea.setPrefRowCount(15);
        logArea.setEditable(false);
        logArea.setPromptText("Les notifications apparaîtront ici...");
        
        annulerBtn.setOnAction(e -> {
            String eventInfo = eventCombo.getValue();
            if (eventInfo == null) {
                showAlert(" Veuillez sélectionner un événement !");
                return;
            }
            
            String eventId = eventInfo.split(" - ")[0];
            Evenement evenement = gestion.rechercherEvenement(eventId);
            
            if (evenement != null && !evenement.isAnnule()) {
                logArea.appendText("\n=== ANNULATION DE L'ÉVÉNEMENT ===\n");
                logArea.appendText("Événement: " + evenement.getNom() + "\n");
                logArea.appendText("Participants à notifier: " + evenement.getNombreParticipants() + "\n\n");
                
                // Annuler l'événement (déclenche les notifications)
                evenement.annuler();
                
                logArea.appendText(" Événement annulé ! Notifications envoyées.\n");
                logArea.appendText("==========================================\n\n");
                
                updateEventCombo(eventCombo);
            } else {
                showAlert(" Événement déjà annulé ou introuvable !");
            }
        });
        
        rafraichirBtn.setOnAction(e -> updateEventCombo(eventCombo));
        
        HBox boutonsBox = new HBox(10);
        boutonsBox.getChildren().addAll(annulerBtn, rafraichirBtn);
        
        Label explication = new Label("""
             Comment ça marche :
            • Ajoutez des participants aux événements (onglet Participants)
            • Les participants deviennent automatiquement observateurs
            • Quand vous annulez un événement, tous les participants reçoivent une notification
            • Le Pattern Observer découple les notifications de la logique métier
            """);
        explication.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 10px; -fx-background-radius: 5px;");
        
        content.getChildren().addAll(titre, description, 
            new Label("Événement:"), eventCombo, boutonsBox,
            new Label(" Log des Notifications:"), logArea, explication);
        
        tab.setContent(new ScrollPane(content));
        
        return tab;
    }
    
    // Méthodes utilitaires
    private void updateListeEvenements(ListView<String> liste) {
        liste.getItems().clear();
        for (Evenement evt : gestion.getTousLesEvenements()) {
            String status = evt.isAnnule() ? " ANNULÉ" : " ACTIF";
            String info = String.format("%s - %s (%s) - %d/%d participants - %s",
                evt.getId(), evt.getNom(), evt.getTypeEvenement(),
                evt.getNombreParticipants(), evt.getCapaciteMax(), status);
            liste.getItems().add(info);
        }
    }
    
    private void updateEventCombo(ComboBox<String> combo) {
        combo.getItems().clear();
        for (Evenement evt : gestion.getTousLesEvenements()) {
            String info = evt.getId() + " - " + evt.getNom() + " (" + evt.getTypeEvenement() + ")";
            combo.getItems().add(info);
        }
    }
    private void updateStats(HBox statsBox) {
        // Vider les anciennes cartes
        statsBox.getChildren().clear();
        
        // Recréer les cartes avec les nouvelles valeurs
        statsBox.getChildren().addAll(
            createStatCard("Événements Total", () -> String.valueOf(gestion.getNombreEvenements())),
            createStatCard("Événements Actifs", () -> String.valueOf(gestion.getEvenementsActifs().size())),
            createStatCard("Concerts", () -> String.valueOf(gestion.rechercherEvenementParType("Concert").size())),
            createStatCard("Conférences", () -> String.valueOf(gestion.rechercherEvenementParType("Conférence").size()))
        );
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void chargerDonnees() {
        // Charger les données existantes au démarrage
        SerializationService.restaurerEvenements("evenements.json");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}