public class Main {
  int x = 5;
​
  public static void main(String[] args) {
    Main myObj1 = new Main();
    Main myObj2 = new Main();
    myObj2.x = 25;
    System.out.println(myObj1.x);
    System.out.println(myObj2.x);
  }
}
