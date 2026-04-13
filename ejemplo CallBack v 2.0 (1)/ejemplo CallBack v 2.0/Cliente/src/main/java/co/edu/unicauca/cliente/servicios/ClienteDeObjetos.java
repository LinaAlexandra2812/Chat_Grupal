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
            int numPuertoRMIRegistry = 1099;
            String direccionIpRMIRegistry = "localhost";

            java.util.Properties prop = new java.util.Properties();
            java.io.File file = new java.io.File("config.properties");
            if (!file.exists()) {
                file = new java.io.File("../config.properties");
            }

            if (file.exists()) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                    prop.load(fis);
                    direccionIpRMIRegistry = prop.getProperty("server.ip", "localhost");
                    numPuertoRMIRegistry = Integer.parseInt(prop.getProperty("server.port", "1099"));
                    System.out.println("Configuración cargada desde " + file.getAbsolutePath() + ": IP=" + direccionIpRMIRegistry + ", Puerto=" + numPuertoRMIRegistry);
                } catch (Exception e) {
                    System.err.println("Error al leer el archivo de propiedades: " + e.getMessage());
                }
            } else {
                System.out.println("No se encontró config.properties. Usando valores predeterminados (localhost:1099).");
            }

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
                System.out.println("2. Ver lista de usuarios activos");
                System.out.println("3. Consultar cantidad de usuarios activos");
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
                        System.out.println("Lista de usuarios activos (" + usuarios.size() + "):");
                        for (String u : usuarios)
                        {
                            System.out.println("  - " + u);
                        }
                        break;

                    case 3:
                        int cantidad = servidor.consultarCantidadUsuarios();
                        System.out.println("Cantidad de usuarios activos consultada al servidor: " + cantidad);
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
