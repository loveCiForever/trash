package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static Timer timer = new Timer();
    private static int sec = 0;
    private static JLabel lblTime;
    private static JButton  btnStart;
    private static Board board;

    public static void main(String[] args) {
        board = new Board();
        board.setEndGameListener((player, st) -> {
            if(st == Board.ST_WIN) {
                JOptionPane.showMessageDialog(null, "Player " + player + " Win");
                stopGame();
            } else if(st == Board.ST_DRAW) {
                JOptionPane.showMessageDialog(null, "Draw");
                stopGame();
            }
        });

        JPanel jPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(boxLayout);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        board.setPreferredSize(new Dimension(600, 600));

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(flowLayout);

        btnStart = new JButton("Start");
        lblTime = new JLabel("00:00");

        bottomPanel.add(lblTime);
        bottomPanel.add(btnStart);

        btnStart.addActionListener(e -> {
            if(btnStart.getText().equals("Start")) {
                startGame();
            } else {
                stopGame();
            }
        });

        jPanel.add(board);
        jPanel.add(bottomPanel);

        JFrame jFrame = new JFrame("Tic Tac Toe");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(true);
        jFrame.add(jPanel);

        int x = (int) ((dimension.getWidth()/2) - (jFrame.getWidth()/2));
        int y = (int) ((dimension.getHeight()/2) - (jFrame.getHeight()/2));

        jFrame.setLocation(x, y); // Set frame at the center of screen.
        jFrame.pack();
        jFrame.setVisible(true); // show frame
    }

    public static void startGame() {
        int choice = JOptionPane.showConfirmDialog(null, "X play first ?", "Who play first ?", JOptionPane.YES_NO_OPTION);
        board.reset();
        String currentPlayer = (choice == 0) ? Cell.X_VALUE : Cell.O_VALUE;
        board.setCurrentPLayer(currentPlayer);

        sec = 0;
        lblTime.setText("0:0");
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sec++;
                String value = sec/60 + ":" + sec%60;
                lblTime.setText(value);

            }
        }, 1000, 1000);
        btnStart.setText("Stop");
    }

    private static void stopGame() {
        btnStart.setText("Start");

        sec = 0;
        lblTime.setText("0:0");

        timer.cancel();
        timer = new Timer();

        board.reset();

    }
}