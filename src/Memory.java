import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Memory extends JFrame implements ActionListener {
    //klassvariabler
    //private int turnKeeper;
    //private int indexOfLastVisible = 100;
    private boolean unFreeze = true;
    private int nPlayers, nCardsVisible, turnTracker;
    private Card[] allCards = new Card[18];
    private Card[] cardsInUse;
    // private int[] scoreKeeper;
    private int rows = -1, colonns = -1;
    private int indexOfLastVisible;

    // deklarerar grafikobjekt
    private JMenuBar bar = new JMenuBar();
    private JMenu spel = new JMenu("Spel");
    private JMenu inställningar = new JMenu("Inställningar");
    private JFrame frame = new JFrame();
    private JPanel cardPanel = new JPanel();
    private JPanel sq = new JPanel();
    private JButton nytt = new JButton("Nytt Spel");
    private JButton exitButton = new JButton("Avsluta");
    private JLabel[] playerLabel;
    private JLabel[] playerPoints;
    private JPanel[] individualPlayerPanel;
    private JMenuItem nytt1 = new JMenuItem("Nytt");
    private JMenuItem avsluta1 = new JMenuItem("Avsluta");
    private JMenuItem antalspelare = new JMenuItem("Antal spelare");
    private JPanel playerPanel = new JPanel();

    private Memory() {
        nCardsVisible = 0;
        turnTracker = 0;
        indexOfLastVisible = -1;
        newIcon();
        setPlayerAmount();

        this.playerLabel = new JLabel[nPlayers];
        this.playerPoints = new JLabel[nPlayers];
        this.individualPlayerPanel = new JPanel[nPlayers];

        for (int i = 0; i < nPlayers; i++) {
            individualPlayerPanel[i] = new JPanel();
            playerLabel[i] = new JLabel("player" + i);
            playerPoints[i] = new JLabel("0");
        }

        nrcards();
        newGame();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        nytt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
    }

    public static void main(String[] arg) {
        Memory memory = new Memory();
    }

    public void newGame() {
        Tools.randomOrder(allCards);

        int cardAmount = rows * colonns;
        this.cardsInUse = new Card[cardAmount];

        for (int i = 0; i < cardAmount / 2; i++) {
            cardsInUse[i] = allCards[i];
        }

        int temp = cardAmount / 2;
        for (int i = 0; i < cardAmount / 2; i++) {
            cardsInUse[temp + i] = cardsInUse[i].copy();//fel
        }
        Tools.randomOrder(cardsInUse);
        //menu
        frame.setJMenuBar(bar);
        bar.add(spel);
        bar.add(inställningar);
        spel.add(avsluta1);
        spel.add(nytt1);
        inställningar.add(antalspelare);

        // MENU ACTIONLISTENERS

        antalspelare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPlayerAmount();
                frame.dispose();
                newGame();
                System.out.println("kaka");
            }
        });
        avsluta1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        nytt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Memory memory = new Memory();
            }
        });


        //huvudsettings
        frame.setLayout(new BorderLayout());
        frame.setSize(900, 900);
        // kort
        frame.add(cardPanel, BorderLayout.CENTER);
        cardPanel.setBackground(Color.white);
        cardPanel.setPreferredSize(new Dimension(128, 128));
        cardPanel.setLayout(new GridLayout(rows, colonns));
        placerarKort(cardsInUse); //placerar ut kort
        addlyssnare(cardsInUse); // ger lyssnare

        // poäng o spelare
        playerPanel.setBackground(new Color(255, 0, 205));
        playerPanel.setPreferredSize(new Dimension(150, 500));
        playerPanel.setLayout(new GridLayout(nPlayers, 1, 5, 5));
        addPlayerFrames();
        frame.add(playerPanel, BorderLayout.WEST);

        // nytt spel, avsluta
        frame.add(sq, BorderLayout.SOUTH);
        sq.add(nytt);
        sq.add(exitButton);
        // packa, gör synligt
        frame.setVisible(true);
        pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void addPlayerFrames() {
        for (int i = 0; i < nPlayers; i++) {
            playerPanel.add(individualPlayerPanel[i]);
            individualPlayerPanel[i].add(playerLabel[i]);
            individualPlayerPanel[i].add(playerPoints[i]);
            individualPlayerPanel[i].setBackground(new Color(255, 0, 205));
        }
        individualPlayerPanel[0].setBackground(Color.green);
    }

    public void setPlayerAmount() {
        nPlayers = parseI(JOptionPane.showInputDialog("skriv in ett antal spelare(mellan 2-6)"));
    }

    public void addlyssnare(Card[] i) {
        for (int c = 0; c < i.length; c++) {
            i[c].addActionListener(this);
        }
    }

    private void nrcards() {
        //terminera program när man klickar kryss

        while (true) {
            if (rows < 1 || colonns < 1) {

                rows = parseI(JOptionPane.showInputDialog("Ange antal rader mellan 1-6:"));
                colonns = parseI(JOptionPane.showInputDialog("Ange antal kolonner mellan 1-6:"));

            }
            if (((rows * colonns) % 2 == 0) && rows < 7 && colonns < 7 && rows > 0 && colonns > 0) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Vänligen använd naturliga tal i mängden {1...6}");
                rows = -1;
                colonns = -1;
            }
        }
    }
    //
    private static int parseI(String a) {
        int b;

        try {
            b = Integer.parseInt(a);
            return b;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Vänligen skriv endast siffror");
            return (-172);
        }
    }

    private void placerarKort(Card[] a) {

        for (int i = 0; i < (rows * colonns); i++) {
            cardPanel.add(a[i]);
            a[i].addActionListener(this);
        }
    }

    private void newIcon() {
        File[] pictures;
        File folder = new File("/Users/Jmsar/IdeaProjects/Memory/src/mypictures");
        pictures = folder.listFiles();// den här är null
        String[] picturePaths = new String[18]; //nullpointer exception
        for (int i = 0; i < pictures.length; i++) {
            picturePaths[i] = pictures[i].getPath();
            ImageIcon tempimage = new ImageIcon(picturePaths[i]);
            allCards[i] = new Card(tempimage, Card.Status.HIDDEN);
        }
    }

    private void cardsVisible() {
        nCardsVisible++;

        if (nCardsVisible == 4) {
            checkPairs();
            nCardsVisible = 0;
        }
    }

    public void checkPairs() {
        int[] tempIndex = new int[2];
        int temp = 0;
        for (int i = 0; i < cardsInUse.length; i++) {

            if (cardsInUse[i].getStatus() == Card.Status.VISIBLE) {
                tempIndex[temp] = i;
                temp++;
            }
        }
        javax.swing.Timer delay = new javax.swing.Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nPlayers != 1) {
                    if (cardsInUse[tempIndex[0]].equalIcon(cardsInUse[tempIndex[1]])) {
                        cardsInUse[tempIndex[0]].setStatus(Card.Status.MISSING);
                        cardsInUse[tempIndex[1]].setStatus(Card.Status.MISSING);
                        givePoints();
                    } else {
                        cardsInUse[tempIndex[0]].setStatus(Card.Status.HIDDEN);
                        cardsInUse[tempIndex[1]].setStatus(Card.Status.HIDDEN);
                        nextPlayersTurn();
                    }
                } else {
                    if (cardsInUse[tempIndex[0]].equalIcon(cardsInUse[tempIndex[1]])) {
                        cardsInUse[tempIndex[0]].setStatus(Card.Status.MISSING);
                        cardsInUse[tempIndex[1]].setStatus(Card.Status.MISSING);
                        givePoints();
                    } else {
                        cardsInUse[tempIndex[0]].setStatus(Card.Status.HIDDEN);
                        cardsInUse[tempIndex[1]].setStatus(Card.Status.HIDDEN);
                        givePoints();
                    }
                }
                unFreeze = true;
            }
        });
        delay.setRepeats(false);
        unFreeze = false;
        delay.start();

    }

    public void nextPlayersTurn() {
        turnTracker++;

        individualPlayerPanel[turnTracker - 1].setBackground(new Color(255, 0, 205));
        if (turnTracker >= nPlayers) {
            turnTracker = 0;
        }
        individualPlayerPanel[turnTracker].setBackground(Color.green);
    }

    public void givePoints() {
        int temp = parseI(playerPoints[turnTracker].getText());
        temp += 1;
        playerPoints[turnTracker].setText("" + temp);
    }

    public void anounceWinner(){
        String player = "Congratulations:" +  + "you've won the game!";
        JOptionPane.showMessageDialog(null,"Congratulations");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (unFreeze) {
            //Stops user from clicking the same card twice
            for (int i = 0; i < cardsInUse.length; i++) {

                // Finds correct card: If card is not already visible makes it visible and launches cardsVisible()
                if (e.getSource() == cardsInUse[i] && cardsInUse[i].getStatus() != Card.Status.MISSING) {
                    cardsInUse[i].setStatus(Card.Status.VISIBLE);
                    cardsVisible();
                    indexOfLastVisible = i;
                    break;
                }
            }
        }
    }
}