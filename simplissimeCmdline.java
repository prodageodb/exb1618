import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.lang.reflect.Array;

public class simplissimeCmdline {

	private static TxnScript txn = null ;
	private static String saut_de_ligne = "\n" ;

    public static void main(String[] args)
	{
		
        try {
            Options opt = new Options();

            opt.addOption("h", false, "Aide de cette application en mode ligne de commande");
            opt.addOption("c", false, "[C]rud : ajouter une ville avec le nom et le code postal (nom, code postal)");
            opt.addOption("r", false, "c[R]ud : retrouver une ville en fonction d'un parametre (id, nom, code postal)");
            opt.addOption("u", false, "cr[U]d : mettre a jour une ville avec l'id, le nom et le code postal > simplissimeCmdline -u -id 21 -nom MARRAKECH -cp 40160");
            opt.addOption("d", false, "cru[D] : effacer une ville en fonction du parametre fourni (id)");
            opt.addOption("l", false, "lister tous les enregistrements de la table villes");
            opt.addOption("v", false, "vérifier les acces a la base (pilote, ...)");
            opt.addOption("z", false, "ecraser la base");
            opt.addOption("i", false, "initialiser la base");
            opt.addOption("p", false, "peupler la base");
            opt.addOption("nom", true, "nom de la ville" );
            opt.addOption("cp", true, "code postal de la ville" );
            opt.addOption("id", true, "identifiant de la ville" );
            opt.addOption("base", true, "nom de la base (si absent, la base est par défaut ANNUAIRE" );
			
            DefaultParser parser = new DefaultParser();
            CommandLine cl = parser.parse(opt, args);

			// afficher une aide qui documente chaque option
			// java -cp ".\postgresql-42.2.5.jar;commons-cli-1.4.jar;." simplissimeCmdline -h
            if ( cl.hasOption('h') ) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("Aide de simplissime", opt);
            }
		
	    String display = "" ;
	    // Initialisation des bases
            if ( cl.hasOption("base") )
	    {
			String sBase = cl.getOptionValue("base") ;
			txn = TxnScript.getTxnScript(sBase) ;
	    }
	    else
	    {
			txn = TxnScript.getTxnScript() ;
	    }

		
	    // Traitement des options de la ligne de commandes

            if ( cl.hasOption('c') )
			{
				// TO DO
			}

            if ( cl.hasOption('r') )
			{
				// TO DO
			}

		
			// java -cp ".\postgresql-42.2.5.jar;commons-cli-1.4.jar;." simplissimeCmdline -u -id 178 -nom MER -cp 41500
            if ( cl.hasOption('u') )
			{
					System.out.println( "\n\nMettre a jour encore le code postal dans la base\n" ) ;
					String sId = cl.getOptionValue("id") ;
					Integer id = Integer.parseInt(sId);
					String nom = cl.getOptionValue("nom") ;
					String sCp = cl.getOptionValue("cp") ;
					Integer cp = Integer.parseInt(sCp);
					display = txn.updateVille ( id, nom, cp ) ;
					System.out.println( display ) ;
            }
			
            if ( cl.hasOption('d') )
			{
				// TO DO
			}
			

			// java -cp ".\postgresql-42.2.5.jar;commons-cli-1.4.jar;." simplissimeCmdline -e
            if ( cl.hasOption('z') )
			{
					boolean the_result ;
					System.out.println( "\n\nEcraser tables de la base : villes, ... \n" ) ;
					the_result = TxnScript.eraseDb () ;
            }

			if ( cl.hasOption('i') )
			{
			    txn.initDb() ;
			}

			if ( cl.hasOption('p') )
			{
			    txn.populateDb() ;
			}
			
			
            if ( cl.hasOption('v') )
			{
					System.out.println( "\n\nVerifier acces a la base \n" ) ;
					
					boolean bCheck = txn.check () ; 
					// System.out.println( display ) ;
            }


			
			// java -cp ".\postgresql-42.2.5.jar;commons-cli-1.4.jar;." simplissimeCmdline -l
            if ( cl.hasOption('l') )
			{
					System.out.println( "\n\nEtat initial de la base\n" ) ;
					
					TxnScript.VillesIterator myVilleIt = txn.getIterator4listerVilles() ;
					int myId ;
					int myCurrentCodePostal ;
					String myCurrentNom ;

					display = "" ;		
					while ( myVilleIt.hasNext() )
					{
						myId = myVilleIt.nextIdVille() ;
						myCurrentCodePostal = myVilleIt.nextCodePostalVille ( ) ;		
						myCurrentNom = myVilleIt.nextNomVille ( ) ;
						
						display = display + "/" + myId ;
						display = display + "/" + myCurrentCodePostal ;
						display = display + "/" + myCurrentNom ;
						display = display + saut_de_ligne ;						

					}
					System.out.println( display ) ;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
	}
}
