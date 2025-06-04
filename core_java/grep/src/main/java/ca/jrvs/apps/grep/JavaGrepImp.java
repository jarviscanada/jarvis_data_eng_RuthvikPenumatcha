package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {
  private final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);
  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public void process () throws IOException {
    //Top level search workflow
    List<String> matchedLines = new ArrayList<>(); //Dynamically add matchedLines

    for(File inputFile : listFiles(getRootPath())) {
      for(String line: readLines(inputFile)) {
        if(ContainsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }

    //Saving the matchedLines in output file
    //logger.debug("Matched lines: {}", matchedLines);
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> list = new ArrayList<>();
    //logger.debug("Current working directory: {}", System.getProperty("user.dir"));
    //logger.debug("Root dir: {}", rootDir);

    try {
      File dir = new File(rootDir);

      if(dir.isDirectory()) {
        identifyAllFiles(dir, list); //Recursively add all the files in the directory
      }
      else {
        throw new IllegalArgumentException("The directory: "+rootDir+" is not valid. Please enter a valid directory as your input.");
      }

      //logger.debug("Files list: {}", list);
      return list;
    }
    catch(Exception e) {
      logger.error(e.getMessage());
    }
    return list;
  }

  public void identifyAllFiles(File dir, List<File> list) {
    //A function that is created to recursively traverse all the files in the directory, and add the files to list
    File[] files_in_directory = dir.listFiles(); //List all the files in the directory

    if(files_in_directory != null) {
      for(File file : files_in_directory) {
        if(file.isFile()) {
          list.add(file);
        }
        else if(file.isDirectory()) {
          identifyAllFiles(file, list);
        }
      }
    }
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> list = new ArrayList<>();

    try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
      String line;

      while ((line = reader.readLine()) != null) {
        list.add(line);
      }

      return list;
    }
    catch (IOException e) {
      logger.error(e.getMessage());
    }
    return list;
  }

  @Override
  public boolean ContainsPattern(String line) {
    if(line.matches(getRegex())) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    //Store the results into output file
    //logger.debug("Writing to file: {}", getOutFile());

    try(BufferedWriter writer = new BufferedWriter(new FileWriter(getOutFile(), true))) {
      for(String line : lines) {
        writer.write(line);
        writer.newLine();
      }

      logger.debug("Successfully written to file: {}", getOutFile());
    }
    catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public static void main(String [] args) {
    if(args.length != 3) {
      throw new IllegalArgumentException("Expected Input: JavaGrep regex rootPath outFile");
    }

    BasicConfigurator.configure(); //Using default logger

    JavaGrepImp javaGrepImp = new JavaGrepImp(); //Creating an Object of JavaGrepImp class

    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);
    javaGrepImp.logger.debug("Running the application with Regex={}, RootPath={}, OutFile={}", javaGrepImp.getRegex(), javaGrepImp.getRootPath(), javaGrepImp.getOutFile());

    try {
      javaGrepImp.process();
    }
    catch (Exception e) {
      javaGrepImp.logger.error(e.getMessage()); //Logging error message
    }
  }
}
