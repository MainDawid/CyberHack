import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationFrame extends JFrame implements ActionListener {

    Matrix matrix;
    private final JButton startGameButton;
    private final JComboBox bufferCombo;
    private boolean createdMatrix = false;
    private final JLabel background;

    ApplicationFrame()
    {
        ImageIcon img = new ImageIcon(this.getClass().getResource("/background.jpg"));
        background = new JLabel(img);
        background.setBounds(240, 10, 1010, 660);

        this.setTitle("CyberHack");
        this.getContentPane().setBackground(Color.darkGray);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false );
        this.setSize(1280, 720);
        this.setLayout(null);
        this.add(background);

        startGameButton = new JButton();
        startGameButton.addActionListener(this);
        startGameButton.setSize(160,50);
        startGameButton.setText("Rozpocznij grę");
        startGameButton.setLocation(40, 100);
        startGameButton.setBackground(Color.orange);

        String[] bufferOptions = {"3", "4", "5", "6"};
        bufferCombo = new JComboBox(bufferOptions);
        bufferCombo.setSize(160,40);
        bufferCombo.setLocation(40,50);
        bufferCombo.setBackground(Color.orange);

        JLabel startLabel = new JLabel("Wybierz rozmiar bufora:");
        startLabel.setBounds(50, 20, 160, 20);
        startLabel.setFont(new Font("Serif",Font.PLAIN, 16));
        startLabel.setForeground(Color.ORANGE);

        this.add(bufferCombo);
        this.add(startGameButton);
        this.add(startLabel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startGameButton) {
            int bufferSize = bufferCombo.getSelectedIndex();
            if(!createdMatrix)
            {
                this.remove(background);
                createdMatrix = true;
            }
            else
                {
                    this.remove(matrix);
                    matrix.timerStop();
                }
            matrix = new Matrix(bufferSize);
            this.add(matrix);
            startGameButton.setText("Uruchom ponownie");
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
}

