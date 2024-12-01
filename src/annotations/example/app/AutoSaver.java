package annotations.example.app;

import annotations.example.annotation.InitializerClass;
import annotations.example.annotation.InitializerMethod;

@InitializerClass
public class AutoSaver {

    @InitializerMethod
    public void startAutoSavingThreads() {
        System.out.println("Start automatic data saving to disk");
    }
}
