package logging;

import java.util.logging.*;

import Main.Formatter;
import Main.Product;
import Main.Repository;
import Main.User;

public class Main {
    private static final Logger LOGGER = Logger.getLogger("Question 2");

    public static void main (String[] args) {
        //Question 1

        // Création de la liste des produits
        Repository.init();
        // Réalisation des scénarios utilisateur
        realiserScenarios();

        //Question 2

        LOGGER.setLevel(Level.INFO);
        Formatter formatter = new Formatter();
        Handler fileHandler;

        try{
            fileHandler = new FileHandler("./commands.log");
            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(formatter);

            fileHandler.setLevel(Level.INFO);
            LOGGER.info("Test de log d'info");
            LOGGER.log(Level.SEVERE, "Test d'un log sévère");
        }catch (Exception e){
            System.err.println("Y a une erreur...");
            e.printStackTrace();
        }
    }

    private static void realiserScenarios() {
        // Création de l'utilisateur
        User user = new User("Aliou", "Aliou-gueye@yahoo.fr", "98Aloui", 23);

        // Scénario pour user
        Repository.affichage(user);
        Repository.update(user, 1, null, 589, null);
        Repository.chercher(user, 1);
        Repository.ajout(user, new Product("Produit4", 3));
        Repository.supprimer(user, 4);
        Repository.affichage(user);
        Repository.ajout(user, new Product("Produit5", 100));
        Repository.update(user, 6, "Produit6", -1, null);
        Repository.chercher(user, 5);
        Repository.supprimer(user, 4);
    }
}
