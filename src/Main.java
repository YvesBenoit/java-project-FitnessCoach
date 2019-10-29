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

        // Load first argument as file path
        if (args.length != 0) {
            Path filePath = Path.of(args[0]);
            if (!Files.exists(filePath)) {
                System.out.println("Le programme ne peut fonctionner qu'avec le fichier csv d'historique des sets passé en argument");
                System.out.println("Il va donc s'arrêter car l'argument passé est non trouvé sur le disque");
                return;
            }

        } else {
            System.out.println("Le programme ne peut fonctionner qu'avec le fichier csv d'historique des sets");
            System.out.println("Il va donc s'arrêter car l'arguments passé est vide !");
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
                    addBodyBuildingSetToFile(args[0]);
                    break;
                case "2":
                    bodyBuildingSetsList = loadCsvFileInMemory(args[0]);
                    showStats(showMenu(exercisesMenuChoices),showMenu(statsMenuChoices));
                    break;
                default:
                    break;
            }
        } while (!mainChoice.equals("3"));

        // Close input scanner
        inputScanner.close();
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
     * Loads a csv file in a list of body building sets.
     *
     * @param fileName the path to the file on disk.
     * @return the ArrayList of BodyBuildingSet.
     */
    public static ArrayList loadCsvFileInMemory(String fileName) {

        // Alimentation d'un arrayList de BodyBuildingSets par lecture du fichier passé en argument
        ArrayList<String> csvLinesList = readFileToArrayList(fileName);

        ArrayList<BodyBuildingSet> bodyBuildingSetsList = new ArrayList<>();
        boolean firstLine = true;

        for (String csvLine : csvLinesList) {
            String[] csvLineSplit = csvLine.split(";");
            if (!firstLine) {
                BodyBuildingSet bodyBuildingSet = new BodyBuildingSet(csvLineSplit[0], Integer.parseInt(csvLineSplit[1]), Double.parseDouble(csvLineSplit[2]));
                bodyBuildingSetsList.add(bodyBuildingSet);
            } else {
                firstLine = false;
            }
        }
        return bodyBuildingSetsList;
    }

    /**
     * Load body buildings sets file as a list of strings representing body building sets in csv format .
     *
     * @param fileName the path to the file on disk.
     * @return the ArrayList of strings representing body building Set in csv format.
     */
    // Lecteur de fichier vers ArrayList
    public static ArrayList readFileToArrayList(String fileName) {
        ArrayList<String> csvLinesInArrayList = new ArrayList<>();
        //Scanner scFile;
        int nbLinesRead = 0;

        try (Scanner scFile = new Scanner(new java.io.File(fileName))) {
            for (int i = 0; (scFile.hasNextLine()); ++i) {                              // boucle tant qu'il y a une ligne suivante
                String lineRead = scFile.nextLine();                                         // lecture d'une ligne
                if (lineRead.length() > 0) {                                                 // si ligne vide , rien n'est fait ==> itération sur ligne suivante
                    csvLinesInArrayList.add(lineRead);   // ajout de la ligne lue sur SstdIn dans la ArrayList
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Une erreur est survenue !\n" + e.getLocalizedMessage());
            //      scFile.nextLine();
        }
        return csvLinesInArrayList;
    }

    /**
     * Add a new body building set.
     */
    private static void addBodyBuildingSetToFile(String fileName) throws FileNotFoundException {

        String exerciseChoice = showMenu(exercisesMenuChoices);
        String exerciseLib = exercisesMenuChoices.get(exerciseChoice);
        int nbRep = getIntOnStdIn("Please enter the Number of iterations for this set of " + exerciseLib + " : ");
        double load = getDoubleOnStdIn("Please enter the Load used for this set of " + exerciseLib + " : ");

        BodyBuildingSet bodyBuildingSet = new BodyBuildingSet(exerciseLib, nbRep, load);

        File csvFile = new File(fileName);
        if (!csvFile.exists())
            throw new FileNotFoundException("Le fichier n'existe pas");
        else {
            PrintStream l_out = new PrintStream(new FileOutputStream(fileName, true));
            l_out.println(bodyBuildingSet.toString());
            l_out.flush();
            l_out.close();
        }
        return;
        //                    writeBodyBuildingSetInFile(filePath;bodyBuildingSet){ /* line=bodyBuildingSet.toString()};
        //                    updateBodyBuildingSetsList(true);
    }

    public static int getIntOnStdIn(String userGuide) {
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

    public static double getDoubleOnStdIn(String userGuide) {
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
     */
    private static void showStats(String exerciseChoice, String statChoice) {

        String exerciseLib = exercisesMenuChoices.get(exerciseChoice);
        String statLib = statsMenuChoices.get(statChoice);

        double sumOfLoadByNbRep = 0;
        double sumOfNbRep = 0;
        double maxLoad = 0;
        double maxNbRep = 0;
        double maxLoadBySet = 0;

        // Utilisation d'une 2eme ArrayList (égale mais pas identique) à fin de ne conserver que les sets de l'exercice à stater
        ArrayList<BodyBuildingSet> bodyBuildingSetsListOfExercise = new ArrayList<>();
        bodyBuildingSetsListOfExercise = (ArrayList<BodyBuildingSet>) bodyBuildingSetsList.clone();

        // si pas de set ==> pas de stats...
        if (bodyBuildingSetsListOfExercise.size() < 1) {
            System.out.println("----------- Stats " + exerciseLib + " " + statLib + " ----------");
            System.out.println("Aucun set pour cet exercice ! ");
            return;
        }

        for (BodyBuildingSet currentBBS : bodyBuildingSetsList) {
            if (currentBBS.getExercise().equals(exerciseLib)) {
                sumOfLoadByNbRep += currentBBS.getLoadByNbRep();
                sumOfNbRep += currentBBS.getNbRep();
                maxLoad = maxLoad < currentBBS.getLoad() ? currentBBS.getLoad() : maxLoad;
                maxNbRep = maxNbRep < currentBBS.getNbRep() ? currentBBS.getNbRep() : maxNbRep;
                maxLoadBySet = maxLoadBySet < currentBBS.getLoadByNbRep() ? currentBBS.getLoadByNbRep() : maxLoadBySet;
            } else {
                bodyBuildingSetsListOfExercise.remove(currentBBS);
            }
        }
// détermination des 3 moyennes
        double averageLoad = sumOfLoadByNbRep / sumOfNbRep;
        double averageNbRep = sumOfNbRep / bodyBuildingSetsListOfExercise.size();
        double averageLoadBySet = sumOfLoadByNbRep / bodyBuildingSetsListOfExercise.size();

// détermination des 3 médians
        // Pour le median, la liste étant classée, le médian est l'indice du milieu ou le milieu entre les 2 index centraux.
        // Rem : size commençant à 1 et l'index de liste à 0;
        //          Si size impair ==> index=size/2
        //          Sinon (pair) median = moyenne entre les 2 index du milieu ==> [index= size/2 (médian supérieur) + index=size/2-1 (median inférieur)] / 2
        // 3 tris successifs de la liste sont necessaires :
        //      1er  tri de la liste bodyBuildingSetsListOfExercise sur le poids (load)            (choix 1 au sous menu)
        //      2eme tri                                            sur le nbRep                   (choix 2 au sous menu)
        //      3eme tri                                            sur le poids/set (loadByNbRep) (choix 3 au sous menu)

// 1er tri --> Comparator utilise getLoad()
        bodyBuildingSetsListOfExercise.sort(new Comparator<>() {
            /**
             * Compares the body building set regarding ascending load.
             *
             * @param o1 body building set 1
             * @param o2 body building set 2
             * @return comparison result
             *          level 1 ==> 0 if both body building sets are null; 1 if o1 is null but not o2; -1 if o2 is null but not o1
             *          level 2 ==> 1 if o1.load < o2.load; -1 if o1.load > o2.load
             *          level 3 ==> o1.content.compareTo(o2.content) (alphabetical ascending ordering)
             */
            @Override
            public int compare(BodyBuildingSet o1, BodyBuildingSet o2) {
                int comparison = 0;

                // If both body building sets are null then they are equal
                if (o1 == null && o2 == null) return 0;
                    // Else if body building set 1 is null (but not body building set 2), it should be smaller compared to body building set 2
                else if (o1 == null) return -1;
                    // Else if body building set 2 is null, it should smaller than o1
                else if (o2 == null) return 1;

                // If loads are different, compare them
                if (o1.getLoad() < o2.getLoad()) {
                    comparison = -1;
                } else if (o1.getLoad() > o2.getLoad()) {
                    comparison = 1;
                } else {
                    comparison = 0;
                }

                return comparison;
            }
        });
        double medianLoad;
        if (bodyBuildingSetsListOfExercise.size() % 2 == 0)
            medianLoad = (bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2).getLoad() + bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2 - 1).getLoad()) / 2;
        else {
            medianLoad = bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2).getLoad();
        }

// 2eme tri --> Comparator utilise getNbRep()
        bodyBuildingSetsListOfExercise.sort(new Comparator<>() {
            /**
             * Compares the body building set regarding ascending nbRep.
             *
             * @param o1 body building set 1
             * @param o2 body building set 2
             * @return comparison result
             *          level 1 ==> 0 if both body building sets are null; 1 if o1 is null but not o2; -1 if o2 is null but not o1
             *          level 2 ==> 1 if o1.nbRep < o2.nbRep; -1 if o1.nbRep > o2.nbRep
             */
            @Override
            public int compare(BodyBuildingSet o1, BodyBuildingSet o2) {
                int comparison = 0;

                // If both body building sets are null then they are equal
                if (o1 == null && o2 == null) return 0;
                    // Else if body building set 1 is null (but not body building set 2), it should be smaller compared to body building set 2
                else if (o1 == null) return -1;
                    // Else if body building set 2 is null, it should smaller than o1
                else if (o2 == null) return 1;

                // If nbReps are different, compare them
                if (o1.getNbRep() < o2.getNbRep()) {
                    comparison = -1;
                } else if (o1.getNbRep() > o2.getNbRep()) {
                    comparison = 1;
                } else {
                    comparison = 0;
                }

                return comparison;
            }
        });
        double medianNbRep;
        if (bodyBuildingSetsListOfExercise.size() % 2 == 0)
            medianNbRep = (bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2).getNbRep() + bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2 - 1).getNbRep()) / 2;
        else {
            medianNbRep = bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2).getNbRep();
        }

