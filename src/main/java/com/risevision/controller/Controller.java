package com.risevision.controller;

import com.risevision.Exceptions.NotEqualIdException;
import com.risevision.model.Constants;
import com.risevision.userInt.InstructionWindow;
import com.risevision.userInt.LogWindow;
import com.risevision.userInt.UInterface;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class Controller {

    private InstructionWindow instructions;
    private LogWindow logWindow;
    private File[] files;
    private String textFind, textReplace;
    private Locale[] supportedLocale={
            Locale.ITALIAN,
            Locale.ENGLISH
    };
    //TODO remove hardcoded supported languages reading the name of all properties file search for the correct language
    private ResourceBundle language;

    private File[] filesDirectory = new File[3];

    /**
     * Load the program
     */
    public Controller()  {

        filesDirectory[0]= new File("C://ChangeId/");
        filesDirectory[1]=new File("C://ChangeId/data.properties");
        filesDirectory[2]=new File("C://ChangeId/languages");

        try {
            checkDirectoryExist();
        } catch (IOException e) {
            e.printStackTrace();
            printLog(e.getMessage());
        }

        loadLanguages();
        loadLastConfiguration();
        loadInstructions();
        loadLog();
        showInstructions();
        loadInterface();
    }

    private void loadLastConfiguration() {

        ResourceBundle config = ResourceBundle.getBundle("data");

        Locale lastLanguage = new Locale(config.getString(Constants.language));

        changeLanguage(lastLanguage);
    }

    private void loadInstructions() {
        instructions = InstructionWindow.getInstance(this);
    }

    public ResourceBundle getLanguage() {
        return language;
    }

    public void setTextFind(String textFind) {
        this.textFind = textFind;
    }

    public void setTextReplace(String textReplace) {
        this.textReplace = textReplace;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    /**
     * modify the files replacing every instance of textFind with textReplace
     */
    public void modifyFile()
    {
            for (File file : files) {

                File fileToBeModified = new File(file.getAbsolutePath());
                StringBuilder oldContent = new StringBuilder();
                FileWriter fileWriter;
                fileWriter = null;
                BufferedReader bufferedReader;
                bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new FileReader(fileToBeModified));
                    //Reading all the lines of input text file into oldContent
                    String line = bufferedReader.readLine();
                    while (null != line) {
                        oldContent.append(line).append(System.lineSeparator());
                        line = bufferedReader.readLine();
                    }
                    //Replacing oldString with newString in the oldContent
                    String newContent = oldContent.toString().replaceAll(textFind, textReplace);
                    //Rewriting the input text file with newContent
                    fileWriter = new FileWriter(fileToBeModified);
                    fileWriter.write(newContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //Closing the resources
                        Objects.requireNonNull(bufferedReader).close();
                        Objects.requireNonNull(fileWriter).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

    }

    /**
     * Gets the language that are supported in the application
     * @return the array of supported languages
     */
    //TODO Modify this method to load the languages directly from the properties file
    public Locale[] getAvailableLanguages(){
        return supportedLocale;
    }

    /**
     * load the interface with the language setted in this class
     */
    private void loadInterface() {
        UInterface uInterface = new UInterface(this);
        uInterface.loadInterface();
        uInterface.showInterface();
    }

    /**
     * Load the default language
     */
    private void loadLanguages()  {

        File pathLanguages = filesDirectory[2];

        System.out.println(pathLanguages.getAbsolutePath());
        URL[] urls = new URL[0];
        try {
            urls = new URL[]{pathLanguages.toURI().toURL()};
        } catch (MalformedURLException e) {
            printLog(e.getMessage());
        }
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle.getBundle("changeId", Locale.getDefault(), loader);
        language = ResourceBundle.getBundle("changeId");
    }

    private void checkDirectoryExist() throws IOException {

        for (File file : filesDirectory) {
            if(!file.exists()){
                file.mkdirs();
                file.createNewFile();
            }
        }

    }

    public void changeLanguage(Locale newLanguage){
        language = ResourceBundle.getBundle("changeId",newLanguage);

        Properties properties;

        try {

            File fileProperty = filesDirectory[1];
            File pathProperty = filesDirectory[0];


            FileInputStream inputStream;
            inputStream = new FileInputStream(fileProperty);
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();

            FileOutputStream outputStream;
            outputStream = new FileOutputStream(fileProperty);
            properties.setProperty(Constants.language,newLanguage.getLanguage());
            properties.store(outputStream,null);
            properties.setProperty(Constants.pathFile,pathProperty.getAbsolutePath());
            properties.store(outputStream,null);
            outputStream.close();
        } catch (FileNotFoundException e) {
            //TODO Show an error window that say : data file not found. Settings cannot be memorized!
            printLog(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            printLog(e.getMessage());
        }


    }

    public void showInstructions() {
        if(!instructions.isVisible()) instructions.setVisible(true);
    }


    /*
    -----------------DEVELOPING---------------DEVELOPING-------------
     */
    public String findId() throws NotEqualIdException {
        String id = null;
        for (File file : files) {
            File fileToBeModified = new File(file.getAbsolutePath());
            BufferedReader bufferedReader = null;
            try
            {
                bufferedReader = new BufferedReader(new FileReader(fileToBeModified));
                //Reading all the lines of input text file into oldContent
                String line = bufferedReader.readLine();
                while (line != null)
                {
                    if( line.contains("companyId")){
                        String newId;
                        newId = line.substring(line.indexOf(Constants.comId) + Constants.comId.length(),line.indexOf(Constants.filName));
                        if(id!=null) {
                            if (!id.equals(newId)) throw new NotEqualIdException();
                        }
                        else{
                            id = newId;
                        }
                    }
                    line = bufferedReader.readLine();
                }

            }
            catch (IOException e)
            {
                printLog(e.getMessage());
            } finally
            {
                try
                {
                    //Closing the resources
                    assert bufferedReader != null;
                    bufferedReader.close();
                }
                catch (IOException e)
                {
                    printLog(e.getMessage());
                }
            }
        }
        return id;
    }

    public void loadLog(){
        logWindow = LogWindow.getInstance();
    }

    public void showLog() {
        logWindow.showLog();
    }

    public void printLog(String message) {
        logWindow.print(message);
    }
}
