package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployeeSort {

  public static void main(String[] args) {
    //Comparable vs. Comparator
    //Using Comparable to sort by salary and age in ascending order
    Employee one = new Employee(1, "Alice", 25, 60000);
    Employee two = new Employee(2, "Bob", 30, 50000);
    Employee three = new Employee(3, "John", 27, 40000);
    Employee four = new Employee(4, "Jacob", 40, 75000);

    List<Employee> emp = Arrays.asList(one, two, three, four);
    Collections.sort(emp);

    for(Employee e: emp) {
      System.out.println(e.getName());
    }

    //Using Comparator to sort by age
    Comparator<Employee> ageSort = new Comparator<Employee>() {
      @Override
      public int compare(Employee o1, Employee o2) {
        return Double.compare(o1.getAge(), o2.getAge());
      }
    };
    emp.sort(ageSort);

    for(Employee e: emp) {
      System.out.println(e.getName());
    }
  }
}