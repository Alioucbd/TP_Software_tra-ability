package Main;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import Main.Formatter;
import Main.Product;
import Main.Repository;
import Main.User;

public class Main {

    private static final Map<User, Integer> profilReadlog = new HashMap<>();
    private static final Map<User, Integer> profilWritelog = new HashMap<>();
    private static final Map<User, Product> profilExpensive = new HashMap<>();

    public static void main(String[] args) {
        // Création de la liste des produits
        Repository.init();
        // Création des loggers
        createLoggers();

        // Réalisation des scénarios utilisateur
         realiserScenarios();
         try {
            // Extraction des profils utilisateur
            extractProfiles("./readlog.log");
            extractProfiles("./writelog.log");
            // Sauvegarde des profils extraits
            saveProfile("./profilReads.json", profilReadlog);
            saveProfile("./profilWrites.json", profilWritelog);
            saveProfile("./profilExpensive.json", null);
            System.out.println("Les profils ont été crée avec succès.");
        } catch (Exception e){
            System.err.println("Une erreur s'est produite lors de l'extraction des profils ...");
        }
         
         
    }

    private static void realiserScenarios() {
        // Création des utilisateurs
        User user1 = new User("Hamid", "Hakim2022@gmail.com", "Ramdani$97", 50);
        User user2 = new User("Aliou", "Aliou-gueye@yahoo.fr", "98Aloui", 23);
        User user3 = new User("Salim", "salimo@gmail.com", "Maman9H", 35);
        User user4 = new User("Chanez", "Chanez-ferred@gmail.com", "k4@KD!d", 23);
        User user5 = new User("Aghiles", "aghiles21@live.fr", "aghiles21-s456", 40);

        // Scénario pour user1 (lecture uniquement)
        Repository.affichage(user1);
        Repository.chercher(user1, 13);
        Repository.affichage(user1);
        Repository.chercher(user1, 10);
        Repository.affichage(user1);
        Repository.chercher(user1, 10);
        Repository.affichage(user1);
        Repository.chercher(user1, 13);
        Repository.affichage(user1);
        Repository.chercher(user1, 7);
        Repository.affichage(user1);
        Repository.chercher(user1, 4);

        // Scénario pour user2 (autant de lectures/écritures)
        Repository.affichage(user2);
        Repository.chercher(user2, 1);
        Repository.chercher(user2, 3);
        Repository.ajout(user2, new Product("Produit4", 150));
        Repository.affichage(user2);
        Repository.chercher(user2, 4);
        Repository.ajout(user2, new Product("Produit5", 75));
        Repository.ajout(user2, new Product("Produit6", 15));
        Repository.ajout(user2, new Product("Produit7", 800));
        Repository.ajout(user2, new Product("Produit8", 456));
        Repository.supprimer(user2, 6);
        Repository.affichage(user2);

         // Scénario pour user3 (un peu de tout)
         Repository.affichage(user3);
         Repository.update(user3, 7, null, 450, null);
         Repository.chercher(user3, 8);
         Repository.ajout(user3, new Product("Produit14", 6));
         Repository.supprimer(user3, 13);
         Repository.affichage(user3);
         Repository.ajout(user3, new Product("Produit15", 12));
         Repository.update(user3, 12, "Produit5", -1, null);
         Repository.chercher(user3, 10);
         Repository.supprimer(user3, 10);

         // Scénario pour user4 (lecture des produits les plus chers)
        Repository.affichage(user4);
        Repository.chercher(user4, 7);
        Repository.chercher(user4, 4);
        Repository.affichage(user4);
        Repository.chercher(user4, 9);
        Repository.chercher(user4, 8);
        Repository.affichage(user4);
        Repository.chercher(user4, 7);
        Repository.chercher(user4, 12);
        Repository.affichage(user4);
        Repository.chercher(user4, 8);
        Repository.chercher(user4, 9);

        // Scénario pour user5 (écriture uniquement)
        Repository.ajout(user5, new Product("Produit9", 378));
        Repository.ajout(user5, new Product("Produit10", 2));
        Repository.update(user5, 10, null, 98, null);
        Repository.ajout(user5, new Product("Produit11", 897));
        Repository.ajout(user5, new Product("Produit12", 234));
        Repository.supprimer(user5, 11);
        Repository.ajout(user5, new Product("Produit13", 132));
        Repository.update(user5, 7, "Produit7u", 1, null);
        Repository.update(user5, 13, "Produit13u", 23, null);
        Repository.supprimer(user5, 22);
    }

