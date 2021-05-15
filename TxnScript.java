// squelette du TxnScript de EXB1618

import java.util.Date;


// types retournés par les opérations JDBC

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.CallableStatement ; 
import java.sql.Connection ; 
import java.sql.DriverManager ; 
import java.sql.Statement ; 
import java.sql.ResultSet ; 
import java.sql.PreparedStatement ;
import java.sql.CallableStatement ;
import java.sql.SQLException ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class TxnScript
{

	// Variable générique
	private static String jdbcUrl = "" ;
	private static String jdbcMachine = "" ;
	private static String jdbcDefaultDatabase = "ANNUAIRE" ;
	private static String jdbcUser = "" ;
	private static String jdbcPass = "" ;
	
	// exemple MYSQL LOCAL
	private static String jdbcMysqlMachine = "localhost" ;
	private static String jdbcMysqlUser = "root" ;
	private static String jdbcMysqlPass = "" ; // In MySQL, by default, the username is root and there’s no password.
	private static String jdbcMysqlIntricacies = "zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC" ;

	private static Connection theCnx = null ;

	// le constructeur de TxnScript établit la connexion
        private TxnScript (String jdbcDatabase)
	{
		boolean dbAbsent = true ;

		// si MySQL
		jdbcUrl = "" ;
		jdbcMachine = jdbcMysqlMachine  ;
		jdbcUser = jdbcMysqlUser ;
		jdbcPass = jdbcMysqlPass ;

		// Création de la base de données, si elle n'existe password
		// Une connection dédiée est utilisée
		try
		{		
			jdbcUrl = "jdbc:mysql://" + jdbcUser + ":" + jdbcPass + "@" + jdbcMachine + ":3306";
			theCnx = DriverManager.getConnection( jdbcUrl ) ;
			Statement theStmt = theCnx.createStatement();
		
			// vérifier si la base existe déjà
			ResultSet resultSet = theCnx.getMetaData().getCatalogs();

			//iterate each catalog in the ResultSet
			while (resultSet.next())
			{
				// Get the database name, which is at position 1
				String databaseName = resultSet.getString(1).toUpperCase() ;
				if ( databaseName.equals ( jdbcDatabase ) )
				{
					dbAbsent = false ;
				}
			}
			resultSet.close();
		
			if ( dbAbsent == true )
			{
				String sqlCreateDb = "CREATE DATABASE " + jdbcDatabase ;
				theStmt.execute( sqlCreateDb );
			}
			// theCnx.commit(); // now the database physically exists. If auto-commit activated => fails 
			theCnx.close();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		// Ouverture d'une connection sur la base, pour les futurs requêtes appelées via les appels aux fonctions de tnxscript.
		try
		{		
			jdbcUrl = "jdbc:mysql://" + jdbcUser + ":" + jdbcPass + "@" + jdbcMachine + ":3306" + "/" + jdbcDatabase ;
			theCnx = DriverManager.getConnection( jdbcUrl ) ;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
    }

    // permet aux classes clientes d'obtenir une référence vers un TxnScript pour appeler toutes les fonctions
    // pour mémoire, le constructeur de TxnScript est privé => les fonctions des classes clientes ne peuvent le construire.
    public static TxnScript getTxnScript(String jdbcDatabase) {
        return new TxnScript( jdbcDatabase );
    }

    public static TxnScript getTxnScript() {
        return getTxnScript ( jdbcDefaultDatabase ) ;
    }


    // cette fonction doit initialiser la structure (DDL)
    public static boolean initDb()	
    {
		// TO DO
	    boolean the_result = true ;
		String the_sql = "" ;

		try
		{
			PreparedStatement pstmt = theCnx.prepareStatement(the_sql) ;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}

	    return the_result ;
    }    



    // cette fonction doit charger quelques enregistrements dans la base pour pouvoir faire les tests
    public static boolean populateDb()	
    {
		// TO DO
	    boolean the_result = true ;
		String the_sql = "" ;
		
		try
		{
			PreparedStatement pstmt = theCnx.prepareStatement(the_sql) ;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    return the_result ;
    }    





	
    // cette fonction doit retirer toute la structure de la base (effacer les contraintes, les tables avec leur champ)
    // à la sortie, la base ne doit contenir aucune table
    public static  boolean eraseDb()	
    {
		// TO DO
	    boolean the_result = true ;
		String the_sql = "" ;
		
		try
		{
			PreparedStatement pstmt = theCnx.prepareStatement(the_sql) ;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    return the_result ;
	}
	
    // cette fonction doit vérifier que la connection avec le SGBD est opérationnelle et vérifier que la base est correcte
	// (présence des tables, par exemple)
    public static boolean check()
    {
		// TO DO
	    boolean the_result = true ;
		String the_sql = "" ;
		
		try
		{
			PreparedStatement pstmt = theCnx.prepareStatement(the_sql) ;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    return the_result ;
    }

    /*************** ITERATEUR AVEC LA TABLE VILLES ***************/
    // C'est une mise en oeuvre (implementation) très simple (dégradée) du pattern Iterator
    // Les limitations sont les suivantes
    // pas d'interface, pas de fonction next() mais des accesseurs (getters comme nextIdVille, nextCodePostalVille, ...) pour accéder à chaque champ d'une ville.
    // On peut trouver quelques indications sur : http://mrbool.com/design-patterns-iterator-in-java/26738
    // Pour information, pour une description complète du pattern Iterator, on peut consulter
    // https://refactoring.guru/fr/design-patterns/iterator
	
	public class VillesIterator
	{
		PreparedStatement monTraitement ;
		ResultSet maListe ;
		
		private VillesIterator ( String the_sql )
		{
			try
			{
				if ( the_sql != "" )
				{
					monTraitement = theCnx.prepareStatement(the_sql) ;
					maListe = monTraitement.executeQuery();	
				}
				else
				{
					monTraitement = null ;
					maListe = null ;	
				}
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}			
		}
		
		boolean hasNext()
		{
			boolean the_result = false ;
			try
			{
				if ( maListe != null )
				{
					the_result = maListe.next();
				}
				else
				{
					the_result = false ;
				}
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}			
			

			return the_result ;
		}

		Integer nextIdVille()
		{
			Integer the_result = 0 ;

			try
			{
				the_result = maListe.getInt( "id" ) ;
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}			
			return the_result ;
		}
		
		
		String nextNomVille()
		{
			String the_result ="" ;
			try
			{
				the_result = maListe.getString( "nom" ) ;
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}			
			return the_result ;
		}

		Integer nextCodePostalVille()
		{
			Integer the_result = 0 ;
			try
			{
				the_result = maListe.getInt( "code_postal" ) ;
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}			
			return the_result ;
		}
	}
	
	
    // insére un enregistrement dans la table Ville
    // renvoie l'id
    public static Integer insertVille (String nom, Integer codePostal)
    {
		// TO DO		
	    Integer the_result = 0 ;
	    return the_result ;
    }

	
    // recherche tous les enregistrements de la table Ville dont le code postal correspondant
    // retour : un itérateur sur les enregistrements renvoyés par cette recherche
    public VillesIterator getIterator4searchByCodePostal(Integer searchedCodePostal)
    {
		// TO DO		
		String the_sql = "" ;
		VillesIterator the_it = null ;
		
		return the_it ;
    }


	// retirer (effacer) un enregistrement de la table, en fonction de l'id
	// // retour : renvoyer false si aucun enregistrement n'est trouvé avec cet id
    public boolean deleteVille(Integer id)
    {
		boolean the_result = false ;
		String the_sql = "" ;
		
		// TO DO

		return the_result ;
    }

    // recherche tous les enregistrements de la table Ville (sans aucun critère)
    // retour : un itérateur sur les enregistrements renvoyés par cette recherche
    public VillesIterator getIterator4listerVilles()
    {
		// TO DO
		String the_sql = "" ;
		VillesIterator the_it = new VillesIterator(the_sql) ;
		return the_it ;
    }


    // met à jour un enregistrement existant dans la table Ville
    // retour : une chaine avec les valeurs intégrées dans la table	
    public static String updateVille (Integer id, String nom, Integer codePostal)
    {
        String the_result = "" ;
		// TO DO
		return the_result ;
    }
	

    // clot la connection au SGBD
    // retour : true si la fermeture s'est bien déroulée, false sinon
    private boolean close()
    {
		boolean the_result = true ;
		return the_result ;
    }
}
