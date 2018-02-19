package com.loncoto.helloSpark;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class App 
{
    public static void main( String[] args )
    {
    	SparkConf conf = new SparkConf()
    						.setAppName("wordCountSpark")
    						.setMaster("local[*]");
    	// local indique que le noeud spark "maitre" est sur localhostn on peut potentiellement
    	// soumettre un job dans un autre cluster spark
    	// [*] -> indiquer le parallelisme a utiliser, ici, on le laisse faire
    	
    	
    	// contexte d'execution spark
    	JavaSparkContext sc = new JavaSparkContext(conf);
    	
    	/*
    	 * le fonctionnement de spark est centré autour des RDD, resilient distributed dataset
    	 * un des autres concepts fondamentaux de park (coté code), est qu'il fonctionne
    	 * en mode lazy evaluation : evaluation paresseuse
    	 * on va construire un "graphe" d'exectution via des transformation des RDD
    	 * et celui-ci sera effectivement executé lors de la demande de sortie du résultat
    	 * 
    	 */
    	
    	// permiere etape, lires les données depuis une entrée, ici un livre dans hdfs
    	JavaRDD<String> lines = sc.textFile("/user/formation/livre1/miserable.txt");
    
    	// si je veut compter les mots, que vais avoir en sortie
    	// map String -> Integer --> Tuple2 (couple de valeur)
    	
    	JavaPairRDD<String, Integer> resultat =
    			// split genere une tableau, donc on a une ensemble de tableau a la suite des uns de autres
    			// flatmap lit le contenu de ces tableau et le ressort dans un seul tableau flux "ecrasé"
    			lines.flatMap(l -> Arrays.asList(l.split("[- .,;!?'\"$!#]+")))
    			// chaque mot est transformé en couple "mot" + compteau a 1
    				.mapToPair(mot -> new Tuple2<String, Integer>(mot, 1))
    			// regrouper ensemble tous les tuples qui on la meme clé (le mot)
    			// et appliquer la fonction passer en argument consecutivement sur toutes le valeurs
    				.reduceByKey(( a, b) -> a + b);
    																		  
    	// sauvegarder la sortie dans un fichier dans hdfs
    	// c'est cela qui declenchera reelement l'execution
    	resultat.saveAsTextFile("/user/formation/wordcount1");
    	
    }
}
