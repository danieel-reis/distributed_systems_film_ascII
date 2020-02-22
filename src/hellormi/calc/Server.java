/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellormi.calc;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author daniel
 */
public class Server implements Calculadora {

    public Server() {
    }

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            Calculadora stub = (Calculadora) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Calculadora", stub);
            System.err.println("Server ready");
        } catch (RemoteException r) {
            System.err.println("Server exception: " + r.toString());
            r.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public double soma(double a, double b) throws RemoteException {
        return a + b;
    }

    public double subtrai(double a, double b) throws RemoteException {
        return a - b;
    }

    public double multiplica(double a, double b) throws RemoteException {
        return a * b;
    }

    public double divide(double a, double b) throws RemoteException {
        if (b > 0) {
            return a / b;
        }
        return 0;
    }

}
