package examprep3.socket_and_thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnstilesThread implements Runnable {

    Socket s;
    volatile TurnstilesControl tc;
    BufferedReader in;
    PrintWriter out;
    String helpText;
    String commandNotFoundText;

    String command;
    String value;
    int id;

    public TurnstilesThread(Socket s, TurnstilesControl tc) throws IOException {
        this.s = s;
        this.tc = tc;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);

        helpText = "\n"
                + "Type: REGISTER#ID - To register a new turnstile with specific ID\n"
                + "Type: INCREMENT#ID - To incement the count for a specifik turnstile\n"
                + "Type: MONITOR#ALL - To get the total count for all turnstiles\n"
                + "Type: MONITOR#ID - To get the count for a specific turnstile\n"
                + "Type: disconnect - To disconnect from the server\n";
        commandNotFoundText = "Command not found, type \"help\" for a list of commands";
    }

    @Override
    public void run() {
        String input = "";
        while (!out.checkError()) {
            try {
                input = in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(TurnstilesThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            switch (input) {
                case "":
                    continue;
                case "help":
                    out.println(helpText);
                    continue;
                case "disconnect": {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TurnstilesThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
            }
            if (!CheckCommand(input)) {
                out.println(commandNotFoundText);
                continue;
            }

            switch (command) {
                case "REGISTER":
                    out.println(register());
                    continue;
                case "INCREMENT":
                    out.println(increment());
                    continue;
                case "MONITOR":
                    out.println(monitor());
                    continue;
            }

            out.println(commandNotFoundText);
        }
    }

    private boolean CheckCommand(String input) {
        String[] inputParts;
        String[] availableCommands = new String[]{"REGISTER", "INCREMENT", "MONITOR"};

        try {
            inputParts = input.split("[#]");
        } catch (Exception e) {
            return false;
        }
        if (inputParts.length != 2) {
            return false;
        }
        
        command = inputParts[0];
        value = inputParts[1];
        
        if((value.equals("ALL") && !command.equals("MONITOR")) || !Arrays.asList(availableCommands).contains(command)){
            return false;
        }
        if (!value.equals("ALL")) {
            try {
                id = Integer.parseInt(value);
                value = "";
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
    
    private String register(){
        if(tc.registerTurnstile(id)){
            return "Turnstile with id " + id + " was successfully registered";
        }
        
        return "There is already a turnstile with id " + id + " in the system";
    }
    
    private String increment(){
        if(tc.incrementTurnstile(id))
            return "Turnstile with id " + id + " has been incemented";
        return "No turnstile with id " + id + " was found";
    }
    
    private String monitor(){
        long count;
        if(value.equals("")){
             count = tc.getTurnstileCount(id);

            if(count < 0){
                return "No turnstile with id " + id + " was found";
            }

            return "Turnstile with id " + id + " have turned " + count + " times";
        }
        
        count = tc.getTurnstilesCount();
        
        return "Trunstiles have in total turned " + count + " times";
    }
}
