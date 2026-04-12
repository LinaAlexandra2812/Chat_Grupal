package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {

    private final Map<String, UsuarioCllbckInt> usuarios;// lista que almacena la referencia remota de los clientes

    public ControladorServidorChatImpl() throws RemoteException {
        super();// asignamos el puerto
        usuarios = new HashMap<>();
    }

    @Override
    public synchronized boolean registrarReferenciaUsuario(String nickName, UsuarioCllbckInt usuario)
            throws RemoteException {
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
        System.out.println("Invocando al método desconectar usuario desde el servidor");
        usuarios.remove(nickName);
        notificarUsuarios("*** " + nickName + " ha salido del chat ***");
    }

    @Override
    public synchronized void enviarMensaje(String mensaje) throws RemoteException {
        notificarUsuarios(mensaje);
    }

    private synchronized void notificarUsuarios(String mensaje) throws RemoteException {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        Iterator<Map.Entry<String, UsuarioCllbckInt>> it = usuarios.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, UsuarioCllbckInt> entry = it.next();
            try {
                entry.getValue().notificar(mensaje, usuarios.size());
            } catch (RemoteException e) {
                System.out.println("Cliente '" + entry.getKey() + "' no responde. Eliminando del registro.");
                it.remove();
                notificarUsuarios("*** " + entry.getKey() + " ha salido del chat ***");
            }
        }
    }

    public List<String> obtenerUsuarios() throws RemoteException {
        return new ArrayList<>(usuarios.keySet());
    }

}
