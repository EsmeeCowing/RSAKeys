//Esmée Cowing

import java.math.BigInteger;
import java.util.BitSet;

public class DixonRow {
    public BigInteger n;
    public BigInteger originalR;
    public BigInteger originalFOfR;
    public BitSet currentParitySet;
    public BitSet currentRowMultParitySet;

    public DixonRow(BigInteger r, BigInteger n, BitSet currentParitySet){
        this.n = n;
        this.originalR = r;
        this.originalFOfR = r.pow(2).mod(n);

        //false means an even parity
        this.currentParitySet = currentParitySet;
        this.currentRowMultParitySet = new BitSet(currentParitySet.length());


    }

    //multiplies this row*other row, making this row a multiplied version and leaving other row as it was
    public void multiply(DixonRow[] rowAray, int multiplierIndex){
        for (int indexOfNextOddParity = rowAray[multiplierIndex].currentRowMultParitySet.nextSetBit(0);
             indexOfNextOddParity != -1;
             indexOfNextOddParity = rowAray[multiplierIndex].currentRowMultParitySet.nextSetBit(indexOfNextOddParity+1)) {
            this.currentRowMultParitySet.flip(indexOfNextOddParity);
        }
        this.currentRowMultParitySet.flip(multiplierIndex);


        for (int indexOfNextOddParity = rowAray[multiplierIndex].currentParitySet.nextSetBit(0);
             indexOfNextOddParity != -1;
             indexOfNextOddParity = rowAray[multiplierIndex].currentParitySet.nextSetBit(indexOfNextOddParity+1)) {
            this.currentParitySet.flip(indexOfNextOddParity);
        }
    }

    //returns the final augmented fOfR
    public BigInteger getFOfR(DixonRow[] rowArray){
        BigInteger fOfR = originalFOfR;
        for (int indexOfNextOddParity = currentRowMultParitySet.nextSetBit(0);
             indexOfNextOddParity != -1;
             indexOfNextOddParity = currentRowMultParitySet.nextSetBit(indexOfNextOddParity+1)){
            fOfR = fOfR.multiply(rowArray[indexOfNextOddParity].originalFOfR);
        }
        return fOfR;
    }

    //returns the final augmented r
    public BigInteger getR(DixonRow[] rowArray){
        BigInteger R = originalR;
        for (int indexOfNextOddParity = currentRowMultParitySet.nextSetBit(0);
             indexOfNextOddParity != -1;
             indexOfNextOddParity = currentRowMultParitySet.nextSetBit(indexOfNextOddParity+1)){
            R = R.multiply(rowArray[indexOfNextOddParity].originalR);
        }
        return R;
    }


    @Override
    public String toString() {
        return " parity set: "+this.currentParitySet.toString();
    }
}
