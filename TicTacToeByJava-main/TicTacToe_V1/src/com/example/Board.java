package com.example;

import javax.swing.*;
import javax.imageio.ImageIO;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel{
    private static final int row = 3;
    private static final int col = 3;

    private Image imgX;
    private Image imgO;
    private final Cell[][] matrix  = new Cell[row][col];
    private String currentPLayer = Cell.EMPTY_VALUE;

    private EndGameListener endGameListener;
    public static final int ST_DRAW = 0;
    public static final int ST_WIN = 1;
    public static final int ST_CONTINUE = 2;

    public Board(String player) {
        this();
        this.currentPLayer = player;
    }
    public Board() {
        this.initMatrix();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();

                if(currentPLayer.equals(Cell.EMPTY_VALUE)) {
                    return;
                }

                soundClick();

                for(int i = 0; i < row; i++) {
                    for(int j = 0; j < col; j++) {
                        Cell cell = matrix[i][j];

                        int clickXStart = cell.getX();
                        int clickYStart = cell.getY();

                        int clickXEnd = clickXStart + cell.getWidth();
                        int clickYEnd = clickYStart + cell.getHeight();

                        if(x >= clickXStart && x <= clickXEnd && y >= clickYStart && y <= clickYEnd) {
                            if(cell.getValue().equals(Cell.EMPTY_VALUE)) {
                                cell.setValue(currentPLayer);
                                repaint();

                                int res = checkWin(currentPLayer);
                                if(endGameListener != null) {
                                    endGameListener.end(currentPLayer, res);
                                }

                                if(res == ST_CONTINUE) {
                                    currentPLayer = currentPLayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
                                }
                            }
                        }
                    }
                }
            }
        });

        try {
            imgX = ImageIO.read(getClass().getResourceAsStream("x.png"));
            imgO = ImageIO.read(getClass().getResourceAsStream("o.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void soundClick() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
//                    URL url = getClass().getResource("/clickSound.wav");
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("clickSound.wav"));
                    clip.open(audioInputStream);
                    clip.start();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    private void initMatrix() {
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                Cell cell = new Cell();
                matrix[i][j] = cell;
            }
        }
    }

    public void setCurrentPLayer(String currentPLayer) {
        this.currentPLayer = currentPLayer;
    }

    public void setEndGameListener(EndGameListener endGameListener) {
        this.endGameListener = endGameListener;
    }

    public String getCurrentPLayer() {
        return currentPLayer;
    }

    public void reset() {
        this.initMatrix();
        this.setCurrentPLayer(Cell.EMPTY_VALUE);
        repaint();
    }

    public int checkWin(String player){
        // First diagonal
        if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)){
            return ST_WIN;
        }

        // Second diagonal
        if(this.matrix[0][2].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][0].getValue().equals(player)){
            return ST_WIN;
        }

        // Row 1
        if(this.matrix[0][0].getValue().equals(player) && this.matrix[0][1].getValue().equals(player) && this.matrix[0][2].getValue().equals(player)){
            return ST_WIN;
        }

        // Row 2
        if(this.matrix[1][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[1][2].getValue().equals(player)){
            return ST_WIN;
        }

        // Row 3
        if(this.matrix[2][0].getValue().equals(player) && this.matrix[2][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)){
            return ST_WIN;
        }

        // Col 1
        if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][0].getValue().equals(player) && this.matrix[2][0].getValue().equals(player)){
            return ST_WIN;
        }

        // Col 2
        if(this.matrix[0][1].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][1].getValue().equals(player)){
            return ST_WIN;
        }

        // Col 3
        if(this.matrix[0][2].getValue().equals(player) && this.matrix[1][2].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)){
            return 1;
        }


        if(this.isFull()){
            return ST_DRAW;
        }

        return ST_CONTINUE;
    }

    private boolean isFull(){
        int k = 0;
        for(int i = 0 ; i < row; i++){
            for(int j = 0 ; j < col; j++){
                Cell cell = matrix[i][j];
                if(!cell.getValue().equals(Cell.EMPTY_VALUE)){
                    k++;
                }
            }
        }
        return k == col * row;
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth()/3;
        int height = getHeight()/3;

        Graphics2D graphics2D = (Graphics2D) g;

        int k = 0;
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                int x = j * width;
                int y = i * height;

                Cell cell = matrix[i][j];
                cell.setX(x);
                cell.setY(y);
                cell.setWeight(width);
                cell.setHeight(height);

                Color color = k%2 == 0 ? Color.pink : Color.white;
                graphics2D.setColor(color);
                graphics2D.fillRect(x, y, width, height);

                if(cell.getValue().equals(Cell.X_VALUE)) {
                    Image img = imgX;
                    graphics2D.drawImage(img, x, y, width, height, this);
                } else if(cell.getValue().equals(Cell.O_VALUE)) {
                    Image img = imgO;
                    graphics2D.drawImage(img, x, y, width, height, this);
                }

                k++;
            }
        }
    }
}
