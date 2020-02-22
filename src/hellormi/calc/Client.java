/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellormi.calc;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author daniel
 */
public class Client {

    private Client() {
    }

    public static void main(String args[]) {
        String host = (args.length < 1) ? null : args[0];
        double a = Double.parseDouble(args[1]);
        double b = Double.parseDouble(args[2]);
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Calculadora stub = (Calculadora) registry.lookup("Calculadora");
            System.err.println("a + b: " + stub.soma(a, b));
            System.err.println("a - b: " + stub.subtrai(a, b));
            System.err.println("a * b: " + stub.multiplica(a, b));
            System.err.println("a / b: " + stub.divide(a, b));
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
