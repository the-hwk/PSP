package server;

import server.enums.Command;

import java.util.Scanner;

public class ConsoleThread implements Runnable {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Console started! Write LIST to get all commands");

        boolean work = true;

        do {
            try {
                Command command = Command.valueOf(scanner.nextLine().toUpperCase());

                switch (command) {
                    case LIST:
                        printAllCommands();
                        break;
                    case STOP_SERVER:
                        UPDServer.ServerStatus.setStopped(true);
                        work = false;
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("No such command!");
            }
        } while (work);
    }

    private void printAllCommands() {
        System.out.println("All commands:");
        for (Command value : Command.values()) {
            System.out.println(value);
        }
    }
}