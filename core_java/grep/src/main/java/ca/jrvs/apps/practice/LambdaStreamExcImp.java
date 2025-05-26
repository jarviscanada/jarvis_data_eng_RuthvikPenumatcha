package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

  @Override
  public Stream<String> createStrStream(String... strings) {
    return Stream.of(strings);
  }

  @Override
  public Stream<String> toUpperCase(String... strings) {
    return Stream.of(strings).map(String::toUpperCase);
  }

  @Override
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    return stringStream.filter(str -> !str.contains(pattern));
  }

  @Override
  public IntStream createIntStream(int[] arr) {
    return IntStream.of(arr);
  }

  @Override
  public <E> List<E> toList(Stream<E> stream) {
    return stream.collect(Collectors.toList());
  }

  @Override
  public List<Integer> toList(IntStream intStream) {
    return intStream.boxed().collect(Collectors.toList());
  }

  @Override
  public IntStream createIntStream(int start, int end) {
    return IntStream.range(start, end);
  }

  @Override
  public DoubleStream squareRootIntStream(IntStream intStream) {
    return intStream.mapToDouble(Math::sqrt);
  }

  @Override
  public IntStream getOdd(IntStream intStream) {
    return intStream.filter(integer -> integer % 2 != 0);
  }

  @Override
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    return message -> System.out.println(prefix + message + suffix);
  }

  @Override
  public void printMessages(String[] messages, Consumer<String> printer) {
    Arrays.stream(messages).forEach(val -> printer.accept(val));
  }

  @Override
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    intStream.filter(integer -> integer % 2 != 0).forEach(val -> printer.accept(Integer.toString(val)));
  }

  @Override
  public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
    return ints.flatMap(List::stream).map(val -> val*val);
  }

  public static void main (String [] args) {
    LambdaStreamExcImp lambdaStreamExcImp = new LambdaStreamExcImp();
    String [] arr = {"one", "two", "three", "four"};

    //Create a String Stream
    Stream<String> stringStream = lambdaStreamExcImp.createStrStream(arr);
    stringStream.forEach(val -> System.out.println(val));

    //Convert the array to uppercase
    stringStream = lambdaStreamExcImp.toUpperCase(arr);
    stringStream.forEach(val -> System.out.println(val));

    //Filter
    stringStream = lambdaStreamExcImp.filter(lambdaStreamExcImp.createStrStream(arr), "e");
    stringStream.forEach(val -> System.out.println(val));

    //Create Int Stream
    int [] intArr = {1, 2, 3, 4, 5};
    IntStream intStream = lambdaStreamExcImp.createIntStream(intArr);
    intStream.forEach(val -> System.out.println(val));

    //Convert a stream to list
    List<String> list = lambdaStreamExcImp.toList(lambdaStreamExcImp.createStrStream(arr));
    System.out.println(list);

    //Convert a int stream to list
    List<Integer> intList = lambdaStreamExcImp.toList(lambdaStreamExcImp.createIntStream(intArr));
    System.out.println(intList);

    //Int stream using given range
    IntStream intSt = lambdaStreamExcImp.createIntStream(1, 10);
    intSt.forEach(val -> System.out.println(val));

    //Square root int stream
    DoubleStream doubleStream = lambdaStreamExcImp.squareRootIntStream(lambdaStreamExcImp.createIntStream(intArr));
    doubleStream.forEach(val -> System.out.println(val));

    //Get Odd values
    intSt = lambdaStreamExcImp.getOdd(lambdaStreamExcImp.createIntStream(intArr));
    intSt.forEach(val -> System.out.println(val));

    //Lambda Printer
    Consumer<String> printer = lambdaStreamExcImp.getLambdaPrinter("start ", " end");
    printer.accept("message");

    //Print Messages
    String msg[] = {"Message one", "Message two", "Final message"};
    lambdaStreamExcImp.printMessages(msg, printer);

    //Print Odd values
    printer = lambdaStreamExcImp.getLambdaPrinter("Number: ", " is odd!");
    lambdaStreamExcImp.printOdd(lambdaStreamExcImp.createIntStream(intArr), printer);

    //Flat Nested Int
    Stream<Integer> ints = lambdaStreamExcImp.flatNestedInt(Stream.of(intList));
    ints.forEach(val -> System.out.println(val));
  }
}
