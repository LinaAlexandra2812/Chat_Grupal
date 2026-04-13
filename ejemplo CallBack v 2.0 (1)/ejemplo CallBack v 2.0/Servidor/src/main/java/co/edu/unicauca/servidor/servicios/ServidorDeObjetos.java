
package co.edu.unicauca.servidor.servicios;

import co.edu.unicauca.servidor.controladores.ControladorServidorChatImpl;
import co.edu.unicauca.servidor.utilidades.UtilidadesConsola;
import co.edu.unicauca.servidor.utilidades.UtilidadesRegistroS;
import java.rmi.RemoteException;

public class ServidorDeObjetos
{
    public static void main(String args[]) throws RemoteException
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
     
        ControladorServidorChatImpl objRemoto = new ControladorServidorChatImpl();//se leasigna el puerto de escucha del objeto remoto
        
        try
        {
           UtilidadesRegistroS.arrancarNS(numPuertoRMIRegistry);
           UtilidadesRegistroS.RegistrarObjetoRemoto(objRemoto, direccionIpRMIRegistry, numPuertoRMIRegistry, "ServidorChat");            
           
        } catch (Exception e)
        {
            System.err.println("No fue posible Arrancar el NS o Registrar el objeto remoto" +  e.getMessage());
        }
        
        
    }
}
