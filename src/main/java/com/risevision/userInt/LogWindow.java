package com.risevision.userInt;

import javax.swing.*;
import java.awt.*;

public class LogWindow {

    private static LogWindow instance;

    private JFrame jFrame;
    private JTextArea textArea;
    private JButton clearButton;

    public static LogWindow getInstance() {
        if(instance==null) instance = new LogWindow();
        return instance;
    }

    private LogWindow(){
        jFrame = new JFrame("LogWindow");
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setSize(200,200);

        JPanel panel= new JPanel();
        //panel.setLayout(new GridLayout(2,1));

        textArea = new JTextArea();

        textArea.setLineWrap(true);
        textArea.setAutoscrolls(true);

        clearButton = new JButton("Clear");

        clearButton.addActionListener(e -> clearText());

        panel.add(textArea);
        panel.add(clearButton, BorderLayout.SOUTH);

        jFrame.add(panel);
    }

    public void showLog(){
        jFrame.setVisible(true);
    }

    public void print(String message){
        textArea.append(message + "\n");
    }

    public void clearText(){
        textArea.setText("");
    }

}
