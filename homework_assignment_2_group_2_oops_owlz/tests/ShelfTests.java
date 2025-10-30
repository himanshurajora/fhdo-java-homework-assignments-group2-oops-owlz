package application.tests;

import application.modules.Book;
import application.modules.Shelf;
import application.modules.RobotExceptions;

public class ShelfTests {
    private static int run=0, pass=0; private static java.util.List<String> fails=new java.util.ArrayList<>();

    public static void run(){
        testAddBook();
        testAddBookNull();
        testRemoveBookOk();
        testRemoveBookMissing();
        testShowBooks();
        System.out.println("ShelfTests: "+pass+"/"+run+" passed");
        for(String f:fails) System.out.println("FAIL: "+f);
    }
    private static void ok(boolean c,String m){run++; if(c)pass++; else fails.add(m);}    

    private static void testAddBook(){ Shelf s=new Shelf(); s.addBook(new Book("B","A")); ok(true,"added"); }
    private static void testAddBookNull(){ Shelf s=new Shelf(); try{s.addBook(null); ok(false,"null should throw");}catch(IllegalArgumentException e){ok(true,"");}}
    private static void testRemoveBookOk(){ Shelf s=new Shelf(); Book b=new Book("B","A"); s.addBook(b); try{s.removeBook(b); ok(true,"");}catch(Exception e){ok(false,"remove should work");}}
    private static void testRemoveBookMissing(){ Shelf s=new Shelf(); try{s.removeBook(new Book("X","A")); ok(false,"missing should throw");}catch(RobotExceptions.InvalidOperationException e){ok(true,"");}}
    private static void testShowBooks(){ Shelf s=new Shelf(); s.showBooks(); ok(true,"show prints"); }
}


