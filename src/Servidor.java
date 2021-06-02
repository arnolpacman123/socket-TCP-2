import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Servidor {
    private ServerSocket servidorSocket;
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    public Servidor() {
        try {
            this.servidorSocket = new ServerSocket(6666);
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarServidor() {
        while (true) {
            establecerConexion();
            asignarHilo();
        }
    }

    private void establecerConexion() {
        try {
            socket = this.servidorSocket.accept();
            System.out.println("Un nuevo cliente está conectado: " + socket);
            this.entrada = new DataInputStream(this.socket.getInputStream());
            this.salida = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            cerrarSocket();
            e.printStackTrace();
        }
    }

    private void asignarHilo() {
        System.out.println("Asignando un nuevo hilo para este cliente");
        Thread nuevoHilo = new ControladorCliente(this.socket, this.entrada, this.salida);
        nuevoHilo.start();
    }

    private void cerrarSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ControladorCliente extends Thread {

    DateFormat fecha = new SimpleDateFormat("yyy/MM/dd");
    DateFormat hora = new SimpleDateFormat("hh:mm:ss");
    DataInputStream datoEntrada;
    DataOutputStream datoSalida;
    Socket socket;

    public ControladorCliente(Socket socket, DataInputStream datoEntrada, DataOutputStream datoSalida) {
        this.socket = socket;
        this.datoEntrada = datoEntrada;
        this.datoSalida = datoSalida;
    }

    @Override
    public void run() {
        String recibido, regresar;
        while (true) {
            try {
                this.datoSalida.writeUTF("¿Qué deseas? [Fecha u Hora]\n" +
                        "Escriba Salir para finalizar la conexión.");
                recibido = this.datoEntrada.readUTF();
                if (recibido.equals("Salir")) {
                    cerrarConexion();
                    break;
                }
                regresar = mensajeARegresar(recibido);
                datoSalida.writeUTF(regresar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cerrarDatosStream();
    }

    private void cerrarConexion() {
        System.out.println("¡Cliente " + this.socket + " saliendo!");
        System.out.println("Cerrando conexión");
        cerrarSocket();
        System.out.println("Conexión cerrada");
    }

    private void cerrarSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarDatosStream() {
        try {
            this.datoEntrada.close();
            this.datoSalida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String mensajeARegresar(String recibido) {
        String regresar;
        Date fechaF = new Date();
        regresar = switch (recibido) {
            case "Fecha" -> this.fecha.format(fechaF);
            case "Hora" -> this.hora.format(fechaF);
            default -> "Dato de entrada no válido";
        };
        return regresar;
    }

}