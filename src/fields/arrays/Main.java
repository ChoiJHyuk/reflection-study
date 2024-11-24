package fields.arrays;

import java.lang.reflect.Array;

public class Main {

    public static void main(String[] args) {
        int[] oneDimensionalArray = {1, 2};

        double[][] twoDimensionalArray = {{1.5, 2.5}, {3.5, 4.5}};

//        inspectArrayObject(twoDimensionalArray);
        inspectArrayValues(twoDimensionalArray);
    }

    public static void inspectArrayValues(Object arrayObject) {
        int arrayLength = Array.getLength(arrayObject);

        for (int i = 0; i < arrayLength; i++) {
            Object arrayElement = Array.get(arrayObject, i);

            if (arrayElement.getClass().isArray()) {
                inspectArrayValues(arrayElement);
            } else {
                System.out.print(arrayElement + " ");
            }
        }
    }

    public static void inspectArrayObject(Object arrayObject) {
        Class<?> clazz = arrayObject.getClass();

        System.out.printf("Is array : %s\n", clazz.isArray());

        Class<?> arrayComponentType = clazz.getComponentType();

        System.out.printf("This is an array of type : %s\n", arrayComponentType.getTypeName());
    }
}
