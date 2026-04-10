package co.edu.unicauca.servidor.controladores;

import java.util.List;
import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControladorServidorChatInt extends Remote
{
    public boolean registrarReferenciaUsuario(UsuarioCllbckInt  usuario) throws RemoteException;
    public void desconectarUsuario(String nickname);
    public void enviarMensaje(String mensaje)throws RemoteException;
    public List <String> obtenerUsuarios() throws RemoteException;
}


