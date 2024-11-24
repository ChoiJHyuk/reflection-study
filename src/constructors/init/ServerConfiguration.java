package constructors.init;

import java.net.InetSocketAddress;

public class ServerConfiguration {
    private static ServerConfiguration serverConfiguration;

    private final InetSocketAddress serverAddress;
    private final String greetingMessage;

    private ServerConfiguration(int port, String greetingMessage) {
        this.greetingMessage = greetingMessage;
        this.serverAddress = new InetSocketAddress("localhost", port);

        if (serverConfiguration == null) {
            serverConfiguration = this;
        }
    }

    public static ServerConfiguration getInstance() {
        return serverConfiguration;
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public String getGreetingMessage() {
        return greetingMessage;
    }
}
