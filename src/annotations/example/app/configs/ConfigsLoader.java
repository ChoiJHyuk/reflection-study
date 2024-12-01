package annotations.example.app.configs;

import annotations.example.annotation.InitializerClass;
import annotations.example.annotation.InitializerMethod;

@InitializerClass
public class ConfigsLoader {

    @InitializerMethod
    public void loadAllConfigs() {
        System.out.println("Loading all configuration files");
    }
}
