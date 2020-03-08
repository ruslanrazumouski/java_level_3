package java3.chat.client;

import java3.chat.common.Library;
import java3.network.SocketThread;
import java3.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static javax.swing.JOptionPane.showInputDialog;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("admin");
    private final JPasswordField tfPassword = new JPasswordField("11111");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private final JList<String> userList = new JList<>();

    JMenuBar menuBar = new JMenuBar();
    JMenu userMenu = new JMenu("User");
    JMenuItem menuItemChangeNickname = new JMenuItem("Change nickname");

    private boolean shownIoErrors = false;

    private SocketThread socketThread;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final String WINDOW_TITLE = "Chat";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(WINDOW_TITLE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUser = new JScrollPane(userList);
        scrollUser.setPreferredSize(new Dimension(100,0));

        log.setEditable(false);
        log.setLineWrap(true);
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect,BorderLayout.WEST);
        panelBottom.add(tfMessage,BorderLayout.CENTER);
        panelBottom.add(btnSend,BorderLayout.EAST);

        add(scrollLog, BorderLayout.CENTER);
        add(scrollUser, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        userMenu.add(menuItemChangeNickname);
        menuItemChangeNickname.setEnabled(false);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        cbAlwaysOnTop.addActionListener(this);
        tfMessage.addActionListener(this);
        btnSend.addActionListener(this);
        btnLogin.addActionListener(this);
        btnDisconnect.addActionListener(this);
        menuItemChangeNickname.addActionListener(this);

        panelBottom.setVisible(false);
        setVisible(true);
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
        //String userName = tfLogin.getText();
        if ( "".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.requestFocusInWindow();
        socketThread.sendMessage(Library.getTypeBcastClient(msg));
        //wrtMsgToLogFile(msg, userName);
    }

    private void connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()) );
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException e) {
            showException(Thread.currentThread(), e);
        }
        menuItemChangeNickname.setEnabled(true);
    }

    private void changeNickname() {
        String nickname = showInputDialog("Please input new nickname");
        if (nickname == null || nickname.equals("")) return;
        String userName = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        socketThread.sendMessage(Library.getAuthRename(nickname, userName, password));
    }

    private void wrtMsgToLogFile(String msg, String userName) {
        try ( FileWriter out = new FileWriter("log.txt", true) ) {
            out.write(userName + ": " + msg + "\n");
            out.flush();
        }
        catch (IOException e) {
            if(!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    private void putLog(String msg) {
        if("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if ( src == btnSend || src == tfMessage ) {
            sendMessage();
        } else if ( src == cbAlwaysOnTop ) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if ( src == btnLogin ) {
            connect();
        } else if ( src == btnDisconnect) {
            menuItemChangeNickname.setEnabled(false);
            socketThread.close();
        } else if ( src == menuItemChangeNickname) {
            changeNickname();
        } else {
            throw new RuntimeException("unknown source " + src);
        }
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0) {
            msg = "Empty stacktrace";
        } else {
            msg = "Exception in " + t.getName() + " " +
                    e.getClass().getCanonicalName() + ": " +
                    e.getMessage() + "\n\t at " + ste[0];
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);
    }

    /**
     * Socket thread listener methods
    **/

    @Override
    public void onSocketThreadStart(SocketThread thread, Socket socket) {
        putLog("Start");
    }

    @Override
    public void onSocketThreadStop(SocketThread thread) {
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
        setTitle(WINDOW_TITLE);
        userList.setListData(new String[0]);
    }

    @Override
    public void onSocketThreadReady(SocketThread thread, Socket socket) {
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(Library.getAuthRequest(login, password));
    }

    @Override
    public void onReceivingString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    private void handleMessage(String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.AUTH_ACCEPT:
                setTitle(WINDOW_TITLE + " entered with nickname: " + arr[1]);
                break;
            case Library.AUTH_RENAME:
                //setTitle(String.format("%s user with nickname:%s change nickname on: %s", WINDOW_TITLE, arr[1], arr[2]));
                setTitle(String.format("%s user with nickname: change nickname on: %s", WINDOW_TITLE, arr[1]));
                break;
            case Library.AUTH_DENIED:
                putLog(msg);
                break;
            case Library.MSG_FORMAT_ERROR:
                putLog(msg);
                socketThread.close();
                break;
            case Library.TYPE_BROADCAST:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) +
                arr[2] + ": " + arr[3]);
                break;
            case Library.USER_LIST:
                String users = msg.substring(Library.USER_LIST.length() +
                        Library.DELIMITER.length());
                String[] usersArr = users.split(Library.DELIMITER);
                Arrays.sort(usersArr);
                userList.setListData(usersArr);
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
        }
    }

    @Override
    public void onSocketThreadException(SocketThread thread, Exception exception) {
        //showException(thread, exception);
    }
}
