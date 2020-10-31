//Esmée Cowing

import java.math.BigInteger;

public class RSAKey {
    String keyString;
    public BigInteger[] keyArray;

    public RSAKey(String key) {
        this.keyString = key;
        String[] keyStringArray = keyString.split(";");
        this.keyArray = new BigInteger[3];

        for (int i = 0; i < keyStringArray.length; i++) {
            this.keyArray[i] = new BigInteger(keyStringArray[i]);

        }
    }

    public BigInteger useKey(BigInteger secret){
        return secret.modPow(keyArray[2], keyArray[1]);
    }

    public String toString() {
        return this.keyString;
    }
}
