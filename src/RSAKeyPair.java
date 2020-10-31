//cracking RSA note: use the random number generator everyone's using/ appending/ prinstream makes a new file that overwrites the file with that name so be careful of making multiple messengers
import java.math.BigInteger;
import java.util.Random;

public class RSAKeyPair {
    public RSAKey publicKey;
    public RSAKey privateKey;

    public RSAKeyPair(RSAKey publicKey, RSAKey privateKey){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public RSAKeyPair(int desiredBitLength){
            int numNBits = desiredBitLength;
            if (desiredBitLength%2 != 0){
                //throw new IllegalArgumentException("you can only ask for keys with even bit lengths (sorry!)");
            }

            BigInteger p = BigInteger.probablePrime(numNBits / 2, new Random());
            while (p.bitLength() != (desiredBitLength / 2)) {
                p = BigInteger.probablePrime(numNBits / 2, new Random());
            }
            BigInteger q = BigInteger.probablePrime(numNBits / 2, new Random());
            while (q.bitLength() != (desiredBitLength / 2)) {
                q = BigInteger.probablePrime(numNBits / 2, new Random());
            }
            System.out.println(desiredBitLength+" "+(p.multiply(q)).bitLength());

            BigInteger n = p.multiply(q);
            BigInteger fiOfN = n.subtract(p).subtract(q).add(BigInteger.ONE);

            BigInteger publicKeyInt = new BigInteger(new Random().nextInt(fiOfN.bitLength() + 1), 5, new Random());
            while (!(publicKeyInt.compareTo(BigInteger.ONE) == 1
                    && publicKeyInt.compareTo(fiOfN) == -1
                    && publicKeyInt.gcd(n).compareTo(BigInteger.ONE) == 0
                    && publicKeyInt.modInverse(fiOfN).compareTo(publicKeyInt) != 0)) {
                publicKeyInt = new BigInteger(new Random().nextInt(fiOfN.bitLength() + 1), 5, new Random());

            }

            BigInteger privateKeyInt = publicKeyInt.modInverse(fiOfN);


            publicKey = new RSAKey(BigInteger.valueOf((numNBits))+";"+ n+";"+ publicKeyInt);
            privateKey = new RSAKey(BigInteger.valueOf((numNBits))+";"+ n+";"+ privateKeyInt);
    }

    //prints private key, then public key
    public String toString() {
        return privateKey.toString()+"\n"+publicKey.toString();
    }

}
