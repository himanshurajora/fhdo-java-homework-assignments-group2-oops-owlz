package application.tests;

import application.modules.Book;

public class BookTests {
    private static int run=0, pass=0; private static java.util.List<String> fails=new java.util.ArrayList<>();

    public static void run(){
        testCreateOk();
        testCreateNoTitle();
        testCreateNoAuthor();
        testToString();
        testWeightDefault();
        System.out.println("BookTests: "+pass+"/"+run+" passed");
        for(String f:fails) System.out.println("FAIL: "+f);
    }
    private static void ok(boolean c,String m){run++; if(c)pass++; else fails.add(m);}    

    private static void testCreateOk(){ Book b=new Book("B","A"); ok(b.getTitle().equals("B"), "ctor ok"); }
    private static void testCreateNoTitle(){ try{ new Book(null,"A"); ok(false,"no title should throw"); } catch(IllegalArgumentException e){ ok(true,""); } }
    private static void testCreateNoAuthor(){ try{ new Book("B",null); ok(false,"no author should throw"); } catch(IllegalArgumentException e){ ok(true,""); } }
    private static void testToString(){ Book b=new Book("B","A"); ok(b.toString().contains("B"), "toString contains title"); }
    private static void testWeightDefault(){ Book b=new Book("B","A"); ok(b.getWeightKg()==0, "default weight 0"); }
}


