package annotations.example;

import annotations.example.annotation.InitializerClass;
import annotations.example.annotation.InitializerMethod;
import annotations.example.annotation.RetryOperation;
import annotations.example.annotation.ScanPackages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ScanPackages({"app", "app.configs", "app.databases", "app.http"})
public class Main {

    public static void main(String[] args) throws Throwable {
        initialize();
    }

    public static void initialize() throws Throwable {
        ScanPackages annotation = Main.class.getAnnotation(ScanPackages.class);
        String[] packageNames = annotation.value();

        if (packageNames == null || packageNames.length == 0) {
            return;
        }

        List<Class<?>> classes = getAllClasses(packageNames);

        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(InitializerClass.class)) {
                continue;
            }

            List<Method> methods = getAllInitializingMethods(clazz);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            for (Method method : methods) {
                callIntializingMethod(instance, method);
            }
        }
    }

    private static void callIntializingMethod(Object instance, Method method) throws Throwable {
        RetryOperation annotation = method.getAnnotation(RetryOperation.class);

        int numberOfRetries = annotation == null ? 0 : annotation.numberOfRetries();

        while (true) {
            try {
                method.invoke(instance);
                break;
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();

                if (numberOfRetries > 0 && Set.of(annotation.retryExceptions()).contains(targetException.getClass())) {
                    numberOfRetries--;

                    System.out.println("Retrying...");
                    Thread.sleep(annotation.durationBetweenRetriesMs());
                } else if (annotation != null) {
                    throw new RuntimeException(annotation.failureMessage(), targetException);
                } else {
                    throw targetException;
                }
            }
        }
    }

    private static List<Method> getAllInitializingMethods(Class<?> clazz) {
        List<Method> initializingMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InitializerMethod.class)) {
                initializingMethods.add(method);
            }
        }
        return initializingMethods;
    }

    public static List<Class<?>> getAllClasses(String... packageNames) throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> allClasses = new ArrayList<>();

        for (String packageName : packageNames) {
            String packageRelativePath = packageName.replace('.', '/');

            URI packageUri = Main.class.getResource(packageRelativePath).toURI();
            System.out.println(packageUri);

            if (packageUri.getScheme().equals("file")) {
                Path packageFullPath = Paths.get(packageUri);
                allClasses.addAll(getAllPackageClasses(packageFullPath, packageName));
            } else if (packageUri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());

                Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
                allClasses.addAll(getAllPackageClasses(packageFullPathInJar, packageName));

                fileSystem.close();
            }
        }
        return allClasses;
    }

    private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName) throws IOException, ClassNotFoundException {

        if (!Files.exists(packagePath)) {
            return Collections.emptyList();
        }

        List<Path> files = Files.list(packagePath)
                .filter(Files::isRegularFile)
                .toList();

        List<Class<?>> classes = new ArrayList<>();

        for (Path filePath : files) {
            String fileName = filePath.getFileName().toString();

            if (fileName.endsWith(".class")) {
                String classFullName = packageName.isBlank() ?
                        fileName.replaceFirst("\\.class$", "")
                        : packageName + "." + fileName.replaceFirst("\\.class$", "");
                System.out.println(classFullName);
                Class<?> clazz = Class.forName("annotations.example." + classFullName);
                classes.add(clazz);
            }
        }
        return classes;
    }
}
