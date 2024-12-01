package annotations.example.app.http;

import annotations.example.annotation.InitializerClass;
import annotations.example.annotation.InitializerMethod;

@InitializerClass
public class ServiceRegistry {

    @InitializerMethod
    public void registerService() {
        System.out.println("Service successfully registered");
    }
}
