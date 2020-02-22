/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellormi.calc;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author daniel
 */
public interface Calculadora extends Remote {
    double soma(double a, double b) throws RemoteException;
    double subtrai(double a, double b) throws RemoteException;
    double multiplica(double a, double b) throws RemoteException;
    double divide(double a, double b) throws RemoteException;
}
