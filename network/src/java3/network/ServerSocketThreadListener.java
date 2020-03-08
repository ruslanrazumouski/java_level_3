package java3.network;

import java.net.ServerSocket;
import java.net.Socket;

public interface ServerSocketThreadListener {
    void onServerStart(ServerSocketThread thread);
    void onServerStop(ServerSocketThread thread);
    void onServerSocketCreated(ServerSocketThread thread, ServerSocket server);
    void onServerTimeOut(ServerSocketThread thread, ServerSocket server);
    void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket);
    void onServerException(ServerSocketThread thread, Throwable exception);
}
