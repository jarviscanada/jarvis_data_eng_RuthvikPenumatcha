package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

  /**
   * Top level search workflow
   * @throws IOException
   */
  void process() throws IOException;

  /**
   * Traverse a given directory and return all the files in the directory
   * @param rootDir input Directory
   * @return files under the directory
   */
  List<File> listFiles(String rootDir);

  /**
   * Read a file and return all the lines
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  List<String> readLines(File inputFile);

  /**
   * Check if a line contains the regex pattern passed by the user
   * @param line input string
   * @return true if there is a match, false otherwise
   */
  boolean ContainsPattern(String line);

  /**
   * Write lines to a file
   * @param lines matched line
   * @throws IOException if write fails
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}
