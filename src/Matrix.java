import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

public class Matrix extends JPanel {

    Random pick;

    private final JPanel leftPanel;
    private final JPanel rightTopPanel;
    private final JPanel rightMiddlePanel;

    private final JButton[][] buttonMatrix;
    private final Game game;

    private final JButton[] bufferButton;
    private final JButton[] sequenceButton;

    private final int matrixSize;
    private final int bufferSize;
    private final int sequenceSize;

    private int winToken;
    private boolean token = true; //0 = coll, 1 = row
    private boolean firstCheck = false;

    private int timeLeft = 60;
    private final Timer timer;

    private final JLabel timerLabel;
    private final ButtonValues[][] matrixParameters;

    Matrix(int buffer) {

        pick = new Random();

        JLabel background;
        ImageIcon img = new ImageIcon(this.getClass().getResource("/background.jpg"));
        background = new JLabel(img);
        background.setBounds(0, 0, 1010, 660);
        leftPanel = new JPanel();
        rightTopPanel = new JPanel();
        rightMiddlePanel = new JPanel();
        matrixSize = buffer + 4;
        bufferSize = matrixSize - 1;
        sequenceSize = matrixSize - 3;
        winToken = bufferSize;
        timer = new java.util.Timer();

        JLabel bufferLabel = new JLabel("Rozmiar bufora, do którego możesz załadować elementy:");
        JLabel sequenceLabel = new JLabel("Sekwencja, którą musisz ułożyć:");
        timerLabel = new JLabel("Pozostały czas to: " + timeLeft + "s");

        this.add(leftPanel);
        this.add(rightTopPanel);
        this.add(rightMiddlePanel);
        this.setLocation(240, 10);
        this.setSize(1010, 660);
        this.setBackground(Color.lightGray);
        this.setLayout(null);
        this.add(bufferLabel);
        this.add(sequenceLabel);
        this.add(timerLabel);
        this.add(background);

        GridLayout matrixLayout = new GridLayout(matrixSize, matrixSize);
        GridLayout bufferLayout = new GridLayout(1, matrixSize);

        leftPanel.setBounds(20, 50, 540, 540);
        leftPanel.setLayout(matrixLayout);

        rightTopPanel.setBounds(575, 80, matrixSize * 60, 60);
        rightTopPanel.setLayout(bufferLayout);

        rightMiddlePanel.setBounds(575, 260, (matrixSize - 1) * 60, 60);
        rightMiddlePanel.setLayout(bufferLayout);

        buttonMatrix = new JButton[matrixSize][matrixSize];
        bufferButton = new JButton[bufferSize];
        sequenceButton = new JButton[bufferSize - 1];

        bufferLabel.setBounds(575, 20, 460, 80);
        bufferLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        sequenceLabel.setBounds(575, 200, 540, 40);
        sequenceLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        timerLabel.setBounds(580,440,460,80);
        timerLabel.setFont(new Font("Serif", Font.PLAIN, 22));

        game = new Game(matrixSize);
        matrixParameters = new ButtonValues[matrixSize][matrixSize];

        run();
    }

    private void run() {                                                                                                //uruchomienie funkcji
        initMatrix();
        initBuffer();
        initSequence();
        pickSequence();
        prepareMatrix();
        clickButton();
        timerStart();
        findElements();
        defaultBorder();
    }


    private void timerStart()                                                                                           //Odliczanie do końca gry
    {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerLabel.setText("Pozostały czas to: " + timeLeft + "s");
                timeLeft--;
                if (timeLeft <= 0){
                    timerLabel.setText("Czas się skończył");
                    timer.cancel();
                    lost();
                }
                if (winToken==1)
                {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public void timerStop()
    {
        timer.cancel();
    }

    private void initBuffer()                                                                                           //inicjalizuje pusty bufor
    {
        for (int i = 0; i < bufferSize; i++) {
            bufferButton[i] = new JButton(" ");
            bufferButton[i].setBackground(Color.yellow);
            rightTopPanel.add(bufferButton[i]);
        }
    }


    private void initSequence()                                                                                         //inicjalizuje pustą sekwencję
    {
        for (int i = 0; i < bufferSize - 1; i++) {
            sequenceButton[i] = new JButton(" ");
            sequenceButton[i].setBackground(Color.orange);
            rightMiddlePanel.add(sequenceButton[i]);
        }
    }


    private void initMatrix()                                                                                           //inicjalizuje tablicę przycisków z wartościami
    {
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                buttonMatrix[i][j] = new JButton();
                buttonMatrix[i][j].setText(game.getMatrixValue(i, j).toString());
                buttonMatrix[i][j].setBackground(Color.GRAY);
                buttonMatrix[i][j].setForeground(Color.ORANGE);
                matrixParameters[i][j] = new ButtonValues();
                leftPanel.add(buttonMatrix[i][j]);
            }
        }
    }


