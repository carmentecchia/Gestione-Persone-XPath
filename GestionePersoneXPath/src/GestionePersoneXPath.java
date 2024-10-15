import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GestionePersoneXPath {
    public Document parseXMLFile(String filePath) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stampaPersone(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("persona");
        if (nodeList.getLength() == 0) {
            System.out.println("Nessuna persona trovata.");
            return;
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element personaElement = (Element) node;

                String id = personaElement.getAttribute("id");
                String nome = personaElement.getElementsByTagName("nome").item(0).getTextContent();
                String cognome = personaElement.getElementsByTagName("cognome").item(0).getTextContent();
                String eta = personaElement.getElementsByTagName("età").item(0).getTextContent();

                System.out.println("ID: " + id);
                System.out.println("Nome: " + nome);
                System.out.println("Cognome: " + cognome);
                System.out.println("Età: " + eta); // Stampa età
                System.out.println("---------------------------");
            }
        }
    }

    public void aggiungiPersonaInterattivo(Document doc, Scanner scanner) {
        System.out.print("Inserisci ID: ");
        String id = scanner.nextLine();

        System.out.print("Inserisci Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Inserisci Cognome: ");
        String cognome = scanner.nextLine();

        System.out.print("Inserisci Età: ");
        int eta = Integer.parseInt(scanner.nextLine());

        aggiungiPersona(doc, id, nome, cognome, eta);
    }

    public void aggiungiPersona(Document doc, String id, String nome, String cognome, Integer eta) {
        Element nuovaPersona = doc.createElement("persona");
        nuovaPersona.setAttribute("id", id);

        Element nomeElement = doc.createElement("nome");
        nomeElement.appendChild(doc.createTextNode(nome));
        nuovaPersona.appendChild(nomeElement);

        Element cognomeElement = doc.createElement("cognome");
        cognomeElement.appendChild(doc.createTextNode(cognome));
        nuovaPersona.appendChild(cognomeElement);

        Element etaElement = doc.createElement("età");
        etaElement.appendChild(doc.createTextNode(String.valueOf(eta)));
        nuovaPersona.appendChild(etaElement);

        doc.getDocumentElement().appendChild(nuovaPersona);

        System.out.println("Aggiunta nuova persona: " + nome + " " + cognome + ", Età: " + eta);
    }

    public boolean eliminaPersona(Document doc, String id) {
        NodeList nodeList = doc.getElementsByTagName("persona");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element personaElement = (Element) node;
                if (personaElement.getAttribute("id").equals(id)) {
                    personaElement.getParentNode().removeChild(personaElement);
                    return true;
                }
            }
        }
        return false;
    }

    public String cercaPersona(Document doc, String id) {
        NodeList nodeList = doc.getElementsByTagName("persona");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element personaElement = (Element) node;
                if (personaElement.getAttribute("id").equals(id)) {
                    String nome = personaElement.getElementsByTagName("nome").item(0).getTextContent();
                    String cognome = personaElement.getElementsByTagName("cognome").item(0).getTextContent();
                    String eta = personaElement.getElementsByTagName("età").item(0).getTextContent();
                    return "ID: " + id + "\nNome: " + nome + "\nCognome: " + cognome + "\nEtà: " + eta;
                }
            }
        }
        return null;
    }

    public boolean validaXMLConXSD(String xmlFilePath, String xsdFilePath) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdFilePath));

            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(new File(xmlFilePath)));
            System.out.println("Il file XML è valido rispetto all'XSD.");
            return true;
        } catch (SAXException e) {
            System.out.println("Errore di validazione XML: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Errore di input/output: " + e.getMessage());
        }
        return false;
    }

    public void applicaXSLT(String xmlFilePath, String xsltFilePath, String outputFilePath) {
        try {
            File xmlFile = new File(xmlFilePath);
            StreamSource xmlSource = new StreamSource(xmlFile);

            File xsltFile = new File(xsltFilePath);
            StreamSource xsltSource = new StreamSource(xsltFile);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSource);

            StreamResult result = new StreamResult(new File(outputFilePath));

            transformer.transform(xmlSource, result);

            System.out.println("Trasformazione XSLT completata. Output salvato in: " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void salvaDocumentoXML(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

            System.out.println("File XML salvato correttamente in: " + filePath);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
