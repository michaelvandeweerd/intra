package eu.parcifal.intra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.parcifal.intra.http.HTTPListener;
import eu.parcifal.plus.print.Console;

public class Server implements Runnable {

    private static final String WARNING_NO_LISTENERS = "cannot stop listening as no listeners are started, run command \"start\" first";

    private static final String MESSAGE_QUIT = "closing down the server";

    private static final String WARNING_INVALID_COMMAND = "invalid command \"%1$s\"";

    private static final String MESSAGE_RESTART = "restarting the server";

    private static final String MESSAGE_STOP = "stopping the server";

    private static final String WARNING_ILLEGAL_STATE = "cannot start listening, run command \"stop\" first";

    private static final String MESSAGE_START = "starting the server";

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
                    Console.log(MESSAGE_START);

                    try {
                        this.start();
                    } catch (IllegalStateException exception) {
                        Console.log(WARNING_ILLEGAL_STATE);
                    }
                    break;
                case "stop":
                    Console.log(MESSAGE_STOP);

                    this.stop();
                    break;
                case "restart":
                    Console.log(MESSAGE_RESTART);
                    this.restart();
                    break;
                case "quit":
                    Console.log(MESSAGE_QUIT);
                    this.quit();
                    break;
                default:
                    Console.log(WARNING_INVALID_COMMAND, line);
                    break;
                }
            } catch (IOException exception) {
                Console.warn(exception);
            }
        }

        this.stop();
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

    public void stop() throws NullPointerException {
        if (this.listeners != null) {
            for (HTTPListener listener : this.listeners) {
                listener.stop();
            }

            this.listeners = null;
        } else {
            Console.log(WARNING_NO_LISTENERS);
        }
    }

    public void restart() {
        this.stop();
        this.start();
    }

    public void quit() {
        this.running = false;
    }

}
