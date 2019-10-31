import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ServiceSetsFile {

    /**
     * Loads a csv file in a list of body building sets.
     *
     * @param fileName the path to the file on disk.
     * @return the ArrayList of BodyBuildingSet.
     */
    public static ArrayList loadCsvFileInMemory(String fileName) throws FileNotFoundException, FileEmptyException {

        // Alimentation d'un arrayList de BodyBuildingSets par lecture du fichier passé en argument
        ArrayList<String> csvLinesList = readFileToArrayList(fileName);

        // Alimentation d'un arrayList de BodyBuildingSets par lecture de la table de String issue de la lecture fichier
        // La 1ere ligne (==> l'entête) est ignorée
        // les lignes suivantes sont splitées en fonction du separateur ";" , les objets sets sont instanciés et stockés dans la liste
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
        if (bodyBuildingSetsList.size() <  2) {
            throw (new FileEmptyException("pas de sets dans le fichier !")) ;
        }

        return bodyBuildingSetsList;
    }

    /**
     * dedicated exception in case of file with only the first line (or empty file)
     *      *
     *      * @param s ???
     *      * @return ???
     */
    public static class FileEmptyException extends Exception {

        public FileEmptyException(String s) {
            super(s) ;
        }
    }

    /**
     * Load body buildings sets file as a list of strings representing body building sets in csv format .
     *
     * @param fileName the path to the file on disk.
     * @return the ArrayList of strings representing body building Set in csv format.
     */
    private static ArrayList readFileToArrayList(String fileName) throws FileNotFoundException {
        ArrayList<String> csvLinesInArrayList = new ArrayList<>();

        Scanner scFile = new Scanner(new java.io.File(fileName));
        for (int i = 0; (scFile.hasNextLine()); ++i) {     // boucle tant qu'il y a une ligne suivante
            String lineRead = scFile.nextLine();           // lecture d'une ligne
            if (lineRead.length() > 0) {                   // si ligne vide , rien n'est fait ==> itération sur ligne suivante
                csvLinesInArrayList.add(lineRead);         // ajout de la ligne lue sur stdIn dans la ArrayList
            }
        }
        return csvLinesInArrayList;
    }

    public static void addBodyBuildingSetToFile(BodyBuildingSet bodyBuildingSet , String fileName) throws FileNotFoundException {

        File csvFile = new File(fileName);
        if (!csvFile.exists())
            throw new FileNotFoundException("File doesn't exist !");
        else {
            PrintStream l_out = new PrintStream(new FileOutputStream(fileName, true));
            l_out.println(bodyBuildingSet.toString());
            l_out.flush();
            l_out.close();
        }
        return;
    }

}
