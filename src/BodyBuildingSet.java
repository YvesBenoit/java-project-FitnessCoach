import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BodyBuildingSet {

    private String exercise = "";
    private int nbRep = 0;
    private double load = 0.0;
    private double loadByNbRep = load * nbRep;


//    private final Path filePath;

    /**
     * Constructor with all attributes in argument.
     *
     * @param exercise the type of exercise done in the set.
     * @param nbRep    the number of iteration during the set.
     * @param load     the load used during the set.
     *                 //  * @param filePath
     */
    public BodyBuildingSet(String exercise, int nbRep, double load) {
        this.exercise = exercise;
        this.nbRep = nbRep;
        this.load = load;
        this.loadByNbRep = load * nbRep;
    }

    /**
     * Gets body building set exercise .
     *
     * @return the exercise done in the set.
     */
    public String getExercise() {
        return this.exercise;
    }

    /**
     * Gets body building set number of iterations during set.
     *
     * @return the number of iterations during set.
     */
    public int getNbRep() {
        return this.nbRep;
    }

    /**
     * Gets body building set load used during set.
     *
     * @return body building set load used during set.
     */
    public double getLoad() {
        return this.load;
    }


    /**
     * Gets body building set load by nbRep produce.
     *
     * @return body building set load by nbRep produce.
     */
    public double getLoadByNbRep() {
        return this.loadByNbRep;
    }


    @Override
    /**
     * Formats body building set in scv format.
     *
     * @return body building set in scv format.
     */
    public String toString() {
        return getExercise() + ";" + getNbRep() + ";" + getLoad();
    }

    @Override
    public boolean equals(Object o) {
        // If o ref is null or is not the same class, we should return false directly.
        if (o == null || getClass() != o.getClass()) return false;

        // Otherwise, it means that it is a BodyBuildingSet object, then we can compare set properties.
        BodyBuildingSet bodyBuildingSet = (BodyBuildingSet) o;
        if ((this.exercise.equals(bodyBuildingSet.exercise)) &&
                (this.nbRep == bodyBuildingSet.nbRep) &&
                (this.load == bodyBuildingSet.load) &&
                (this.loadByNbRep == bodyBuildingSet.loadByNbRep)) {
            return true;
        } else {
            return false;
        }
    }


}