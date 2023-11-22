package finaltask.server;

import finaltask.server.KVServer;

import java.io.IOException;

public class KVServerStarter {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
    }
}
