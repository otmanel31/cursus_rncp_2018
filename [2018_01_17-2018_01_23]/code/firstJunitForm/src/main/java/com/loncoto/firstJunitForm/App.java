package com.loncoto.firstJunitForm;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Calculatrice c = new Calculatrice();
        System.out.println(c.addition(10,  15));
        System.out.println(c.division(10,  4));
        //System.out.println(c.division(10,  0));
        System.out.println(1.0 / 2.0);
        /*System.out.println(4.0 / 10.0);
        double d = 0.1;
        d = d * 10;
        d = d / 10;
        d = d + d + d;
        System.out.println(d);
        d = 0.0625;
        d = d * 10;
        d = d / 10;
        d = d + d + d;
        System.out.println(d);*/
        
        //BigDecimal bd = new BigDecimal(1000);
        // calculs
        TextUtils tu = new TextUtils();
        System.out.println(tu.capitalise("vincent courtalon"));
        System.out.println(tu.inverse("bonjour"));
        
        Scanner input = new Scanner(System.in);
        System.out.println("chaine a capitaliser? ");
        String chaine = input.nextLine();
        System.out.println(tu.capitalise(chaine));
        System.out.println("--------------------");
        chaine = null;
        System.out.println(tu.capitalise(chaine));
        
    }
}
