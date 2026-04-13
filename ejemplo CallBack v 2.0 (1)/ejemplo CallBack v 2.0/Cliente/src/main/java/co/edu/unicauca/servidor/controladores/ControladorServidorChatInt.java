package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ControladorServidorChatInt extends Remote
{
    boolean registrarReferenciaUsuario(String nickName, UsuarioCllbckInt usuario) throws RemoteException;
    void enviarMensaje(String mensaje) throws RemoteException;
    void enviarMensajePrivado(String emisor, String destino, String mensaje) throws RemoteException;
    void desconectarUsuario(String nickName) throws RemoteException;
    List<String> obtenerUsuarios() throws RemoteException;
    int consultarCantidadUsuarios() throws RemoteException;
}


