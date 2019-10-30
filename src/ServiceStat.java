import java.util.ArrayList;
import java.util.Comparator;

public class ServiceStat {



    public static double[][] computeStats(ArrayList<BodyBuildingSet> bodyBuildingSetsListComplete,String exerciseLib) throws ServiceSetsFile.fileEmptyException {

        double sumOfLoadByNbRep = 0;
        double sumOfNbRep = 0;
        double[][] res = new double[3][3];

//       Utilisation d'une 2eme ArrayList (égale mais pas identique) à fin de ne conserver que les sets de l'exercice à stater
        ArrayList<BodyBuildingSet> bodyBuildingSetsList = (ArrayList<BodyBuildingSet>) bodyBuildingSetsListComplete.clone();

        for (BodyBuildingSet currentBBS : bodyBuildingSetsListComplete) {
            if (currentBBS.getExercise().equals(exerciseLib)) {
                sumOfLoadByNbRep += currentBBS.getLoadByNbRep();
                sumOfNbRep += currentBBS.getNbRep();
                res[0][2] = res[0][2] < currentBBS.getLoad() ? currentBBS.getLoad() : res[0][2];    /* maxLoad */
                res[1][2] = res[1][2] < currentBBS.getNbRep() ? currentBBS.getNbRep() : res[1][2];     /* maxNbRep */
                res[2][2] = res[2][2] < currentBBS.getLoadByNbRep() ? currentBBS.getLoadByNbRep() : res[2][2];     /* maxLoadBySet */
            } else {
                bodyBuildingSetsList.remove(currentBBS);
            }
        }

        // no sets ==> no stats...
        if (bodyBuildingSetsList.isEmpty()) {
            throw new ServiceSetsFile.fileEmptyException("no sets for "+exerciseLib+" ! ");
        }

        // détermination des 3 moyennes
        res[0][0] = sumOfLoadByNbRep / sumOfNbRep;       /* averageLoads   */
        res[1][0] = sumOfNbRep / bodyBuildingSetsList.size();       /* averageNbRep   */
        res[2][0] = sumOfLoadByNbRep / bodyBuildingSetsList.size();       /* averageLoadsByNbRep   */

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
        bodyBuildingSetsList.sort(new Comparator<>() {
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
        if (bodyBuildingSetsList.size() % 2 == 0)
            res[0][1] = (bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2).getLoad() + bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2 - 1).getLoad()) / 2;
        else {
            res[0][1] = bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2).getLoad();
        }

// 2eme tri --> Comparator utilise getNbRep()
        bodyBuildingSetsList.sort(new Comparator<>() {
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
        if (bodyBuildingSetsList.size() % 2 == 0)
            res[1][1] = (bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2).getNbRep() + bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2 - 1).getNbRep()) / 2;
        else {
            res[1][1] = bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2).getNbRep();
        }

// 3eme tri --> Comparator utilise getLoadByNbRep()
        bodyBuildingSetsList.sort(new Comparator<>() {
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
        if (bodyBuildingSetsList.size() % 2 == 0)
            res[2][1] = (bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2).getLoadByNbRep() + bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2 - 1).getLoadByNbRep()) / 2;
        else {
            res[2][1] = bodyBuildingSetsList.get(bodyBuildingSetsList.size() / 2).getLoadByNbRep();
        }

        return res;

    }



}
