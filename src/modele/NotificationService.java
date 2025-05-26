package modele;

/**
 * Interface définissant les services de notification
 */
public interface NotificationService {

    /**
     * Envoie une notification avec un message donné
     * @param message Le message à envoyer
     * @return true si la notification a été envoyée avec succès, false sinon
     */
    boolean envoyerNotification(String message);

    /**
     * Envoie une notification à un destinataire spécifique
     * @param destinataire L'email du destinataire
     * @param message Le message à envoyer
     * @return true si la notification a été envoyée avec succès, false sinon
     */
    boolean envoyerNotification(String destinataire, String message);

    /**
     * Envoie une notification à un participant
     * @param participant Le participant destinataire
     * @param message Le message à envoyer
     * @return true si la notification a été envoyée avec succès, false sinon
     */
    boolean envoyerNotification(Participant participant, String message);
}