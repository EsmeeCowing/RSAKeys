//Esmée Cowing

import java.io.*;
import java.math.BigInteger;
import java.util.BitSet;

//todo: ???read the file into a list/string and then modify it and write it back out
//This is a class because I was originally planning on doing multiple key-cracking strategies and it seemed like a good way to organize it.
public class KeyCracker {
    public static void dixonCrack(String directoryFileName, int maxBitLength) throws IOException {
        String newFileString = "";
        BufferedReader directoryFileReader = new BufferedReader(new FileReader(directoryFileName));
        String currentLine;
        while ((currentLine = directoryFileReader.readLine()) != null) {
            newFileString += currentLine;
            String[] lineArray;
            if ((lineArray = currentLine.split(":")).length == 2) {
                RSAKey currentKey = new RSAKey(lineArray[1]);
                BigInteger n = currentKey.keyArray[1];
                if (n.bitLength() < maxBitLength) {

                    BigInteger p = dixonFactor(n);
                    BigInteger q = n.divide(p);
                    BigInteger fiOfN = n.subtract(p).subtract(q).add(BigInteger.ONE);
                    BigInteger e = currentKey.keyArray[2].modInverse(fiOfN);

                    newFileString += (":"+currentKey.keyArray[0] + ";" + n + ";" + e);
                    System.out.println(currentKey.keyArray[0] + ";" + n + ";" + e + "\n");//todo: remove this print statement
                }
            }
            newFileString += "\n";
        }
        PrintStream out = new PrintStream(directoryFileName);
        out.print(newFileString);
    }

    private static BigInteger dixonFactor(BigInteger n) {
        System.out.println("factoring an n! The n is "+n);
        int numMultRows = 0;

        double factorMax = 30 * Math.pow(Math.E, (0.08 * n.bitLength()));
        BigInteger[] primeFactors = DixonPrimeFinder.getPrimeArray((int) (factorMax));
        System.out.println("factor base size: "+primeFactors[primeFactors.length-1]);

        BigInteger currentR = n.sqrt().add(BigInteger.ONE);
        BigInteger currentN = n;
        DixonRow[] multiplierRows = new DixonRow[primeFactors.length];
        DixonRow currentRow;

        while (true) {
            //generating a parity set based on r
            BitSet paritySet = new BitSet(primeFactors.length); //true means that the power is odd
            BigInteger currentLeftover = (currentR.pow(2)).mod(n);
            if (currentLeftover.equals(BigInteger.ZERO)) {
                throw new IllegalStateException("you've chosen to factor a prime number >:(");
            }
            for (int i = 0; i < primeFactors.length; i++) {
                while ((currentLeftover.mod(primeFactors[i])).equals(BigInteger.ZERO)) {
                    paritySet.flip(i);
                    currentLeftover = currentLeftover.divide(primeFactors[i]);
                }
            }

            if (currentLeftover.equals(BigInteger.ONE)) {
                currentRow = new DixonRow(currentR, n, paritySet);

                //integrating the new row into the dixon algorithm matrix
                boolean isMultiplierRow = false;
                int multiplierIndex = -1;
                int indexOfNextOddParity = -1;
                while ((indexOfNextOddParity = currentRow.currentParitySet.nextSetBit(indexOfNextOddParity + 1)) != -1) {
                    if (multiplierRows[indexOfNextOddParity] != null) {
                        currentRow.multiply(multiplierRows, indexOfNextOddParity);
                    } else if (!isMultiplierRow) {
                        isMultiplierRow = true;
                        multiplierIndex = indexOfNextOddParity;

                        numMultRows++;
                        if (numMultRows%500 ==0) {
                            System.out.println("columns cleared: " + numMultRows + "/" + primeFactors.length);
                        }
                    }
                }
                if (isMultiplierRow) {
                    multiplierRows[multiplierIndex] = currentRow;
                }

                //code that quits the loop if we find the correct factor
                if (currentRow.currentParitySet.nextSetBit(0) == -1) {
                    BigInteger possibleFactor = currentRow.getFOfR(multiplierRows).sqrt().mod(n).add(currentRow.getR(multiplierRows)).gcd(n);
                    if (!possibleFactor.equals(BigInteger.ONE) && !possibleFactor.equals(n)) {
                        System.out.println("factor found!");
                        return possibleFactor;
                    }
                }
            }

            //code that changes r in preparation for the next round
            if (((currentR.pow(2)).mod(n)).compareTo(primeFactors[primeFactors.length - 1].pow(3)) > 0) {
                currentN = currentN.add(n);
                currentR = currentN.sqrt();
            }
            currentR = currentR.add(BigInteger.ONE);
        }
    }
}



