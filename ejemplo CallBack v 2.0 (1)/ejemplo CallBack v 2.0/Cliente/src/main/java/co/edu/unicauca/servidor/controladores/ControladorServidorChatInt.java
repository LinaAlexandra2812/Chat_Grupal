package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControladorServidorChatInt extends Remote
{
    boolean registrarReferenciaUsuario(String nickName, UsuarioCllbckInt usuario) throws RemoteException;
    public void enviarMensaje(String mensaje)throws RemoteException;
}


