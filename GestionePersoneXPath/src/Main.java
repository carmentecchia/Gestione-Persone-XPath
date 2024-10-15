import org.w3c.dom.Document;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionePersoneXPath gestionePersone = new GestionePersoneXPath();
        GestionePersoneXPath parser = new GestionePersoneXPath();
        Scanner scanner = new Scanner(System.in);
        String xsltFilePath = "src/trasformazione.xsl";
        String outputHtmlPath = "src/output.html";
        String xsdFilePath = "src/control.xsd";
        String filePath = "src/persone.xml";
        Document doc = parser.parseXMLFile(filePath);


        if (doc == null) {
            System.out.println("Errore durante il caricamento del file XML.");
            return;
        }

        while (true) {
            System.out.println("\nScegli un'operazione:");
            System.out.println("1 - Visualizza le persone");
            System.out.println("2 - Aggiungi una persona");
            System.out.println("3 - Elimina una persona con id");
            System.out.println("4 - Cerca una persona con id");
            System.out.println("5 - Controlla versione XSD");
            System.out.println("6 - Trasformazione XSL");
            System.out.println("7 - Salva e esci");

            System.out.print("Comando: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    parser.stampaPersone(doc);
                    break;

                case "2":
                    parser.aggiungiPersonaInterattivo(doc, scanner);
                    break;

                case "3":
                    System.out.print("Inserisci l'ID della persona da eliminare: ");
                    String idDaEliminare = scanner.nextLine();

                    try {
                        boolean personaEliminata = parser.eliminaPersona(doc, idDaEliminare);
                        if (personaEliminata) {
                            System.out.println("Persona con ID " + idDaEliminare + " rimossa.");
                        } else {
                            System.out.println("Persona con ID " + idDaEliminare + " non trovata.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "4":
                    System.out.print("Inserisci l'ID della persona da cercare: ");
                    String idDaCercare = scanner.nextLine();

                    try {
                        String personaTrovata = parser.cercaPersona(doc, idDaCercare);
                        if (personaTrovata != null) {
                            System.out.println("Persona trovata:\n" + personaTrovata);
                        } else {
                            System.out.println("Persona con ID " + idDaCercare + " non trovata.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "5":
                    if (gestionePersone.validaXMLConXSD(filePath, xsdFilePath)) {
                        System.out.println("XML valido.");
                    } else {
                        System.out.println("XML non valido.");
                    }
                    break;

                case "6":
                    gestionePersone.applicaXSLT(filePath, xsltFilePath, outputHtmlPath);
                    break;

                case "7":
                    parser.salvaDocumentoXML(doc, filePath);
                    System.out.println("Modifiche salvate. Uscita.");
                    return;

                default:
                    System.out.println("Comando non riconosciuto. Riprova.");
            }
        }
    }
}