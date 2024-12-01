package annotations.example.app.databases;

import annotations.example.annotation.InitializerClass;
import annotations.example.annotation.InitializerMethod;

@InitializerClass
public class CacheLoader {

    @InitializerMethod
    public void loadCache() {
        System.out.println("Loading data from cache");
    }

    @InitializerMethod
    public void reloadCache() {
        System.out.println("Reloading cache");
    }
}
