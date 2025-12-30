//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        int order = 5;

        String trainingFile1 = "bush.txt";
        String trainingFile2 = "kerry.txt";

        String[] tests = {"test_bush.txt", "test_kerry.txt", "test_mixed.txt"};

        BestModel comparator = new BestModel(order, trainingFile1, trainingFile2, tests);

        comparator.ExecuteTest();
    }
}