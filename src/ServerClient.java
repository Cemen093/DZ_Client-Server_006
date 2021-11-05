import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

class ServerClient extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private InetAddress inetAddress;
    private int countConnections;

    public ServerClient(Socket socket) throws IOException {
        this.socket = socket;
        this.inetAddress = socket.getInetAddress();
        this.countConnections = 1;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void reStart(Socket socket) throws IOException {
        this.socket = socket;
        this.inetAddress = socket.getInetAddress();
        this.countConnections = countConnections + 1;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getCountConnections() {
        return countConnections;
    }

    @Override
    public void run() {
        String line;
        try {
            send("you have been logged in");

            while (true) {
                line = in.readLine();
                System.out.println("received string "+line+" from client "+socket.getInetAddress());
                if(line.equals("stop")) {
                    System.out.println("connection "+socket.getInetAddress()+" was terminated");
                    out.write("stop");
                    out.flush();

                    in.close();
                    out.close();
                    socket.close();
                    break;
                }
                send("you sent me "+line);
                out.flush();
            }

        } catch (IOException e) {
        }
    }
}