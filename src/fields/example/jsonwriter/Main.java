package fields.example.jsonwriter;

import fields.example.data.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        Address address = new Address("Main Street", (short) 1);
        Company company = new Company("Udemy", "San Francisco", new Address("Harrison Street", (short) 600));
        Person person = new Person("John", true, 29, 100.555f, address, company);

        String json1 = objectToJson(person);

        System.out.println(json1);

        Actor actor1 = new Actor("Elijah Wood", new String[]{"Lord of the Rings", "The Good Son"});
        Actor actor2 = new Actor("Ian McKellen", new String[]{"X-Men", "Hobbit"});
        Actor actor3 = new Actor("Orlando Bloom", new String[]{"Pirates of the Caribbean", "Kingdom of Heaven"});

        Movie movie = new Movie("Lord of the Rings", 8.8f, new String[]{"Action", "Adventure", "Drama"},
                new Actor[]{actor1, actor2, actor3});

        String json2 = objectToJson(movie);

        System.out.println(json2);
    }

    public static String objectToJson(Object instance) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) continue;

            stringBuilder.append(formatStringValue(field.getName()));
            stringBuilder.append(":");
            if (field.getType().isPrimitive()) {
                stringBuilder.append(formatPrimitiveValue(field.get(instance), field.getType()));
            } else if (field.getType().equals(String.class)) {
                stringBuilder.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isArray()) {
                stringBuilder.append(arrayToJson(field.get(instance)));
            } else {
                stringBuilder.append(objectToJson(field.get(instance)));
            }

            if (i != fields.length - 1) stringBuilder.append(",");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static String arrayToJson(Object arrayInstance) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");

        int arrayLength = Array.getLength(arrayInstance);

        Class<?> componentType = arrayInstance.getClass().getComponentType();

        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayInstance, i);

            if (componentType.isPrimitive()) {
                stringBuilder.append(formatPrimitiveValue(element, componentType));
            } else if (componentType.equals(String.class)) {
                stringBuilder.append(formatStringValue(element.toString()));
            } else {
                stringBuilder.append(objectToJson(element));
            }

            if (i != arrayLength - 1) stringBuilder.append(",");
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    private static String formatPrimitiveValue(Object instance, Class<?> type) {
        if (type.equals(boolean.class) || type.equals(int.class)
                || type.equals(long.class) || type.equals(short.class)) {
            return instance.toString();
        } else if (type.equals(double.class) || type.equals(float.class)) {
            return String.format("%.2f", instance);
        }

        throw new RuntimeException(String.format("Unsupported field type: %s", type.getName()));
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