        private static void extractProfiles(String file) throws Exception {
            Path path = Paths.get(file);
            List<String> logs = Files.readAllLines(path);
            Pattern pattern1 = Pattern.compile("(\\{.*}) a réalisé l'opération (.*) sur le produit (\\{.*})");
            Pattern pattern2 = Pattern.compile("(\\{.*}) a réalisé l'opération (.*) sur le produit (\\d*)");
            Pattern pattern3 = Pattern.compile("(\\{.*}) a réalisé l'opération (.*)");
            Matcher matcher1, matcher2, matcher3;
            ObjectMapper mapper = new ObjectMapper();
    
            for(String log : logs) {
                matcher1 = pattern1.matcher(log);
                matcher2 = pattern2.matcher(log);
                matcher3 = pattern3.matcher(log);
    
                User user = null;
                Product product = null;
    
                if(matcher1.find()) {
                    user = mapper.readValue(matcher1.group(1), User.class);
                    product = mapper.readValue(matcher1.group(3), Product.class);
                } else if(matcher2.find())
                    user = mapper.readValue(matcher2.group(1), User.class);
                else if(matcher3.find())
                    user = mapper.readValue(matcher3.group(1), User.class);
    
                if(user == null)
                    continue;
    
                if(file.equals("./readlog.log")) {
                    incrementOperation(user, profilReadlog);
                    incrementExpensive(user, product);
                } else
                    incrementOperation(user, profilWritelog);
            }
        }
    
        private static void saveProfile(String path, Map<User, Integer> profile) throws Exception {
            String result = profile == null ? toJson() : toJson(profile);
            File file = new File(path);
            file.createNewFile();
    
            FileWriter fileWriter = new FileWriter(path);
    
            fileWriter.write("[\n" + result + "\n]");
            fileWriter.close();
        }
    
        private static String toJson(Map<User, Integer> profile) {
            String template = "{ \"user\": %s, \"operations\": %s }";
    
            return profile.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(entry -> String.format(template, entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(",\n"));
        }
    
        private static String toJson() {
            String template = "{ \"user\": %s, \"profilExpensiveProduct\": %s }";
    
            return profilExpensive.entrySet().stream()
                    .sorted(Comparator.comparing(entry -> ((Map.Entry<User, Product>) entry).getValue().getPrice()).reversed())
                    .map(entry -> String.format(template, entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(",\n"));
        }
    
        private static void incrementOperation(User user, Map<User, Integer> profile) {
            if(profile.containsKey(user))
                profile.put(user, profile.get(user)+1);
            else
                profile.put(user, 1);
        }
    
        private static void incrementExpensive(User user, Product product) {
            if(product == null)
                return;
    
            if(!profilExpensive.containsKey(user) ||
                    (profilExpensive.containsKey(user) && profilExpensive.get(user).getPrice() < product.getPrice()))
                profilExpensive.put(user, product);
        }
    
        private static void createLoggers() {

            try{
                Formatter formatter = new Formatter();
                java.util.logging.Handler readFH = new java.util.logging.FileHandler("./readlog.log");
                java.util.logging.Handler writeFH = new java.util.logging.FileHandler("./writelog.log");
                java.util.logging.Logger readLogger = Repository.getReadLogger();
                java.util.logging.Logger writeLogger = Repository.getWriteLogger();

                readFH.setFormatter(formatter);
                readFH.setLevel(java.util.logging.Level.ALL);
                readLogger.setLevel(java.util.logging.Level.ALL);
                readLogger.addHandler(readFH);

                writeFH.setFormatter(formatter);
                writeFH.setLevel(java.util.logging.Level.ALL);
                writeLogger.setLevel(java.util.logging.Level.ALL);
                writeLogger.addHandler(writeFH);
            }catch (Exception e){
                System.err.println("Impossible d'initialiser les loggers...");
                e.printStackTrace();
                System.exit(1);
            };
        };

       
    
    }
    

        

       
    