// 3eme tri --> Comparator utilise getLoadByNbRep()
        bodyBuildingSetsListOfExercise.sort(new Comparator<>() {
            /**
             * Compares the body building set regarding ascending nbRep.
             *
             * @param o1 body building set 1
             * @param o2 body building set 2
             * @return comparison result
             *          level 1 ==> 0 if both body building sets are null; 1 if o1 is null but not o2; -1 if o2 is null but not o1
             *          level 2 ==> 1 if o1.nbRep < o2.nbRep; -1 if o1.nbRep > o2.nbRep
             */
            @Override
            public int compare(BodyBuildingSet o1, BodyBuildingSet o2) {
                int comparison = 0;

                // If both body building sets are null then they are equal
                if (o1 == null && o2 == null) return 0;
                    // Else if body building set 1 is null (but not body building set 2), it should be smaller compared to body building set 2
                else if (o1 == null) return -1;
                    // Else if body building set 2 is null, it should smaller than o1
                else if (o2 == null) return 1;

                // If nbReps are different, compare them
                if (o1.getLoadByNbRep() < o2.getLoadByNbRep()) {
                    comparison = -1;
                } else if (o1.getLoadByNbRep() > o2.getLoadByNbRep()) {
                    comparison = 1;
                } else {
                    comparison = 0;
                }

                return comparison;
            }
        });
        double medianLoadBySet;
        if (bodyBuildingSetsListOfExercise.size() % 2 == 0)
            medianLoadBySet = (bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2).getNbRep() + bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2 - 1).getNbRep()) / 2;
        else {
            medianLoadBySet = bodyBuildingSetsListOfExercise.get(bodyBuildingSetsListOfExercise.size() / 2).getNbRep();
        }


// Affichage du résultat
        System.out.println("----------- Stats " + exerciseLib + " " + statLib + " ----------");
        switch (statChoice) {
            case "1":
                System.out.println("poids moyen   soulevé : " + averageLoad + " kg.");
                System.out.println("poids médian  soulevé : " + medianLoad + " kg.");
                System.out.println("poids maximal soulevé : " + maxLoad + " kg.");
                break;
            case "2":
                System.out.println("nombre moyen   de répétitions : " + averageNbRep + " reps.");
                System.out.println("nombre médian  de répétitions : " + medianNbRep + " reps.");
                System.out.println("nombre maximal de répétitions : " + maxNbRep + " reps.");
                break;
            case "3":
                System.out.println("poids moyen   soulevé par set : " + averageLoadBySet + " kg.");
                System.out.println("poids médian  soulevé par set : " + medianLoadBySet + " kg.");
                System.out.println("poids maximal soulevé par set : " + maxLoadBySet + " kg.");
                break;
            default:
                break;
        }
    }

}
