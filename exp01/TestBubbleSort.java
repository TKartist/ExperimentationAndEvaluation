package exp01;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import static exp01.TestCaseGenerator.generateRandomArray;

public class TestBubbleSort {

    private static <T extends Comparable<T>> long testAlgo(Sorter<T> sortingAlgo, T[] array) {
        long startTime = System.nanoTime();
        sortingAlgo.sort(array);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static void generateResultCSV(int index, String sorter, String dataType, int size, long executionTime,
            String sortOrder,
            BufferedWriter bufferedWriter) {
        String content = String.format("%d, %s, %s, %s, %d, %d %n", index + 1, sorter, sortOrder, dataType, size,
                executionTime);
        try {
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BubbleSortPassPerItem<Float> ppiSorter = new BubbleSortPassPerItem<>();
        BubbleSortUntilNoChange<Float> uncSorter = new BubbleSortUntilNoChange<>();
        BubbleSortWhileNeeded<Float> wnSorter = new BubbleSortWhileNeeded<>();

        String sortOrder = "Descending";
        int arraySize = 10000;
        String filenamePpi = sortOrder + "_ppi_" + "Float_" + Integer.toString(arraySize) + ".csv";
        String filenameUnc = sortOrder + "_unc_" + "Float_" + Integer.toString(arraySize) + ".csv";
        String filenameWn = sortOrder + "_wn_" + "Float_" + Integer.toString(arraySize) + ".csv";

        try {
            File filePpi = new File(filenamePpi);
            filePpi.createNewFile();
            FileWriter fileWriterPpi = new FileWriter(filePpi);
            BufferedWriter bufferedWriterPpi = new BufferedWriter(fileWriterPpi);
            if (new File(filenamePpi).length() == 0) {
                bufferedWriterPpi.write("Index, SortingAlgo, SortingType, DataType, DataSize, ExeTime \n");
            }
            File fileUnc = new File(filenameUnc);
            filePpi.createNewFile();
            FileWriter fileWriterUnc = new FileWriter(fileUnc);
            BufferedWriter bufferedWriterUnc = new BufferedWriter(fileWriterUnc);
            if (new File(filenameUnc).length() == 0) {
                bufferedWriterUnc.write("Index, SortingAlgo, SortingType, DataType, DataSize, ExeTime \n");
            }
            File fileWn = new File(filenameWn);
            fileWn.createNewFile();
            FileWriter fileWriterWn = new FileWriter(fileWn);
            BufferedWriter bufferedWriterWn = new BufferedWriter(fileWriterWn);
            if (new File(filenameWn).length() == 0) {
                bufferedWriterWn.write("Index, SortingAlgo, SortingType, DataType, DataSize, ExeTime \n");
            }
            for (int i = 0; i < 100; i++) {
                Float[] ppiArray = generateRandomArray(Float.class, arraySize, sortOrder);
                Float[] uncArray = Arrays.copyOf(ppiArray, arraySize);
                Float[] wnArray = Arrays.copyOf(ppiArray, arraySize);

                long executionTimePpi = testAlgo(ppiSorter, ppiArray);
                long executionTimeUnc = testAlgo(uncSorter, uncArray);
                long executionTimeWn = testAlgo(wnSorter, wnArray);

                generateResultCSV(i, "PassPerItem", "Float", arraySize, executionTimePpi, sortOrder,
                        bufferedWriterPpi);
                generateResultCSV(i, "UntilNoChange", "Float", arraySize, executionTimeUnc, sortOrder,
                        bufferedWriterUnc);
                generateResultCSV(i, "WhileNeeded", "Float", arraySize, executionTimeWn, sortOrder, bufferedWriterWn);
            }
            bufferedWriterPpi.close();
            bufferedWriterUnc.close();
            bufferedWriterWn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
