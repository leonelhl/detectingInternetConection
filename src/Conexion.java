
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LEOX
 */
public class Conexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        /* El problema de detectar cuando tenemos internet aqui
         es que si haces un metodo tradicional de ping o socket para establecer la conexion con el server
         te va a dar que si, puesto que la conexion con nauta primero establece la conexion con el sitio y despues
         te redirecciona a nauta por eso siempre da que si hay conexion, a pesar de no tener internet.
         Por tanto el sentido es tomar esa conexion y leer o parsear esa pagina que nos esta dando, en caso de contener
         algo como login.nauta.cu sabemos que estamos en presencia del portal
         de lo contrario si tenemos internet. Enjoy!! */
        boolean open = false;

        while (true) {
            // Esperando 15 segundos para comprobar si hay conexion
            System.out.print("Verificando conexión");
            CuentaAtras(0, 0, 10);
            try {
                URL my_URL = new URL("http://google.com");
                URLConnection uc = my_URL.openConnection();
                boolean hayconexion = true;
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                /*String inputLine = in.readLine();
                if (inputLine.contains("nauta")) {
                    //System.out.println("el portal de nauta");
                    hayconexion = false;
                }*/
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("nauta")||inputLine.contains("secure.etecsa.net")||inputLine.contains("portalurl")) {
                        hayconexion = false;
                        break;
                    }
                }
                
                in.close();

                if (hayconexion) {
                    System.out.println("Está Conectado. Enjoy!");
                    if (!open) {
                        TocarCancion("1.wav");
                    }
                    open = true;
                } else {
                    System.out.println("NO tiene conexión a Internet.");
                    if (open) {
                        TocarCancion("2.wav");
                    }
                    open = false;
                }
            } catch (Exception e) {
                System.out.println("Problema con la conexion de Red.");
                if (open) {
                    TocarCancion("2.wav");
                }
                open = false;
            }
        }
    }

    public static void TocarCancion(String tema) {
        try {
            Clip sonido = AudioSystem.getClip();
            File musica = new File(tema);
            sonido.open(AudioSystem.getAudioInputStream(musica));
            sonido.start();
            Thread.sleep(15000);
            sonido.close();
        } catch (Exception ex) {
        }
    }

    public static void CuentaAtras(int nuHora, int nuMin, int nuSeg) {
        while (nuSeg != 0 || nuMin != 0 || nuHora != 0) {
            if (nuSeg != 0) {//si no es el ultimo segundo
                nuSeg--;  //decremento el numero de segundos                                  
            } else if (nuMin != 0) {//si no es el ultimo minuto
                nuSeg = 59;//segundos comienzan en 59
                nuMin--;//decremento el numero de minutos
            } else if (nuHora != 0) {
                nuHora--;//decremento el numero de horas
                nuMin = 59;//minutos comienzan en 59
                nuSeg = 59;//segundos comienzan en 59
            } else {
                break;//se acabo el tiempo fin hilo  
            }
            System.out.print(".");//Muestro en pantalla el temporizador
            try {
                Thread.sleep(1000);//Duermo el hilo durante 999 milisegundos(casi un segundo, quintandole el tiempo de proceso)
            } catch (InterruptedException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("");//Cuando termine un salto de linea
    }
}
