import java.util.Objects;
import java.util.HashMap;


public class BestModel {
    int order;
    String filename1, filename2;
    String[] classificationFiles;
    MarkovModel markovModel1;
    MarkovModel markovModel2;
    private static final String AVG_KEY = "**AVERAGE**";

    BestModel(int order, String file1, String file2, String[] classificationFiles) {
        this.order = order;
        this.filename1 = file1;
        this.filename2 = file2;
        this.classificationFiles = classificationFiles;
        this.markovModel1 = new MarkovModel(order, ReadFile(filename1)); // Bush
        this.markovModel2 = new MarkovModel(order, ReadFile(filename2)); // Kerry

    }

    public String ReadFile(String filename) {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filename)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ExecuteTest()
    {
        for(int i=0; i<classificationFiles.length; i++){
            HashMap<String, Double> probs1 = markovModel1.LikeliHoods(ReadFile(classificationFiles[i]));
            HashMap<String, Double> probs2 = markovModel2.LikeliHoods(ReadFile(classificationFiles[i]));
            HashMap<String, Double> probDiffs = AvgAndTop10DiffStr(probs1, probs2);
            PrintResult(classificationFiles[i], probs1, probs2, probDiffs);
        }
    }

    public HashMap<String, Double> AvgAndTop10DiffStr(HashMap<String, Double> likelihoods1, HashMap<String, Double> likelihoods2) {
        HashMap<String, Double> top10DiffAndAvg = new HashMap<String, Double>();
        top10DiffAndAvg.put(AVG_KEY, likelihoods2.get(AVG_KEY) -  likelihoods1.get(AVG_KEY));
        for (String i : likelihoods1.keySet()){
            if(!Objects.equals(i, AVG_KEY)){
                if(top10DiffAndAvg.size() < 11){
                    top10DiffAndAvg.put(i, likelihoods2.get(i) - likelihoods1.get(i));
                }
                else if(Math.abs(likelihoods2.get(i) - likelihoods1.get(i)) > Math.abs(top10DiffAndAvg.get(FindSmallest(top10DiffAndAvg)))){
                    top10DiffAndAvg.remove(FindSmallest(top10DiffAndAvg));
                    top10DiffAndAvg.put(i, likelihoods2.get(i) - likelihoods1.get(i));
                }
            }
        }
        return top10DiffAndAvg;
    }

    public String FindSmallest(HashMap<String, Double> map){
        String smallest = "";
        Double valSmallest = Double.MAX_VALUE;
        for (String i : map.keySet()) {
            if(Math.abs(map.get(i)) < Math.abs(valSmallest) && !Objects.equals(i, AVG_KEY)){
                valSmallest = map.get(i);
                smallest = i;
            }
        }
        return smallest;

    }

    public void PrintResult(String filename, HashMap<String, Double> probs1,
                            HashMap<String, Double> probs2, HashMap<String, Double> probsDiff)
    {

        int sequenceWidth = this.order + 6;

        String formatHeader = String.format("%%-%ds %%-20s %%-20s %%-20s%%n", sequenceWidth);

        String formatRow = String.format("%%-%ds %%-20.6f %%-20.6f %%-20.6f%%n", sequenceWidth);

        System.out.println("===========================================================");
        System.out.println("FILE: " + filename);
        System.out.println("===========================================================");

        System.out.printf(formatHeader, "TEXT SEQUENCE", "DIFF", "BUSH MODEL", "KERRY MODEL");

        int totalWidth = sequenceWidth + 20 + 20 + 20 + 3; // Approx total width of characters
        String separator = new String(new char[totalWidth]).replace('\0', '-');
        System.out.println(separator);

        for (String key : probsDiff.keySet()) {
            if (!key.equals(AVG_KEY)) {
                System.out.printf(formatRow,
                        key,
                        probsDiff.get(key),
                        probs1.get(key),
                        probs2.get(key));
            }
        }

        System.out.println(separator);

        System.out.printf(formatRow,
                "AVERAGE",
                probsDiff.get(AVG_KEY),
                probs1.get(AVG_KEY),
                probs2.get(AVG_KEY));
        System.out.println("===========================================================");

        if (probs1.get(AVG_KEY) > probs2.get(AVG_KEY)) {
            System.out.println("RESULT: Quote was more likely said by GEORGE BUSH");
        } else {
            System.out.println("RESULT: Quote was more likely said by JOHN KERRY");
        }

        System.out.println("===========================================================");
    }

}
