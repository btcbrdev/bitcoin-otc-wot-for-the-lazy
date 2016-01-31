package info.walltime.bitcoinwot;

import org.jibble.pircbot.PircBot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class IrcBot extends PircBot {
    @Override
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
        super.onMessage(channel, sender, login, hostname, message);

        if (BitcoinWot.DEBUG) {
            System.out.println(sender + ": " + message);
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        super.onPrivateMessage(sender, login, hostname, message);

        if (BitcoinWot.DEBUG) {
            System.out.println("Private message: " + sender + ": " + message);
        }

        if (sender.equals("gribble")) {
            if (message.contains("Your challenge string")) {
                String[] challengeArray = message.split(" ");
                String challenge = challengeArray[challengeArray.length - 1];
                String proof = BitcoinWot.KEY.signMessage(challenge);

                BitcoinWot.BOT.sendMessage("gribble", ";;bcverify " + proof);
            } else if (message.contains("You are now authenticated")) {
                if (BitcoinWot.REGISTERING_WOT != null) {
                    BitcoinWot.REGISTERING_WOT.dispatchEvent(new WindowEvent(BitcoinWot.REGISTERING_WOT, 
                            WindowEvent.WINDOW_CLOSING));
                }

                if (BitcoinWot.PASSWORD != null) {
                    BitcoinWot.PASSWORD.dispatchEvent(new WindowEvent(BitcoinWot.PASSWORD, 
                            WindowEvent.WINDOW_CLOSING));
                }

                java.awt.EventQueue.invokeLater(() -> {
                    new AuthenticatedUser().setVisible(true);
                });
            } else if (message.contains("Signature verification failed")) {
                JOptionPane.showMessageDialog(null, 
                            "Erro na autenticação.\n\nProvavelmente esse usuário não foi criado usando "
                                    + "Bitcoin OTC WoT para preguiçosos.\n\nParabéns, você não é um preguiçoso! :)");

                if (BitcoinWot.PASSWORD != null) {
                    BitcoinWot.PASSWORD.dispatchEvent(new WindowEvent(BitcoinWot.PASSWORD, 
                            WindowEvent.WINDOW_CLOSING));
                }
            } else if (message.contains("You have to have received some ratings in order to rate other users")
                     || message.contains("Your nick does not exist in the Rating database")) {
                JOptionPane.showMessageDialog(null, 
                        "Hmmmmmm... Parece que você é novo por aqui, não?"
                        + "\n\nInfelizmente não é possível qualificar ninguém se você não tem reputação positiva."
                        + "\n\nMas não desista. Você só precisa convencer alguém que já tenha reputação a te dar um voto de confiança."
                        + "\n\nEntre na comunidade Bitcoin OTC WoT Brasil no Facebook e peça a alguém que você conheça.");
            } else if (message.contains("You have not given this nick a rating previously")
                        || message.contains("Rating entry successful")
                        || message.contains("Successfully removed your rating")) {

                JOptionPane.showMessageDialog(null, 
                        "Legal! Você qualificou um usuário com sucesso! :)");
            }
        }
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        super.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);

        if (BitcoinWot.LOGIN != null) {
            if (BitcoinWot.DEBUG) {
                System.out.println("Notice: " + sourceNick + ": " + notice);
            }

            if (notice.contains("is not registered")) {
                
                if (!BitcoinWot.REGISTERING.get()) {
                    JOptionPane.showMessageDialog(null, 
                            "Esse nick não está registrado, por favor registre antes.");

                    BitcoinWot.LOGIN.getjButton1().setEnabled(true);
                    BitcoinWot.LOGIN.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } else {

                    if (BitcoinWot.VERIFYING_EMAIL.get()) {
                        if (BitcoinWot.REGISTERING.get()) {
                            JOptionPane.showMessageDialog(null, 
                                    "Ocorreu um erro na verificação do email.");
                        }   
                    } else {
                        BitcoinWot.LOGIN.getjButton2().setEnabled(true);
                        BitcoinWot.LOGIN.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
                        BitcoinWot.LOGIN.setState(Frame.ICONIFIED);

                        java.awt.EventQueue.invokeLater(() -> {
                            new NewPassword().setVisible(true);
                        });
                    }
                }
            } else if (notice.contains("Registered")) {
                if (BitcoinWot.REGISTERING.get()) {
                    if (BitcoinWot.VERIFYING_EMAIL.get()) {
                         BitcoinWot.VERIFY_EMAIL.dispatchEvent(
                                 new WindowEvent(BitcoinWot.VERIFY_EMAIL, 
                            WindowEvent.WINDOW_CLOSING));

                        java.awt.EventQueue.invokeLater(new Runnable() {
                          public void run() {             
                              new RegisteringWot().setVisible(true);
                          }
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, 
                                "Esse nick já existe, por favor escolha outro.");

                        BitcoinWot.LOGIN.getjButton2().setEnabled(true);
                        BitcoinWot.LOGIN.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
                    }
                } else {
                    BitcoinWot.LOGIN.setState(Frame.ICONIFIED);

                    BitcoinWot.LOGIN.getjButton1().setEnabled(true);
                    BitcoinWot.LOGIN.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    java.awt.EventQueue.invokeLater(() -> {
                        new Password().setVisible(true);
                    });
                }
            } if (notice.contains("Invalid password")) {
                    JOptionPane.showMessageDialog(null, "Senha incorreta!");

                if (BitcoinWot.PASSWORD != null) {
                    BitcoinWot.PASSWORD.dispatchEvent(new WindowEvent(BitcoinWot.PASSWORD, 
                            WindowEvent.WINDOW_CLOSING));
                }
            }
        }
    }

    @Override
    protected void onChannelInfo(String channel, int userCount, String topic) {
        super.onChannelInfo(channel, userCount, topic);

        if (BitcoinWot.DEBUG) {
            System.out.println("Channel " + channel);
        }

    }

    @Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);

        if (BitcoinWot.LOGIN != null) {
            BitcoinWot.LOGIN.getjButton1().setEnabled(true);
            BitcoinWot.LOGIN.getjButton2().setEnabled(true);
            BitcoinWot.LOGIN.setCursor(Cursor.getDefaultCursor());
            BitcoinWot.LOGIN.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    
}
