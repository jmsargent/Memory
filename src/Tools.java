import java.util.Random;

public class Tools {
    public static void randomOrder(Object[] cardArray){
        // deklarerar variabler
        Object card;
        int index;
        Random rand = new Random();

        for (int i = cardArray.length - 1;  i>0 ; i-- ){
            index = rand.nextInt(i+1);
            card = cardArray[index];
            cardArray[index] = cardArray[i];
            cardArray[i] = card;
        }
    }
}