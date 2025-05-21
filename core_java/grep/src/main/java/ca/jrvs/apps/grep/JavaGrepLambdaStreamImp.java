package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaStreamImp extends JavaGrepImp {
  private final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaStreamImp.class);

  @Override
  public void process () throws IOException {
    //Top level search workflow
    List<String> matchedLines = listFiles(getRootPath()).stream().flatMap(file -> {
      try {
        return Files.lines(file.toPath());
      }
      catch (IOException e) {
        logger.error(e.getMessage(), e);
      }
      return Stream.empty();
    }).filter(this::ContainsPattern).collect(Collectors.toList());

    //Saving the matchedLines in output file
    logger.debug("Matched lines: {}", matchedLines);
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> list = new ArrayList<>();
    logger.debug("Current working directory: {}", System.getProperty("user.dir"));
    logger.debug("Root dir: {}", rootDir);

    try {
      File dir = new File(rootDir);

      if(!dir.isDirectory()) {
        throw new IllegalArgumentException("The directory: "+rootDir+" is not valid. Please enter a valid directory as your input.");
      }

      Stream<File> pathStream = Files.walk(Paths.get(rootDir)).map(Path::toFile).filter(file -> Files.isRegularFile(file.toPath()));

      list = pathStream.collect(Collectors.toList());

      logger.debug("Files list: {}", list);
      return list;
    }
    catch(Exception e) {
      logger.error(e.getMessage());
    }

    return list;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> list = new ArrayList<>();

    try(Stream<String> lines = Files.lines(inputFile.toPath())) {
      list = lines.collect(Collectors.toList());
      return list;
    }
    catch (IOException e) {
      logger.error(e.getMessage());
    }
    return list;
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    //Store the results into output file
    logger.debug("Writing to file: {}", getOutFile());

    try(BufferedWriter writer = new BufferedWriter(new FileWriter(getOutFile(), true))) {
      lines.stream().forEach(line -> {
        try {
          writer.write(line);
          writer.newLine();
        }
        catch (IOException e) {
          logger.error(e.getMessage());
        }
      });

      logger.debug("Successfully written to file: {}", getOutFile());
    }
    catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  public static void main (String [] args) {
    if(args.length != 3) {
      throw new IllegalArgumentException("Expected Input: JavaGrep regex rootPath outFile");
    }

    BasicConfigurator.configure(); //Using default logger

    JavaGrepLambdaStreamImp javaGrepLambdaStreamImpImp = new JavaGrepLambdaStreamImp(); //Creating an Object of JavaGrepImp class

    javaGrepLambdaStreamImpImp.setRegex(args[0]);
    javaGrepLambdaStreamImpImp.setRootPath(args[1]);
    javaGrepLambdaStreamImpImp.setOutFile(args[2]);
    javaGrepLambdaStreamImpImp.logger.debug("Running the application with Regex={}, RootPath={}, OutFile={}", javaGrepLambdaStreamImpImp.getRegex(), javaGrepLambdaStreamImpImp.getRootPath(), javaGrepLambdaStreamImpImp.getOutFile());

    try {
      javaGrepLambdaStreamImpImp.process();
    }
    catch (Exception e) {
      javaGrepLambdaStreamImpImp.logger.error(e.getMessage()); //Logging error message
    }
  }
}
