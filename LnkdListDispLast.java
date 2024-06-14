import java.util.LinkedList;
​
public class Main {
  public static void main(String[] args) {
    LinkedList<String> cars = new LinkedList<String>();
    cars.add("Volvo");
    cars.add("BMW");
    cars.add("Ford");
    cars.add("Mazda");
    
    // Use getLast() to display the last item in the list
    System.out.println(cars.getLast());
  }
}
​
