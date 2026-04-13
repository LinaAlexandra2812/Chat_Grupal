package co.edu.unicauca.cliente.servicios;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckImpl;
import co.edu.unicauca.cliente.utilidades.UtilidadesConsola;
import co.edu.unicauca.cliente.utilidades.UtilidadesRegistroC;
import co.edu.unicauca.servidor.controladores.ControladorServidorChatInt;
import java.util.List;

public class ClienteDeObjetos
{
    public static void main(String[] args)
    {
        try
        {
            int numPuertoRMIRegistry = 0;
            String direccionIpRMIRegistry = "";
            System.out.println("Ingrese la dirección IP donde se encuentra el rmiregistry: ");
            direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
            System.out.println("Ingrese el número de puerto por el cual escucha el rmiregistry: ");
            numPuertoRMIRegistry = UtilidadesConsola.leerEntero();

            ControladorServidorChatInt servidor = (ControladorServidorChatInt)
                    UtilidadesRegistroC.obtenerObjRemoto(numPuertoRMIRegistry, direccionIpRMIRegistry, "ServidorChat");

            System.out.println("Digite su nickName: ");
            String nickName = UtilidadesConsola.leerCadena();

            UsuarioCllbckImpl objNuevoUsuario = new UsuarioCllbckImpl();
            boolean registrado = servidor.registrarReferenciaUsuario(nickName, objNuevoUsuario);

            if (!registrado)
            {
                System.out.println("No fue posible registrar el usuario. Nick repetido o inválido.");
                return;
            }

            System.out.println("¡Bienvenido al chat, " + nickName + "!");

            // Shutdown hook: si la terminal se cierra de forma abrupta, se notifica al servidor
            Runtime.getRuntime().addShutdownHook(new Thread(() ->
            {
                try { servidor.desconectarUsuario(nickName); }
                catch (Exception ignored) {}
            }));

            boolean continuar = true;
            while (continuar)
            {
                System.out.println("\n=== MENÚ ===");
                System.out.println("1. Enviar mensaje");
                System.out.println("2. Ver usuarios activos");
                System.out.println("3. Enviar mensaje privado");
                System.out.println("4. Salir del chat");
                int opcion = UtilidadesConsola.leerEntero();

                switch (opcion)
                {
                    case 1:
                        System.out.println("Escriba el mensaje: ");
                        String mensaje = UtilidadesConsola.leerCadena();
                        servidor.enviarMensaje("[" + nickName + "]: " + mensaje);
                        break;

                    case 2:
                        List<String> usuarios = servidor.obtenerUsuarios();
                        System.out.println("Usuarios activos (" + usuarios.size() + "):");
                        for (String u : usuarios)
                        {
                            System.out.println("  - " + u);
                        }
                        break;

                    case 3:
                        System.out.println("Digite el nickName del usuario destino: ");
                        String nickDestino = UtilidadesConsola.leerCadena();

                        if (nickDestino == null || nickDestino.isBlank())
                        {
                            System.out.println("Debe ingresar un nickName destino válido.");
                            break;
                        }

                        System.out.println("Escriba el mensaje privado: ");
                        String mensajePrivado = UtilidadesConsola.leerCadena();
                        servidor.enviarMensajePrivado(nickName, nickDestino, mensajePrivado);
                        break;

                    case 4:
                        servidor.desconectarUsuario(nickName);
                        System.out.println("Ha salido del chat. ¡Hasta luego!");
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("No se pudo realizar la conexión...");
            System.out.println(e.getMessage());
        }
    }
}
