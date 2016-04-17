package eu.parcifal.intra;

import eu.parcifal.plus.print.Console;

public class Main {

    public static void main(String[] args) {
        //Console.printDebug(true);
        
        Server server = new Server();

        server.run();
    }

}
