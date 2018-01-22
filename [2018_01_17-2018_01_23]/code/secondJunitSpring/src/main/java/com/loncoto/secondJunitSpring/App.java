package com.loncoto.secondJunitSpring;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.jta.SpringJtaSynchronizationAdapter;

import com.loncoto.secondJunitSpring.metier.Gazouille;
import com.loncoto.secondJunitSpring.services.GazouilleService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// chargement du contexte spring
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        Scanner input = new Scanner(System.in);
        System.out.println("-------------------------------------");
        
        GazouilleService gs = ctx.getBean(GazouilleService.class);
        
        List<Gazouille> gazouilles = gs.readAllGazouille();
        for (Gazouille g : gazouilles)
        	System.out.println(g);
        
        System.out.println("titre nouvelle gazouille ?");
        String titre  = input.nextLine();
        System.out.println("corps nouvelle gazouille ?");
        String corps = input.nextLine();
        
        gs.publish(new Gazouille(0, titre, corps));
        
        
    }
}
