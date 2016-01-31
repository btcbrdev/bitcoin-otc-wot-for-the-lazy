package info.walltime.bitcoinwot;

import org.bitcoinj.core.ECKey;

import java.awt.*;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class BitcoinWot {

    public static boolean DEBUG = false;

    public static final IrcBot BOT = new IrcBot();
    public static final AtomicBoolean REGISTERING = new AtomicBoolean();
    public static final AtomicBoolean VERIFYING_EMAIL = new AtomicBoolean();

    public static Login LOGIN;
    public static Password PASSWORD;
    public static ECKey KEY;
    public static String PASSWORD_STRING;
    public static RegisteringWot REGISTERING_WOT;
    public static VerifyEmail VERIFY_EMAIL;

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[1].equals("-debug")) {
            BitcoinWot.DEBUG = true;
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });

        BOT.setAutoNickChange(true);
        BOT.setVerbose(BitcoinWot.DEBUG);
        BOT.connect("chat.freenode.net");
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
