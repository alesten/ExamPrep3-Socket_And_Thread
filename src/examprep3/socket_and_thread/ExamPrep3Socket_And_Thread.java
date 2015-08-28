/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examprep3.socket_and_thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 *
 * @author Alexander
 */
public class ExamPrep3Socket_And_Thread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String ip = "localhost";
        int port = 1234;
        
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip,port));
        
        TurnstilesControl turnstilesControl = new TurnstilesControl();
        
        while(true){
            TurnstilesThread tt = new TurnstilesThread(ss.accept(), turnstilesControl);
            new Thread(tt).start();
        }
    }
    
}
