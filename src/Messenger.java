//Esmée Cowing

import java.io.*;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Hashtable;

public class Messenger {
    private RSAKeyPair messengerPair;
    private Hashtable<String, RSAKeyPair> directoryTable;

    public Messenger(String keyFileName, String directoryFileName, int keyBitLength) throws IOException {
        BufferedReader keyFileReader = new BufferedReader(new FileReader(keyFileName));
        if (new File(keyFileName).length() != 0) {
            RSAKey publicKey = new RSAKey(keyFileReader.readLine());
            RSAKey privateKey = new RSAKey(keyFileReader.readLine());
            this.messengerPair = new RSAKeyPair(publicKey, privateKey);
        }
        else{
            this.messengerPair = new RSAKeyPair(keyBitLength);
            PrintStream out = new PrintStream(keyFileName);
            out.println(messengerPair.toString());
        }

        KeyCracker.dixonCrack(directoryFileName, 10000); //I don't really want to have a limit on bitLength right now (but I might if I come back to this project later) so im temporarily making it a very big number.

        BufferedReader directoryFileReader = new BufferedReader(new FileReader(directoryFileName));
        directoryTable = new Hashtable<>(34);
        directoryTable.put("me", messengerPair);

        String currentLine;
        while ((currentLine = directoryFileReader.readLine()) != null) {
            String[] keyAndNameStrings = currentLine.split(":");
            directoryTable.put(keyAndNameStrings[0], new RSAKeyPair(new RSAKey(keyAndNameStrings[1]), new RSAKey(keyAndNameStrings[2])));
        }
    }

    public String getEncryptedMessage(String message, String sender, String recipient){
        long[] messageLongs = BitSet.valueOf(message.getBytes()).toLongArray();
        String returnString = "";
        for (Long l : messageLongs){
            BigInteger addition = BigInteger.valueOf(l);
            if (recipient != null) {
                addition = directoryTable.get(recipient).publicKey.useKey(addition);
            }
            if (sender != null) {
                addition = directoryTable.get(sender).privateKey.useKey(addition);
            }
            returnString += ","+addition;
        }
        returnString = returnString.substring(1);
        return returnString;
    }



    public String getDecryptedMessage(String message, String sender, String recipient){
        String[] messageStrings = message.split(",");
        BigInteger[] messageBigIntegers  = new BigInteger[messageStrings.length];
        for (int i = 0; i < messageStrings.length; i++){
            messageBigIntegers[i] = new BigInteger(messageStrings[i]);
            if (sender != null){
                messageBigIntegers[i] = directoryTable.get(sender).publicKey.useKey(messageBigIntegers[i]);
            }
            if (recipient != null) {
                messageBigIntegers[i] = directoryTable.get(recipient).privateKey.useKey(messageBigIntegers[i]);
            }
        }
        long[] messageLongs = new long[messageStrings.length];
        for (int i = 0; i < messageStrings.length; i++){
            messageLongs[i] = messageBigIntegers[i].longValueExact();
        }
        return new String(BitSet.valueOf(messageLongs).toByteArray());
    }

    @Override
    public String toString() {
        return "messenger key pair: "+"\n"
                +messengerPair.toString()+"\n"
                +"messenger directory"+"\n"
                + directoryTable.toString();
    }
}
