package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc {

  @Override
  public boolean matchJpeg(String jpeg) {
    if(jpeg.matches("(?i).+\\.jpe?g$")) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean matchIp(String ip) {
    if(ip.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean isEmptyLine(String line) {
    if(line.matches("^\\s*$")) {
      return true;
    }
    else {
      return false;
    }
  }

  public static void main(String [] args) {
    RegexExcImp regexExcImp = new RegexExcImp(); //Creating an Instance of RegexExcImp class

    boolean jpeg_res = regexExcImp.matchJpeg("tree.jpg"); //Calling the non-static method matchJpeg on regexExcImp instance
    System.out.println("The string 'tree.jpg' ends with .jpg or .jpeg: "+jpeg_res);

    boolean jpeg_res2 = regexExcImp.matchJpeg("sky.jpeg");
    System.out.println("The string 'sky.jpeg' ends with .jpg or .jpeg: "+jpeg_res2);

    boolean jpeg_res3 = regexExcImp.matchJpeg("bird.jp");
    System.out.println("The string 'bird.jp' ends with .jpg or .jpeg: "+jpeg_res3);

    boolean ip_check = regexExcImp.matchIp("101.10.200.04");
    System.out.println("The IP address '101.10.200.04' is a valid IP address: "+ip_check);

    boolean ip_check2 = regexExcImp.matchIp("140.100.1000.12");
    System.out.println("The IP address '140.100.1000.12' is a valid IP address: "+ip_check2);

    boolean whitespace_check = regexExcImp.isEmptyLine("  ");
    System.out.println("The string '  ' is an empty line: "+whitespace_check);

    boolean whitespace_check2 = regexExcImp.isEmptyLine("  _ ");
    System.out.println("The string '  _ ' is an empty line: "+whitespace_check2);
  }
}
