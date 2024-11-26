package methods.test;

import methods.api.ClothingProduct;
import methods.api.Product;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ProductTest {

    public static void main(String[] args) {
        testGetters(ClothingProduct.class);
        testSetters(ClothingProduct.class);
    }

    public static void testSetters(Class<?> dataClass) {
        List<Field> fields = getAllFields(dataClass);

        for (Field field : fields) {
            String setterName = "set" + capitalizeFirstLetter(field.getName());

            Method setterMethod = null;
            try {
                setterMethod = dataClass.getMethod(setterName, field.getType());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Setter : %s not found", setterName));
            }

            if (!setterMethod.getReturnType().equals(void.class)) {
                throw new IllegalStateException(String.format("Setter method : %s has to return void", setterName));
            }
        }
    }

    public static void testGetters(Class<?> dataClass) {
        List<Field> fields = getAllFields(dataClass);

        Map<String, Method> nameToMethod = mapMethodNameToMethod(dataClass);

        for (Field field : fields) {
            String getterName = "get" + capitalizeFirstLetter(field.getName());

            if (!nameToMethod.containsKey(getterName)) {
                throw new IllegalStateException(String.format("Field : %s doesn't have a getter method", field.getName()));
            }

            Method getter = nameToMethod.get(getterName);

            if (!getter.getReturnType().equals(field.getType())) {
                throw new IllegalStateException(String.format("Getter method : %s() has return type %s but expected %s",
                        getter.getName(), getter.getReturnType(), field.getType()));
            }

            if (getter.getParameterCount() > 0) {
                throw new IllegalStateException(String.format("Getter : %s has arguments", getter.getName()));
            }
        }
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return Collections.emptyList();
        }

        List<Field> allFields = new ArrayList<>();

        allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        allFields.addAll(getAllFields(clazz.getSuperclass()));

        return allFields;
    }

    private static String capitalizeFirstLetter(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    private static Map<String, Method> mapMethodNameToMethod(Class<?> dataClass) {
        Method[] allMethods = dataClass.getMethods();

        Map<String, Method> nameToMethod = new HashMap<>();

        for (Method method : allMethods) {
            String methodName = method.getName();

            nameToMethod.put(methodName, method);
        }

        return nameToMethod;
    }
}
