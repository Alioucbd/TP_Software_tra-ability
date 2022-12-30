package OutPut;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Repository {
    private static final Map<Integer, Product> produits = new HashMap<>();

    // affiche les produits dans un référentiel, où chaque produit a un ID, un
    // nom, un prix et une date d'expiration
    public static void affichage(User user) {
        System.out.println(produits);
        Repository.getReadLogger().log(Level.FINER, (("[09/12/2022 17:50:05 - WRITE] User " + user) + " à fait une opération d'affichage sur le produit ") + produits);
    }

    // récupérer un produit par son ID (si aucun produit avec l'ID fourni n'existe,
    // une exception doit être levée).
    public static void chercher(User user, int id) {
        Product product = null;
        if (produits.containsKey(id))
            product = produits.get(id);

        if (product == null) {
            System.err.println("**** Le produit que vous voulez récupérer n'existe pas *********");
            return;
        }
        System.out.println(product);
        Repository.getReadLogger().log(Level.FINER, (("[09/12/2022 17:50:05 - WRITE] User " + user) + " à fait une opération de chercher sur le produit ") + produits);
        Repository.getReadLogger().log(java.util.logging.Level.FINER, "[30/12/2022 18:11:00 - READ] User " + user + " a fait une opération de chercher sur le produit " + product);
    }

    // ajouter un nouveau produit (si un produit avec le même ID existe déjà, un
    // l'exception doit être levée)
    public static void ajout(User user, Product product) {
        produits.put(product.getId(), product);
        Repository.getWriteLogger().log(Level.FINER, (("[09/12/2022 17:50:05 - WRITE] User " + user) + " à fait une opération d'ajout sur le produit ") + produits);
        Repository.getWriteLogger().log(java.util.logging.Level.FINER, "[30/12/2022 18:11:00 - WRITE] User " + user + " a fait une opération de ajout sur le produit " + product);
    }

    private static void mettre(Product product) {
        produits.put(product.getId(), product);
    }

    public static void init() {
        mettre(new Product("Produit1", 25));
        mettre(new Product("Produit2", 10));
        mettre(new Product("Produit3", 30));
    }

    // supprime un produit par son ID (si aucun produit avec l'ID fourni n'existe,
    // une exception doit être levée).
    public static void supprimer(User user, int product) {
        if (!produits.containsKey(product)) {
            System.err.println("********* Le produit que vous voulez supprimer n'existe pas *********");
            return;
        }
        produits.remove(product);
        Repository.getWriteLogger().log(Level.FINER, (("[09/12/2022 17:50:05 - WRITE] User " + user) + " à fait une opération de supprimer sur le produit ") + produits);
        Repository.getWriteLogger().log(java.util.logging.Level.FINER, "[30/12/2022 18:11:00 - WRITE] User " + user + " a fait une opération de supprimer sur le produit " + product);
    }

    // mettre à jour les informations d'un produit (si aucun produit avec l'ID fourni n'existe,
    // une exception doit être levée)
    public static void update(User user, int id, String name, int price, LocalDate expirationDate) {
        if (!produits.containsKey(id)) {
            System.err.println("****** Le produit que vous voulez mettre à jour n'existe pas *********");
            return;
        }
        Product product = produits.get(id);
        if (name != null)
            product.setName(name);

        if (price > (-1))
            product.setPrice(price);

        if (expirationDate != null)
            product.setExpirationDate(expirationDate);

        Repository.getWriteLogger().log(Level.FINER, (("[09/12/2022 17:50:05 - WRITE] User " + user) + " à fait une opération d'update sur le produit ") + product);
        Repository.getWriteLogger().log(java.util.logging.Level.FINER, "[30/12/2022 18:11:00 - WRITE] User " + user + " a fait une opération de update sur le produit " + product);
    }

    private static final Logger READ_LOGGER = Logger.getLogger("readRepository");

    public static Logger getReadLogger() {
        return READ_LOGGER;
    }

    private static final Logger WRITE_LOGGER = Logger.getLogger("writeRepository");

    public static Logger getWriteLogger() {
        return WRITE_LOGGER;
    }
}