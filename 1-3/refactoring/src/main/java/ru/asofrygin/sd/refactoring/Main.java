package ru.asofrygin.sd.refactoring;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        StorageServer server = new StorageServer("jdbc:sqlite:test.db", 8081);
        server.start();
    }
}