package eu.parcifal.intra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.parcifal.intra.http.HTTPListener;
import eu.parcifal.plus.print.Console;

public class Server implements Runnable {

    private Configuration configuration;

    private HTTPListener[] listeners;

    private boolean running;

    public Server() {
        this.configuration = new Configuration();
    }

    @Override
    public void run() {
        this.running = true;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (this.running) {
            try {
                String line = reader.readLine();

                switch (line) {
                case "start":
                    System.out.println("Starting the server.");

                    try {
                        this.start();
                    } catch (IllegalStateException exception) {
                        System.out.println("Cannot start listening, run command \"stop\" first.");
                    }
                    break;
                case "stop":
                    System.out.println("Stopping the server.");
                    this.stop();
                    break;
                case "restart":
                    System.out.println("Restarting the server.");
                    this.restart();
                    break;
                default:
                    System.out.println(String.format("Invalid command \"%1$s\".", line));
                    break;
                }
            } catch (IOException exception) {
                Console.warn(exception);
            }
        }
    }

    public void start() {
        this.configuration.initialise();
        
        if (this.listeners != null) {
            throw new IllegalStateException();
        } else {
            this.listeners = configuration.listeners();

            for (HTTPListener listener : this.listeners) {
                new Thread(listener).start();
            }
        }
    }

    public void stop() {
        for(HTTPListener listener : this.listeners) {
            listener.stop();
        }
        
        this.listeners = null;
    }

    public void restart() {
        this.stop();
        this.start();
    }

}
