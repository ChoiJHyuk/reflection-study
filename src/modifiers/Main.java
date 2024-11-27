package modifiers;


import modifiers.auction.Auction;
import modifiers.auction.Bid;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
//        printClassModifiers(Class.forName("modifiers.auction.Bid$Builder$BidImpl"));
//        printMethodsModifiers(Auction.class.getDeclaredMethods());
        printFieldsModifiers(Auction.class.getDeclaredFields());
    }

    public static void printFieldsModifiers(Field[] fields) {
        for (Field field : fields) {
            int modifier = field.getModifiers();

            System.out.printf("Field \"%s\" access modifier is %s\n",
                    field.getName(),
                    getAccessModifierName(modifier));

            if (Modifier.isVolatile(modifier)) {
                System.out.println("The field is volatile");
            }

            if (Modifier.isTransient(modifier)) {
                System.out.println("The field is transient and will not be serialized");
            }
            if (Modifier.isFinal(modifier)) {
                System.out.println("The field is final");
            }
            System.out.println();
        }
    }

    public static void printMethodsModifiers(Method[] methods) {
        for (Method method : methods) {
            int modifier = method.getModifiers();

            System.out.printf("%s() access modifier is %s\n",
                    method.getName(),
                    getAccessModifierName(modifier));

            if (Modifier.isSynchronized(modifier)) {
                System.out.println("The method is synchronized");
            } else {
                System.out.println("The method is not synchronized");
            }
            System.out.println();
        }
    }

    public static void printClassModifiers(Class<?> clazz) {
        int modifier = clazz.getModifiers();

        System.out.printf("Class %s access modifier is %s\n",
                clazz.getSimpleName(),
                getAccessModifierName(modifier));

        if (Modifier.isAbstract(modifier)) {
            System.out.printf("The class is abstract\n");
        }

        if (Modifier.isInterface(modifier)) {
            System.out.printf("The class is an interface\n");
        }

        if (Modifier.isStatic(modifier)) {
            System.out.printf("The class is static\n");
        }
    }

    private static String getAccessModifierName(int modifier) {
        if (Modifier.isPublic(modifier)) {
            return "public";
        } else if (Modifier.isProtected(modifier)) {
            return "protected";
        } else if (Modifier.isPrivate(modifier)) {
            return "private";
        } else {
            return "package-private";
        }

    }

    public static void runAuction() {
        Auction auction = new Auction();
        auction.startAuction();

        Bid bid1 = Bid.builder()
                .setBidderName("Company1")
                .setPrice(10)
                .build();
        auction.addBid(bid1);

        Bid bid2 = Bid.builder()
                .setBidderName("Company2")
                .setPrice(12)
                .build();
        auction.addBid(bid2);

        auction.stopAuction();

        System.out.println(auction.getAllBids());
        System.out.println("Highest bid :" + auction.getHighestBid().get());
    }
}
