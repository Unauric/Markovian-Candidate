import java.util.ArrayList;
import java.util.HashMap;

public class MarkovModel {
     String sequence; // sequence of characters we have to process
     int order; // Order of our markov model, how many characters we are going to be processing at a time
     int alphabetCounter; // amount of different characters in our dataset
     HashMap<Integer, HashMap<String, Integer>>  SubstringMap = new HashMap<>();

    public MarkovModel(int k, String s){
        sequence = s;
        order = k;
        MapSubstringsOfLengths(k, k + 1);
        if (k != 1) {
            MapSubstrings(s, 1);
        }
        alphabetCounter = CountCharacters();
    }

    public void InsertNewSequence(String s){
        sequence = s;
        alphabetCounter = CountCharacters();
    }

    public void AddTextToSequence(String s){
        sequence += s;
        alphabetCounter = CountCharacters();
    }

    public void MapSubstringsOfLengths(int from, int to){
        for(int i = from; i <= to; i++){
            MapSubstrings(sequence, i);
        }
    }

    public void MapSubstrings(String s, int k){
        HashMap<String, Integer> map = new HashMap<>();
        for(int i = 0; i < s.length(); i++){
            StringBuilder substring = new StringBuilder();
            for(int j = 0; j < k; j++){
                int charIndex = (i + j) % s.length();
                substring.append(s.charAt(charIndex));
            }
            map.putIfAbsent(substring.toString(), 0);
           /* if(SubstringMap.get(substring.toString()) == 0){
                substrings.add(substring.toString());
            }
            */
            map.put(substring.toString(), map.get(substring.toString()) + 1);
        }
        SubstringMap.put(k, map);
    }

    public ArrayList<String> getWordsOfLength(int length){
        ArrayList<String> words = new ArrayList<>();
        // keys
        for (String i : SubstringMap.get(length).keySet()) {
            if(i.length() == length){
                words.add(i);
            }
        }
        return words;
    }

    public int CountCharacters()
    {
        return getWordsOfLength(1).size();
    }

    public double Laplace(String input){
    /*    if(SubstringMap.get(input.length()).get(input) == null){
            //Objects.requireNonNull(SubstringMap.put(input.length(), SubstringMap.get(input.length()))).put(input, 0);
            return 0;
        }
        */
        String inputMinOne = input.substring(0, input.length()-1);
        SubstringMap.get(input.length()).putIfAbsent(input, 0);
        SubstringMap.get(inputMinOne.length()).putIfAbsent(inputMinOne, 0);
        return (double) (SubstringMap.get(input.length()).get(input) + 1) / (SubstringMap.get(inputMinOne.length()).get(inputMinOne) + alphabetCounter);
    }


    public HashMap<String, Double> LikeliHoods(String input){
        HashMap<String, Double> likeli = new HashMap<String, Double>();
        double probSum = 0;
        for(int i = 0; i < input.length(); i++){
            StringBuilder substring = new StringBuilder();
            for(int j = 0; j < order + 1; j++){
                int charIndex = (i + j) % input.length();
                substring.append(input.charAt(charIndex));
            }
            //System.out.println(substring.toString() + "   " + Laplace(substring.toString()));
            //likeli.putIfAbsent(substring.toString(), (double)0);
            likeli.put(substring.toString(),Math.log(Laplace(substring.toString())));
            probSum += Math.log(Laplace(substring.toString()));
        }
        likeli.put("**AVERAGE**", probSum / input.length());
        //System.out.println("TOTAL: " + probSum);
        //System.out.println("AVERAGE: " + probSum / input.length());
        return likeli;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("S = ").append(alphabetCounter).append("\n");

        // Loop over all substring maps stored by length
        for (int k = order; k <= order + 1; k++) {
            HashMap<String, Integer> map = SubstringMap.get(k);
            if (map == null) continue;  // no substrings of this length

            map.entrySet()
                    .stream()
                    .sorted((a, b) -> a.getKey().compareTo(b.getKey())) // optional sorting
                    .forEach(e -> sb.append("\"")
                            .append(e.getKey())
                            .append("\"")
                            .append("\t")
                            .append(e.getValue())
                            .append("\n"));
        }

        return sb.toString();
    }

}
