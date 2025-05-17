package org.example;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class Main {
    //Lembrar de usar o kct da variavel crua(raw)
    public static Coords co;

    public Main(){
        co = new Coords();
    }

    private static final int PORT = 10001;
    private static final int MSG_BYTES = 20; // size: 16 (header) + 4 + 4? adjust as needed

    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            while (true) {
                var socket = server.accept();
                System.out.println("Client connected: " + socket.getRemoteSocketAddress());
                handleClient(socket.getInputStream());
            }
        }
    }

    private static void handleClient(InputStream in) throws Exception {
        byte[] buf = new byte[MSG_BYTES];
        while (in.read(buf) == MSG_BYTES) {
            // wrap in little endian
            ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
            int size = bb.getShort() & 0xFFFF;
            int type = bb.getShort() & 0xFFFF;
            long timestamp = bb.getLong();
            long raRaw = bb.getInt() & 0xFFFFFFFFL;
            int decRaw = bb.getInt();

            System.out.println(co.J2000(raRaw,decRaw,timestamp));

            /*System.out.printf(
                    "Received: size=%d, type=%d, time=%.3f, RA=%.6f rad, DEC=%.6f rad%n",
                    size, type, timestamp / 1000.0, ra, dec
            );*/
        }
    }
}
