//Esmée Cowing

import java.math.BigInteger;

public class DixonPrimeFinder {

    //THIS FUNCTION DOES NOT INCLUDE ONE WHEN IT GENERATES A LIST OF PRIMES AND IT RETURNS ALL PRIMES LESS THAN OF EQUAL TO MAX
    public static BigInteger[] getPrimeArray(int max){
        BigInteger[] primesUnderMax;

        // indexes are the primes two above them
        //false means the number is a prime true means it is not
        boolean[] numbers = new boolean[max-1];
        for (int i = 2; i <Math.sqrt(max); i ++) {
            if (numbers[i-2] == false) {
                for (int i2 = i*2; i2 <=max; i2+=i){
                    numbers[i2-2] = true;
                }
            }
        }

        int currentPrimesUnderMaxLength = 0;
        for (boolean num : numbers){
            if (num == false){
                currentPrimesUnderMaxLength++;
            }
        }

        primesUnderMax = new BigInteger[currentPrimesUnderMaxLength];
        int index = 0;
        for (int i = 2; i<= max; i++){
            if (numbers[i-2] == false) {
                primesUnderMax[index] = BigInteger.valueOf(i);
                index++;
            }
        }

        return primesUnderMax;

    }

}