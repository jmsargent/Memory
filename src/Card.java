import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Card extends JButton{
    enum Status {HIDDEN, VISIBLE, MISSING}
    private Status status = Status.MISSING;
    private Icon icon;

    public Card(Icon i){
        setStatus(Status.MISSING);
        icon = i;
        setOpaque(true);
    }

    public Card(Icon i,Status s){
        icon = i;
        setOpaque(true);
        setStatus(s);
    }

    public void setStatus(Status s){
        this.status = s;
        if (s == Status.HIDDEN ) {
            setBackground(Color.BLUE);
            setIcon(null);
        } else if (s == Status.MISSING) {
            setBackground(Color.WHITE);
            setIcon(null);
        } else if (s == Status.VISIBLE) {
            setBackground(Color.green);
            setIcon(icon);
        }
    }
    // Avläsa korts tillstånd
    public Status getStatus(){
        return status;
    }

    public Card copy(){
         Card cardCopy = new Card(icon,getStatus());
         return cardCopy;
    }

    public boolean equalIcon(Card c){
        return(c.icon == icon);
    }
}