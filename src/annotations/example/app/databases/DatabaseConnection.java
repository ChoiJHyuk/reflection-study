package annotations.example.app.databases;

import annotations.example.annotation.InitializerClass;
import annotations.example.annotation.InitializerMethod;
import annotations.example.annotation.RetryOperation;

import java.io.IOException;

@InitializerClass
public class DatabaseConnection {

    private int failCounter = 5;

    @InitializerMethod
    @RetryOperation(numberOfRetries = 10, retryExceptions = IOException.class,
            durationBetweenRetriesMs = 1000, failureMessage = "Connection to database 1 failed after retries")
    public void connectToDatabase1() throws IOException {
        System.out.println("Connecting to database 1");
        if (failCounter > 0) {
            failCounter--;
            throw new IOException("Connection failed");
        }

        System.out.println("Connection to database 1 successful");
    }

    @InitializerMethod
    public void connectToDatabase2() {
        System.out.println("Connecting to database 2");
    }
}
