//Esmée Cowing


//Notes on my code: in general, the public key is always written first


//INSTRICTIONS: If my program works as intended, the only class you'll need to use is the messenger class.

//Initializing:
// It takes a filename string that's the name of the file where you want your key to be stored, a file with a directory of everyone you want to send messages to and their public keys, and the bit length you want your key to have
//the way I've written it, initializing might take a while if there are lots of unckracked keys in your directory (sorry!).

//Functions:
//getEncryptedMessage - takes the name of the sender (null if the message is unsigned, and "me" if your are sending the message), and the recipient (null if the message is public), and returns the appropriate encrypted string
//getDecryptedMessages -  takes the name of the sender (null if the message is unsigned, and "me" if your are meant to receive the message), and the recipient (null if the message is public), and returns the decrypted string

//below is some sample code that demos my program

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Messenger myMessenger = new Messenger("currentKey", "classDirectory",78);

        String[] challengeMessages = new String[]{"68072326287437", "673722651521319", "1868050752244696", "24501839097582543", "169738516534137945", "514099795387561339",
                "3819292808648910874", "637316441938040871", "27277884067527749349", "133549888690570227842", "337018872073950976903", "1853641490885369842560",
                "28259397857472103332832", "36099566173017553600636", "4414557255034612004857975", "195497407108060906273650043", "2068479455631652547859125980",
                "37188954636954850477223836029", "312294162531171483257002689281", "9183565372936626967455853949491", "139219999583992666693241290384342",
                "290753235573910225527996340661076", "11066684427109450402103112167963269", "640209138478402009085603777740755517"};

        for (int i = 0; i< challengeMessages.length; i++){
            System.out.println(i+1+") "+myMessenger.getDecryptedMessage(challengeMessages[i],null, ("challenge"+(i+1))));
        }

        System.out.println(myMessenger.getDecryptedMessage(myMessenger.getEncryptedMessage("Hello!", null, "me"), null, "me"));

    }
}

//If I ever come back to this project:
//todo: take your own data for the best base?
//todo:make more efficient?
//todo: write another algorithm?
//todo: make it ok for even bit lengths