    private void pickSequence()                                                                                         //wybiera sekwencję
    {
        int elementNumber = sequenceSize;
        int numberY;
        int numberX;
        int lastNumber;
        boolean switchToken = true; // true - row, false - coll

        numberX = pick.nextInt(matrixSize);
        numberY = pick.nextInt(matrixSize);

        sequenceButton[sequenceSize - elementNumber].setText(game.getMatrixValue(numberY, numberX).toString());
        matrixParameters[numberY][numberX].pickValue();
        elementNumber--;


        if (sequenceSize - elementNumber == 1)
        {
            if (numberY == 0) //buffer good start
            {
                lastNumber = numberY;
                numberY = pick.nextInt(matrixSize);
                while (numberY == lastNumber || matrixParameters[numberY][numberX].ifPickedFunction()) {
                    numberY = pick.nextInt(matrixSize);
                }
                sequenceButton[sequenceSize - elementNumber].setText(game.getMatrixValue(numberY, numberX).toString());
                switchToken = true;
            }
            else //buffer bad start
            {

                lastNumber = numberX;
                numberX = pick.nextInt(matrixSize);
                while (numberX == lastNumber || matrixParameters[numberY][numberX].ifPickedFunction())
                {
                    numberX = pick.nextInt(matrixSize);
                }
                sequenceButton[sequenceSize - elementNumber].setText(game.getMatrixValue(numberY, numberX).toString());
                switchToken = false;

            }
            elementNumber--;
        }
        while (elementNumber >= 0) {
            if (switchToken) {
                lastNumber = numberX;
                numberX = pick.nextInt(matrixSize);
                while (numberX == lastNumber || matrixParameters[numberY][numberX].ifPickedFunction()) {
                    numberX = pick.nextInt(matrixSize);
                }
                sequenceButton[sequenceSize - elementNumber].setText(game.getMatrixValue(numberY, numberX).toString());
                switchToken = false;
            } else {
                lastNumber = numberY;
                numberY = pick.nextInt(matrixSize);
                while (numberY == lastNumber || matrixParameters[numberY][numberX].ifPickedFunction()) {
                    numberY = pick.nextInt(matrixSize);
                }
                sequenceButton[sequenceSize - elementNumber].setText(game.getMatrixValue(numberY, numberX).toString());
                switchToken = true;
            }
            elementNumber--;
        }
    }


