package com.risevision.userInt;

import com.risevision.Exceptions.NoFieldException;
import com.risevision.Exceptions.NotEqualIdException;
import com.risevision.controller.Controller;
import com.risevision.model.Constants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class UInterface {
    private JFrame mainFrame;
    private Controller controller;
    private JTextField textPath;
    private JTextField textReplace;
    private JTextField textFind;
    private JButton bSearchFile;
    private JButton bFindAndReplace;
    private JLabel lPath;
    private JLabel lReplace;
    private JLabel lFind;
    private JMenu mLanguage;
    private JLabel mInstruction;
    private JLabel mLog;

    private InstructionWindow instructionWindow = InstructionWindow.getInstance(controller);

    private int fileSelected;

    public UInterface(Controller controller){
        this.controller = controller;
    }

    /**
     * load the interface
     */
    public void loadInterface() {
        mainFrame = new JFrame("ChangeId");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 200);
        //mainFrame.setLayout(new GridLayout(4, 1));

        JMenuBar menu = new JMenuBar();

        mLanguage = new JMenu(controller.getLanguage().getString(Constants.languages));
        mInstruction = new JLabel(controller.getLanguage().getString(Constants.instructions));
        JLabel mSpace = new JLabel("   ");
        mSpace.setEnabled(false);
        mLog = new JLabel(controller.getLanguage().getString(Constants.log));

        mInstruction.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showInstructions();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        mLog.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showLog();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        for (int i = 0; i < controller.getAvailableLanguages().length; i++) {
                Locale lang = controller.getAvailableLanguages()[i];
                JMenuItem item = new JMenuItem(lang.getLanguage());
                item.addActionListener(e -> {
                    changeTextField(lang);
                    controller.changeLanguage(lang);
                    changeButtonsLabelText();
                    visualizeSelectedFiles();
                    instructionWindow.changeLanguage();
                    //changeTextField();
                });
                mLanguage.add(item);
        }

        menu.add(mLanguage);
        menu.add(mInstruction);
        menu.add(mSpace);
        menu.add(mLog);
        mainFrame.setJMenuBar(menu);
        JPanel replacePanel = new JPanel();
        setReplacePanel(replacePanel);
        mainFrame.add(replacePanel, BorderLayout.CENTER);
    }

    private void changeTextField() {
        if((textFind.getText().equals(controller.getLanguage().getString(Constants.findText))) ||(textFind.getText().equals(""))){
            textFind.setText(controller.getLanguage().getString(Constants.findText));
        }
        if((textReplace.getText().equals(controller.getLanguage().getString(Constants.replaceText))) ||(textReplace.getText().equals(""))){
            textReplace.setText(controller.getLanguage().getString(Constants.replaceText));
        }
        if((textPath.getText().equals(controller.getLanguage().getString(Constants.pathNoFile))) ||(textPath.getText().equals(""))){
            //oldTextPath=textPath.getText();
            // textPath.setText(newLanguage.getDOCPATH());
        }
    }

    private void changeTextField(Locale locale) {
        if((textFind.getText().equals(controller.getLanguage().getString(Constants.findLabel))) ||(textFind.getText().equals(""))){
            textFind.setText(ResourceBundle.getBundle(Constants.resource,locale).getString(Constants.findLabel));
        }
        if((textReplace.getText().equals(controller.getLanguage().getString(Constants.replaceLabel))) ||(textReplace.getText().equals(""))){
            textReplace.setText(ResourceBundle.getBundle(Constants.resource,locale).getString(Constants.replaceLabel));
        }
        if((textPath.getText().equals(controller.getLanguage().getString(Constants.pathNoFile))) ||(textPath.getText().equals(""))){
            //oldTextPath=textPath.getText();
            // textPath.setText(newLanguage.getDOCPATH());
        }
    }


    private void changeButtonsLabelText() {
        bSearchFile.setText(controller.getLanguage().getString(Constants.findButton));
        bFindAndReplace.setText(controller.getLanguage().getString(Constants.replaceButton));
        lPath.setText(controller.getLanguage().getString(Constants.pathLabel));
        lReplace.setText(controller.getLanguage().getString(Constants.replaceLabel));
        lFind.setText(controller.getLanguage().getString(Constants.findLabel));
        mInstruction.setText(controller.getLanguage().getString(Constants.instructions));
        mLanguage.setText(controller.getLanguage().getString(Constants.languages));
    }

    /**
     * Setting the replace panel
     * @param replacePanel the panel to be set up
     */
    private void setReplacePanel(JPanel replacePanel) {

        replacePanel.setLayout(new GridLayout(4,1));

        JPanel pFind = new JPanel();
        JPanel pPath = new JPanel();
        JPanel pReplace = new JPanel();
        JPanel pReplaceButton = new JPanel();

        textFind = new JTextField( 20);
        textFind.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textFind.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        textReplace = new JTextField( 20);
        textReplace.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textReplace.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        textPath = new JTextField( 20);

        bSearchFile = new JButton();
        bSearchFile.addActionListener(e -> {
            try {
                chooseFile();
            } catch (NotEqualIdException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame,controller.getLanguage().getString("noSameId"),controller.getLanguage().getString("differentIdTitle"),JOptionPane.WARNING_MESSAGE);
            }
        });

        bFindAndReplace = new JButton();
        bFindAndReplace.addActionListener(e -> findReplace());

        lPath = new JLabel();
        lReplace = new JLabel();
        lFind = new JLabel();

        changeButtonsLabelText();
        changeTextField();

        pPath.add(lPath, BorderLayout.WEST);
        pPath.add(textPath, BorderLayout.CENTER);
        pPath.add(bSearchFile, BorderLayout.EAST);

        pFind.add(lFind, BorderLayout.WEST);
        pFind.add(textFind, BorderLayout.CENTER);

        pReplace.add(lReplace, BorderLayout.WEST);
        pReplace.add(textReplace, BorderLayout.CENTER);

        pReplaceButton.add(bFindAndReplace, BorderLayout.CENTER);

        replacePanel.add(pPath);
        replacePanel.add(pFind);
        replacePanel.add(pReplace);
        replacePanel.add(pReplaceButton);
    }

    /**
     * Show the main frame
     */
    public void showInterface(){
        mainFrame.setVisible(true);
    }

    /**
     * Choose files and set them into controller
     */
    private void chooseFile() throws NotEqualIdException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(null);
        controller.setFiles(chooser.getSelectedFiles());
        fileSelected = chooser.getSelectedFiles().length;
        visualizeSelectedFiles();
        String id;
        id = controller.findId();
        textFind.setText(id);
    }

    private void visualizeSelectedFiles(){
        if(fileSelected==0) textPath.setText(controller.getLanguage().getString(Constants.pathNoFile));
        if(fileSelected>1) textPath.setText(controller.getLanguage().getString(Constants.pathFile) + " " + fileSelected + " " + controller.getLanguage().getString(Constants.files) );
        else textPath.setText(controller.getLanguage().getString(Constants.pathFile) + " " + fileSelected + " " + controller.getLanguage().getString(Constants.file) );
    }

    /**
     * call controller replace method
     */
    private void findReplace(){
        try {
            if (textFind.getText().equals("") || textFind.getText() == null){
                throw new NoFieldException(controller.getLanguage().getString(Constants.findText)); }
            if (textReplace.getText().equals("") || textReplace.getText() == null || textReplace.getText().equals(controller.getLanguage().getString("replace text")))
                throw new NoFieldException(controller.getLanguage().getString(Constants.replaceText));
            //if (textPath.getText()==null) throw new NoPathException();
            //controller.setFilePath(textPath.getText());
            controller.setTextFind(textFind.getText());
            controller.setTextReplace(textReplace.getText());
            controller.modifyFile();

            textPath.setText("");
            textReplace.setText("");
            textFind.setText("");
            JOptionPane.showMessageDialog(mainFrame,controller.getLanguage().getString("OK"), controller.getLanguage().getString("OKTitle"),JOptionPane.OK_OPTION);
        }catch (NullPointerException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,controller.getLanguage().getString("noFile"), controller.getLanguage().getString("noFile"),JOptionPane.WARNING_MESSAGE);
            controller.printLog(e.getLocalizedMessage());
        } catch (NoFieldException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,controller.getLanguage().getString("emptyField") + e.getMessage(), e.getMessage() + controller.getLanguage().getString("emptyFieldTitle"),JOptionPane.WARNING_MESSAGE);
            controller.printLog(e.getMessage());
            controller.printLog(e.getStackTrace().toString());
        }

    }




}
