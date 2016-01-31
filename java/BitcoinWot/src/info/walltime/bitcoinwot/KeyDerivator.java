package info.walltime.bitcoinwot;

import com.lambdaworks.crypto.SCrypt;
import org.bitcoinj.core.ECKey;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class KeyDerivator {

    private final String username;
    private final String password;
    
    public KeyDerivator(String username, String password) {
        this.username = username;
        this.password = password;    
    }
    
    public ECKey generateKey() throws GeneralSecurityException, UnsupportedEncodingException {
        final String composedKey = username + ":" + password + ":tatualado";
        byte[] hash = SCrypt.scrypt(composedKey.getBytes("UTF-8"),
        new String("Uzy7pjlwDkwIWuq").getBytes("UTF-8"),
                32768, 4, 1, 32);

        ECKey generatedKey = ECKey.fromPrivate(hash, false);
        return generatedKey;
    }
}