    private void prepareMatrix()                                                                                         //przygotowuje i koloruje tablice aby rozpocząć grę
    {
        for (int i = 1; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                buttonMatrix[i][j].setEnabled(false);
                buttonMatrix[i][j].setBackground(Color.darkGray);
            }
        }
    }


    private void offMatrix()                                                                                             //blokuje przyciski tablicy
    {
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                buttonMatrix[i][j].setEnabled(false);
            }
        }
    }


    private void colorButton(int numberX, int numberY)                                                                   //koloruje wiersze/kolumny tablicy oraz blokuje przycisk
    {
        offMatrix();
        buttonMatrix[numberX][numberY].setBackground(Color.ORANGE);
        matrixParameters[numberX][numberY].clickValue();
        token ^= true;
        for (int i = 0; i < matrixSize; i++) {
            if (token) {
                buttonMatrix[numberX][i].setEnabled(true);
                buttonMatrix[numberX][i].setBackground(Color.GRAY);
                if (matrixParameters[numberX][i].ifClickedFunction()) {
                    buttonMatrix[numberX][i].setEnabled(false);
                }
            }
            if (!token) {
                buttonMatrix[i][numberY].setEnabled(true);
                buttonMatrix[i][numberY].setBackground(Color.GRAY);
                if (matrixParameters[i][numberY].ifClickedFunction()) {
                    buttonMatrix[i][numberY].setEnabled(false);
                }
            }
        }
        buttonMatrix[numberX][numberY].setEnabled(false);
    }


    private void checkWin(int i) {                                                                                      //porównuje sekwencję z buforem
        if (i == 0) {
            if (bufferButton[i].getText().equals(sequenceButton[i].getText())) {
                bufferButton[i].setForeground(Color.GREEN);
                sequenceButton[i].setForeground(Color.GREEN);
                winToken--;
                firstCheck = true;
            } else {
                firstCheck = false;
            }
        } else if (i > 0) {
            if (firstCheck) {
                if (bufferButton[i].getText().equals(sequenceButton[i].getText())) {
                    bufferButton[i].setForeground(Color.GREEN);
                    sequenceButton[i].setForeground(Color.GREEN);
                    winToken--;
                } else if (bufferButton[i].getText().equals(sequenceButton[i - 1].getText()) && i == 1) {
                    bufferButton[i - 1].setForeground(Color.RED);
                    bufferButton[i].setForeground(Color.GREEN);
                    firstCheck = false;
                }
            } else {
                if (bufferButton[i].getText().equals(sequenceButton[i - 1].getText())) {
                    bufferButton[i].setForeground(Color.GREEN);
                    sequenceButton[i - 1].setForeground(Color.GREEN);
                    winToken--;
                }
            }
        }
        if (i >= 1) {
            if (!bufferButton[i].getText().equals(sequenceButton[i].getText()) &&
                    !bufferButton[i].getText().equals(sequenceButton[i - 1].getText())) {
                lost();
            }

        }
        if (winToken == 1) {
            won();
        }
    }


    private void won()                                                                                                  //komunikat zwycięstwa
    {
        JOptionPane.showInternalMessageDialog(null,
                "Wygrałes, aby zagrać ponownie kliknij Uruchom Ponownie",
                "Win", JOptionPane.INFORMATION_MESSAGE);
        offMatrix();
    }


    private void lost()                                                                                                 //komunikat porażki
    {
        JOptionPane.showInternalMessageDialog(null,
                "Przegrałes, aby zagrać ponownie kliknij Uruchom ponownie",
                "Win", JOptionPane.ERROR_MESSAGE);
        offMatrix();
    }


    private void findElements()                                                                                         //Podswietlenie szukanego elementu w tablicy
    {
        for (int k = 0; k < bufferSize-1; k++)
        {
            int finalK = k;
            sequenceButton[k].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    defaultBorder();
                    for(int i = 0; i<matrixSize; i++)
                    {
                        for(int j = 0; j<matrixSize; j++)
                        {
                            if(sequenceButton[finalK].getText().equals(buttonMatrix[j][i].getText()))
                            {
                                buttonMatrix[j][i].setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
                            }
                        }
                    }
                }
            });
        }
    }


    private void defaultBorder()                                                                                        //ustawienie domyslnej wartosci tablicy
    {
        for(int i = 0; i<matrixSize; i++)
        {
            for(int j = 0; j<matrixSize; j++)
            {
                buttonMatrix[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 4));
            }
        }
    }


    private void clickButton() {                                                                                         //kliknięcie przycisku
        for (int i = 0; i < matrixSize; i++) {
             for (int j = 0; j < matrixSize; j++) {
                 int finalI = i; // Y
                 int finalJ = j; // X
                 buttonMatrix[i][j].addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         defaultBorder();
                         try {
                             for (int k = 0; k <= bufferSize; k++) {
                                 if (bufferButton[k].getText().equals(" ")) {
                                     bufferButton[k].setText(game.getMatrixValue(finalI, finalJ).toString());
                                     colorButton(finalI,finalJ);
                                     checkWin(k);
                                     break;
                                 }
                             }
                         }catch (ArrayIndexOutOfBoundsException ex) {
                             if (winToken==1)
                                 won();
                             else
                                 lost();
                         }
                         }
                 });
             }
         }
    }
}



