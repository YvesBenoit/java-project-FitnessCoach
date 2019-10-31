import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    /*
     * Ce que doit faire le mini projet :
     *
     * - Proposer de creer un set de musculation dans un fichier csv.
     * (un set : Type d'exercice (9 choix possibles)
     *           le nombre de répétition de l'exercice
     *           le poids utilisé pour cet exercice)
     *
     * - Proposer d'afficher des statistiques  :
     *            de poids/répétitions(poids moyen , médian et maximum)
     *            de nombre de répétitions (nombre de répétitions moyen , médian et maximum)
     *            de poids/set (poids moyen , médian et maximum)
     *
     */

    private final static Map<String, String> mainMenuChoices = new HashMap<>();
    private final static Map<String, String> statsMenuChoices = new HashMap<>();
    private final static Map<String, String> exercisesMenuChoices = new HashMap<>();

    private final static Scanner inputScanner = new Scanner(System.in);

    private static ArrayList<BodyBuildingSet> bodyBuildingSetsList;

    /**
     * Entry point of the Fitness Coach statistics program.
     *
     * @param args csv file.
     */
    public static void main(String[] args) throws FileNotFoundException {

        // Check first argument as valid file path
        if (!checkArgument(args[0])) {
            return;
        }

        // Load menus
        initMenus();

        // Display Main menu and get user choice
        String mainChoice;
        do {
            mainChoice = showMenu(mainMenuChoices);

            switch (mainChoice) {
                case "1":
                    // Display Exercises Menu and get user choice
                    String exerciseChoice = showMenu(exercisesMenuChoices);
                    // get number of repetitions for the new set
                    int nbRep = getIntOnStdIn("Please enter the Number of iterations for this set of " + exercisesMenuChoices.get(exerciseChoice) + " : ");
                    // get load used for the new set
                    double load = getDoubleOnStdIn("Please enter the Load used for this set of " + exercisesMenuChoices.get(exerciseChoice) + " : ");
                    // write the new set in file
                    ServiceSetsFile.addBodyBuildingSetToFile(new BodyBuildingSet(exercisesMenuChoices.get(exerciseChoice), nbRep, load), args[0]);
                    break;

                case "2":
                    // Display Exercises Menu and get user choice
                    exerciseChoice = showMenu(exercisesMenuChoices);
                    // Display Stats Menu and get user choice
                    String statChoice = showMenu(statsMenuChoices);
                    // Load sets in memory
                    try {
                        bodyBuildingSetsList = ServiceSetsFile.loadCsvFileInMemory(args[0]);
                    } catch (Exception e) {
                        if (e instanceof ServiceSetsFile.FileEmptyException) {
                            System.out.println("Une erreur est survenue !  le fichier ne contient qu'un entête !\n" + e.getLocalizedMessage());
                            return;
                        } else {
                            System.out.println("Une erreur est survenue !  Pas de fichier d'historique de sets !\n" + e.getLocalizedMessage());
                            return;
                        }
                    }
                    // compute stats and display result
                    //    1st argument : a 3*3 array of double containing result of the 3 stats (average, median, maximum) for the 3 possibles choices (load/nbRep ; nbRep ; load/set)
                    //    2nd argument is the text of choosen exercise
                    //    3rd argument is the text of asked statistics
                    try {
                        showResults(ServiceStat.computeStats(bodyBuildingSetsList, exercisesMenuChoices.get(exerciseChoice)), exercisesMenuChoices.get(exerciseChoice), statsMenuChoices.get(statChoice));
                    } catch (ServiceSetsFile.FileEmptyException e) {
                        System.out.println("Une erreur est survenue !  il n'y a pas de sets enregistrés pour l'exercice demandé !\n" + e.getLocalizedMessage());
                    }
                    break;

                default:
                    break;
            }
        } while (!"3".equals(mainChoice));

        // Close input scanner
        inputScanner.close();
    }


    /**
     * Check file received is an existing files.
     *
     * @param fileName the path to check.
     * @return true if it is a valid path, false either.
     */
    private static boolean checkArgument(String fileName) {
        if (fileName.length() != 0) {
            Path filePath = Path.of(fileName);
            if (!Files.exists(filePath)) {
                System.out.println("Le programme ne peut fonctionner qu'avec le fichier csv d'historique des sets passé en argument");
                System.out.println("Il va donc s'arrêter car l'argument passé est non trouvé sur le disque");
                return false;
            }
        } else {
            System.out.println("Le programme ne peut fonctionner qu'avec le fichier csv d'historique des sets");
            System.out.println("Il va donc s'arrêter car l'arguments passé est vide !");
            return false;
        }
        return true;
    }

    /**
     * Load menus in the menu choices hash maps.
     */
    private static void initMenus() {
        mainMenuChoices.put("1", "Ajouter un set");
        mainMenuChoices.put("2", "Afficher les performances sur un exercice");
        mainMenuChoices.put("3", "Quitter le programme");

        statsMenuChoices.put("1", "Stats de poids (/ répétitions)");
        statsMenuChoices.put("2", "Stats de nombre de répétitions");
        statsMenuChoices.put("3", "Stats de poids (/ set)  ");

        exercisesMenuChoices.put("1", "SQUAT");
        exercisesMenuChoices.put("2", "LEG_EXTENSION");
        exercisesMenuChoices.put("3", "LEG_CURL");
        exercisesMenuChoices.put("4", "LEG_PRESS");
        exercisesMenuChoices.put("5", "CRUNCH");
        exercisesMenuChoices.put("6", "PLANK");
        exercisesMenuChoices.put("7", "BENCH_PRESS");
        exercisesMenuChoices.put("8", "TRICEPS_EXTENSION");
        exercisesMenuChoices.put("9", "BICEPS_CURL");
    }

    /**
     * Shows a menu based on a list of choices given as parameter in a map and gets the user choice.
     *
     * @param menuPossibleChoiceMap the list of possible choices.
     * @return the actual choice made by the user.
     */
    private static String showMenu(Map<String, String> menuPossibleChoiceMap) {
        ArrayList<String> menuPossibleChoiceList = new ArrayList<>();
        System.out.println("\n----------- Fitness Coach ----------");
        for (Map.Entry<String, String> menuChoice : menuPossibleChoiceMap.entrySet()) {
            System.out.print(menuChoice.getKey());
            System.out.print(". --> ");
            System.out.println(menuChoice.getValue());

            menuPossibleChoiceList.add(menuChoice.getKey());
        }
        System.out.println("----------- ########################### ----------");

        return getUserChoice(menuPossibleChoiceList);
    }

    /**
     * Gets a user choice based on a list of possible choices.
     *
     * @param possibleValues the possible values to check against.
     * @return the user choice.
     */
    private static String getUserChoice(List<String> possibleValues) {
        String userChoice;
        do {
            System.out.print("Entrez votre choix : ");
            userChoice = inputScanner.nextLine();
        } while (!possibleValues.contains(userChoice));

        return userChoice;
    }

    /**
     * Gets an int from a user.
     *
     * @param userGuide a message to explain what for the int is requested.
     * @return the user choice.
     */
    private static int getIntOnStdIn(String userGuide) {
/*----------------------------------------------------------------------
affichage texte utilisateur + saisie clavier + return de l'int saisi
 ----------------------------------------------------------------------*/
        Scanner sc = new Scanner(System.in);
        int res = 0;
        while (true) {
            System.out.println(userGuide);
            try {
                res = sc.nextInt();
                return res;
            } catch (InputMismatchException e) {
                System.out.println("Don't be so stupid, an int is required !");
                sc.nextLine();
            }
        }

    }

    /**
     * Gets a double from a user.
     *
     * @param userGuide a message to explain what for the double is requested.
     * @return the user choice.
     */
    private static double getDoubleOnStdIn(String userGuide) {
/*----------------------------------------------------------------------
affichage texte utilisateur + saisie clavier + return de l'int saisi
 ----------------------------------------------------------------------*/
        Scanner sc = new Scanner(System.in);
        double res = 0;
        while (true) {
            System.out.println(userGuide);
            try {
                res = sc.nextDouble();
                return res;
            } catch (InputMismatchException e) {
                System.out.println("Don't be so stupid, a double is required !");
                sc.nextLine();
            }
        }

    }


    /**
     * Statistics menu handling.
     *
     * @param results     a 3*3 Array containing statistics results.
     * @param exerciseLib the text of the chosen exercise
     * @param statLib     the text of the chosen statistics
     */
    private static void showResults(double[][] results, String exerciseLib, String statLib) {

        // Affichage du résultat
        System.out.println("----------- Stats " + exerciseLib + " " + statLib + " ----------");
        switch (statLib) {
            case "Stats de poids (/ répétitions)":
                System.out.println("poids moyen   soulevé : " + results[0][0] + " kg.");
                System.out.println("poids médian  soulevé : " + results[0][1] + " kg.");
                System.out.println("poids maximal soulevé : " + results[0][2] + " kg.");
                break;
            case "Stats de nombre de répétitions":
                System.out.println("nombre moyen   de répétitions : " + results[1][0] + " reps.");
                System.out.println("nombre médian  de répétitions : " + results[1][1] + " reps.");
                System.out.println("nombre maximal de répétitions : " + results[1][2] + " reps.");
                break;
            case "Stats de poids (/ set)  ":
                System.out.println("poids moyen   soulevé par set : " + results[2][0] + " kg.");
                System.out.println("poids médian  soulevé par set : " + results[2][1] + " kg.");
                System.out.println("poids maximal soulevé par set : " + results[2][2] + " kg.");
                break;
            default:
                break;

        }
    }

}
