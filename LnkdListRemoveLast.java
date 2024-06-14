import java.util.LinkedList;
​
public class Main {
  public static void main(String[] args) {
    LinkedList<String> cars = new LinkedList<String>();
    cars.add("Volvo");
    cars.add("BMW");
    cars.add("Ford");
    cars.add("Mazda");
    
    // Use removeLast() remove the last item from the list
    cars.removeLast();
    System.out.println(cars);
  }
}
​
