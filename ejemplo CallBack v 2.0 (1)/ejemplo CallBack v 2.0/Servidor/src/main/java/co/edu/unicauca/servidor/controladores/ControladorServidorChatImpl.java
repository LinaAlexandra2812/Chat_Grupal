package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {

    private final HashMap<String, UsuarioCllbckInt> usuarios;// lista que almacena la referencia remota de los clientes

    public ControladorServidorChatImpl() throws RemoteException {
        super();// asignamos el puerto
        usuarios = new HashMap<String, UsuarioCllbckInt>();
    }

    /**
     * Corregir para que el servidor pueda registrar la referencia remota del
     * cliente, para esto se debe almacenar la referencia remota del cliente en
     * una lista, para luego poder hacer el callback a los clientes cada vez que
     * se envie un mensaje al servidor
     */
    @Override
    public synchronized boolean registrarReferenciaUsuario(UsuarioCllbckInt usuario) throws RemoteException {
        // método que unicamente puede ser accedido por un hilo
        System.out.println("Invocando al método registrar usuario desde el servidor");
        boolean bandera = false;
        if (!usuarios.containsKey(usuario)) {
            bandera = usuarios.put(usuario.toString(), usuario) == null ? true : false;
        }
        return bandera;
    }

    @Override
    public void desconectarUsuario(String nickname) {
        System.out.println("Invocando al método desconectar usuario desde el servidor");
        if (usuarios.containsKey(nickname)) {
            usuarios.remove(nickname);
        }

    }

    @Override
    public void enviarMensaje(String mensaje) throws RemoteException {
        notificarUsuarios("un cliente envio el siguiente mensaje: " + mensaje);
    }

    private void notificarUsuarios(String mensaje) throws RemoteException {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        for (UsuarioCllbckInt objUsuario : usuarios.values()) {
            objUsuario.notificar(mensaje, usuarios.size());// el servidor hace el callback

        }
    }

    public List<String> obtenerUsuarios() throws RemoteException {
        return new ArrayList<>(usuarios.keySet());
    }
}
