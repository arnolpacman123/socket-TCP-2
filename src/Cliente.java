import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private InetAddress ip;
    private DataInputStream datoEntrada;
    private DataOutputStream datoSalida;
    private Scanner lector;

    public Cliente() {
        try {
            this.ip = InetAddress.getByName("localhost");
            this.socket = new Socket(this.ip, 6666);
            this.datoEntrada = new DataInputStream(this.socket.getInputStream());
            this.datoSalida = new DataOutputStream(this.socket.getOutputStream());
            this.lector = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarCliente()  {
        try {
            while (true) {
                System.out.println(this.datoEntrada.readUTF());
                String enviar = lector.nextLine();
                datoSalida.writeUTF(enviar);
                if (enviar.equals("Salir")) {
                    cerrarConexion();
                    break;
                }
                String recibido = datoEntrada.readUTF();
                System.out.println(recibido);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarConexion() {
        System.out.println("Cerrando esta conexión: " + socket);
        cerrarSocket();
        System.out.println("¡Conexión cerrada!");
    }

    private void cerrarSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}