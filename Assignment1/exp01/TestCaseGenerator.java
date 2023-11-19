package exp01;

import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

public class TestCaseGenerator {

    public static <T extends Comparable<T>> T[] generateRandomArray(Class<T> type, int size, String sortOrder) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(type, size);
        Random randomizer = new Random();

        for (int i = 0; i < size; i++) {
            if (type.equals(Integer.class)) {
                newArray[i] = (T) Integer.valueOf(randomizer.nextInt(1000));
            } else if (type.equals(Double.class)) {
                newArray[i] = (T) Double.valueOf(randomizer.nextDouble(1000));
            } else if (type.equals(Float.class)) {
                newArray[i] = (T) Float.valueOf(randomizer.nextFloat(1000));
            }
        }

        switch (sortOrder) {
            case "Ascending":
                Arrays.sort(newArray);
                break;
            case "Descending":
                Arrays.sort(newArray, Collections.reverseOrder());
                break;
            default:
                break;
        }

        return newArray;
    }
}
