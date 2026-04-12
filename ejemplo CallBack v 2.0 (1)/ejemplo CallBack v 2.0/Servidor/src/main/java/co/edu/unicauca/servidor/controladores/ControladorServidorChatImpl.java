package co.edu.unicauca.servidor.controladores;


import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {

    private final Map<String, UsuarioCllbckInt> usuarios;//lista que almacena la referencia remota de los clientes

    public ControladorServidorChatImpl() throws RemoteException
    {
        super();//asignamos el puerto 
        usuarios= new HashMap<>();
    }
    
    @Override
    public synchronized boolean registrarReferenciaUsuario(String nickName, UsuarioCllbckInt usuario) throws RemoteException {
        System.out.println("Invocando al metodo registrar usuario desde el servidor");

        if (nickName == null || nickName.isBlank() || usuario == null) {
            return false;
        }

        if (usuarios.containsKey(nickName)) {
            return false;
        }

        usuarios.put(nickName, usuario);
        return true;
    }

    @Override
    public synchronized void desconectarUsuario(String nickName) throws RemoteException {
        usuarios.remove(nickName);
    }
   
    @Override
    public void enviarMensaje(String mensaje)throws RemoteException 
    {        
        notificarUsuarios("un cliente envio el siguiente mensaje: " + mensaje);
    }
    
    private void notificarUsuarios(String mensaje) throws RemoteException 
    {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        for (UsuarioCllbckInt objUsuario : usuarios.values()) 
        {
            objUsuario.notificar(mensaje, usuarios.size());
        }
    }

    public List <String> obtenerUsuarios() throws RemoteException
    {
        return new ArrayList<>(usuarios.keySet());
    }

    
}
