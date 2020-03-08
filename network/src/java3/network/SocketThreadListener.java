package java3.network;

import java.net.Socket;

public interface SocketThreadListener {
    void onSocketThreadStart(SocketThread thread, Socket socket);
    void onSocketThreadStop(SocketThread thread);

    void onSocketThreadReady(SocketThread thread, Socket socket);
    void onReceivingString(SocketThread thread, Socket socket, String msg);

    void onSocketThreadException(SocketThread thread, Exception exception);


}
