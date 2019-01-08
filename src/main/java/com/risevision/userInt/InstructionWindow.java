package com.risevision.userInt;

import com.risevision.controller.Controller;
import com.risevision.model.Constants;

import javax.swing.*;
import java.awt.*;

public class InstructionWindow {

    private static InstructionWindow instance;



    private JFrame instructionFrame;
    private Controller controller;
    private JLabel step1Label = new JLabel();
    private JLabel step2Label = new JLabel();
    private JLabel step3Label = new JLabel();
    private JLabel step4Label = new JLabel();
    private JButton button = new JButton("OK");

    public static InstructionWindow getInstance(Controller controller) {
        if(instance==null) instance= new InstructionWindow(controller);
        return instance;
    }

    private InstructionWindow(Controller controller){
        this.controller = controller;
        loadInstructionWindow();
    }

    private void loadInstructionWindow() {
        instructionFrame = new JFrame();
        instructionFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        instructionFrame.setSize(600, 200);

        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new GridLayout(5, 1));

        button.addActionListener(e -> instructionFrame.setVisible(false));

        instructionPanel.add(step1Label);
        instructionPanel.add(step2Label);
        instructionPanel.add(step3Label);
        instructionPanel.add(step4Label);
        instructionPanel.add(button);

        instructionFrame.add(instructionPanel);

        setLabels();

    }

    private void setLabels() {
        instructionFrame.setTitle(controller.getLanguage().getString(Constants.instructions));
        step1Label.setText(controller.getLanguage().getString(Constants.step1));
        step2Label.setText(controller.getLanguage().getString(Constants.step2));
        step3Label.setText(controller.getLanguage().getString(Constants.step3));
        step4Label.setText(controller.getLanguage().getString(Constants.step4));
    }

    public void setVisible(boolean visible){
        instructionFrame.setVisible(visible);
    }

    public boolean isVisible(){
        return instructionFrame.isVisible();
    }

    void changeLanguage(){
        if (this.isVisible()){
            setLabels();
        }
    }



}
