package finki.wbs.library.demo;

import finki.wbs.library.demo.model.Author;
import finki.wbs.library.demo.model.Book;
import finki.wbs.library.demo.model.Contributor;
import finki.wbs.library.demo.model.exceptions.InvalidAuthorNameException;
import finki.wbs.library.demo.model.exceptions.InvalidBookNameException;
import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.SystemUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
    public static List<Book> books = new ArrayList<Book>();
    public static List<Author> authors = new ArrayList<Author>();
    public static List<Contributor> contributors = new ArrayList<Contributor>();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        // m.write(System.out,"TTL");

        Property a = m.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");//for books or person
        String shemaBook = "http://schema.org/Book";
        String shemaPerson = "http://schema.org/Person";
        String shemaOrg = "http://schema.org/Organization";
        String crShema = "http://www.bl.uk/schemas/bibliographic/blterms#hasCreated";
        String conShema = "http://www.bl.uk/schemas/bibliographic/blterms#hasContributedTo";
        StmtIterator iter = m.listStatements(new SimpleSelector(null, a, (String) null));

        if (iter.hasNext()) {

            while (iter.hasNext()) {

                Statement stmt = iter.nextStatement();
                RDFNode object = stmt.getObject();
                Resource m2 = m.getResource(shemaBook);
                Resource m1 = m.getResource(shemaPerson);
                Resource m3 = m.getResource(shemaOrg);

                if (object.equals(m1) || object.equals(m3)) {
                    String type = "";

                    Resource subject = stmt.getSubject();
                    // System.out.println(subject);
                    Property hasCreated = m.getProperty(crShema);
                    Property hasContributedTo = m.getProperty(conShema);
                    if (subject.hasProperty(hasCreated)) {
                        if (object.equals(m1)) {
                            type = "Person";
                        }
                        if (object.equals(m3)) {
                            type = "Organization";
                        }
                        //System.out.println(subject);

                        Statement name = subject.getProperty(m.getProperty("http://schema.org/name"));
                        RDFNode nameNode = name.getObject();
                        //System.out.println(subjectString);

                        NodeIterator iterCreated = m.listObjectsOfProperty(subject, m.getProperty(crShema));
                        List<String> objHasCreated = new ArrayList<>();
                        while (iterCreated.hasNext()) {
                            RDFNode node = iterCreated.next();
                            String queryString = "" +
                                    "PREFIX schema: <http://schema.org/>\n" +
                                    "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                                    "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                                    "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                                    "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                                    "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                    "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                                    "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                                    "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                                    "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                                    "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                    "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                                    "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                                    "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                                    "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                                    "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                                    "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                                    "SELECT ?name " +
                                    "WHERE { " +
                                    "<" + node.toString() + ">" + " rdfs:label ?name" +
                                    "}";
                            Query query = QueryFactory.create(queryString);
                            QueryExecution qexec = QueryExecutionFactory.create(query, m);
                            try {
                                ResultSet results = qexec.execSelect();
                                while (results.hasNext()) {
                                    QuerySolution soln = results.nextSolution();
                                    String nameCon = soln.get("name").toString();
                                    objHasCreated.add(nameCon);
                                    // System.out.println(soln.get("name"));
                                    //  System.out.println(nodes);
                                }
                            } finally {
                                qexec.close();
                            }

                        }


                        //  System.out.println(objHasCreated);
                        Author author = new Author(nameNode.toString(), objHasCreated, type);
                        authors.add(author);


                    }
                    if (subject.hasProperty(hasContributedTo)) {
                        if (object.equals(m1)) {
                            type = "Person";
                        }
                        if (object.equals(m3)) {
                            type = "Organization";
                        }
                        //System.out.println(subject);
                        Statement name = subject.getProperty(m.getProperty("http://schema.org/name"));
                        RDFNode nameNode = name.getObject();

                        // System.out.println(subjectString);

                        NodeIterator iterCon = m.listObjectsOfProperty(subject, m.getProperty(conShema));
                        List<String> objHasCon = new ArrayList<>();
                        while (iterCon.hasNext()) {
                            RDFNode node = iterCon.next();
                            String queryString = "" +
                                    "PREFIX schema: <http://schema.org/>\n" +
                                    "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                                    "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                                    "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                                    "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                                    "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                    "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                                    "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                                    "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                                    "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                                    "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                    "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                                    "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                                    "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                                    "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                                    "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                                    "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                                    "SELECT ?name " +
                                    "WHERE { " +
                                    "<" + node.toString() + ">" + " rdfs:label ?name" +
                                    "}";
                            Query query = QueryFactory.create(queryString);
                            QueryExecution qexec = QueryExecutionFactory.create(query, m);
                            try {
                                ResultSet results = qexec.execSelect();
                                while (results.hasNext()) {
                                    QuerySolution soln = results.nextSolution();
                                    String nameCon = soln.get("name").toString();
                                    objHasCon.add(nameCon);
                                    // System.out.println(soln.get("name"));
                                    //  System.out.println(nodes);
                                }
                            } finally {
                                qexec.close();
                            }

                        }


                        // System.out.println(objHasCon);
                        Contributor contributor = new Contributor(nameNode.toString(), objHasCon, type);
                        contributors.add(contributor);


                    }


                }


                if (object.equals(m2)) {
                    Resource subject = stmt.getSubject();
                    //System.out.println(subject);
                    Statement name = subject.getProperty(m.getProperty("http://www.w3.org/2000/01/rdf-schema#label"));
                    RDFNode nameNode = name.getObject();
                    Statement title = subject.getProperty(m.getProperty("http://purl.org/dc/terms/title"));
                    RDFNode titleNode = title.getObject();


                    NodeIterator iterator = m.listObjectsOfProperty(subject, m.getProperty("http://purl.org/dc/terms/contributor"));
                    List<String> contributorNames = new ArrayList<>();
                    while (iterator.hasNext()) {
                        RDFNode node = iterator.next();
                        //System.out.println(node);


                        String queryString = "" +
                                "PREFIX schema: <http://schema.org/>\n" +
                                "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                                "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                                "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                                "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                                "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                                "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                                "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                                "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                                "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                                "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                                "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                                "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                                "SELECT ?name " +
                                "WHERE { " +
                                "<" + node.toString() + ">" + " schema:name ?name" +
                                "}";
                        Query query = QueryFactory.create(queryString);
                        QueryExecution qexec = QueryExecutionFactory.create(query, m);
                        try {
                            ResultSet results = qexec.execSelect();
                            while (results.hasNext()) {
                                QuerySolution soln = results.nextSolution();
                                String nameCon = soln.get("name").toString();
                                contributorNames.add(nameCon);
                                // System.out.println(soln.get("name"));
                                //  System.out.println(nodes);
                            }
                        } finally {
                            qexec.close();
                        }


                    }


                    NodeIterator iterCreator = m.listObjectsOfProperty(subject, m.getProperty("http://purl.org/dc/terms/creator"));
                    List<String> creatorNames = new ArrayList<>();
                    while (iterCreator.hasNext()) {
                        RDFNode node = iterCreator.next();
                        String queryString = "" +
                                "PREFIX schema: <http://schema.org/>\n" +
                                "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                                "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                                "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                                "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                                "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                                "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                                "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                                "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                                "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                                "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                                "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                                "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                                "SELECT ?name " +
                                "WHERE { " +
                                "<" + node.toString() + ">" + " schema:name ?name" +
                                "}";
                        Query query = QueryFactory.create(queryString);
                        QueryExecution qexec = QueryExecutionFactory.create(query, m);
                        try {
                            ResultSet results = qexec.execSelect();
                            while (results.hasNext()) {
                                QuerySolution soln = results.nextSolution();
                                String nameCr = soln.get("name").toString();
                                creatorNames.add(nameCr);
                                //System.out.println(soln.get("name"));
                                //  System.out.println(nodes);
                            }
                        } finally {
                            qexec.close();
                        }

                    }


                    Statement isbn = subject.getProperty(m.getProperty("http://schema.org/isbn"));
                    RDFNode isbnNode = isbn.getObject();
                    Statement datePublished = subject.getProperty(m.getProperty("http://schema.org/datePublished"));
                    RDFNode dateNode = datePublished.getObject();
                    Statement bnb = subject.getProperty(m.getProperty("http://www.bl.uk/schemas/bibliographic/blterms#bnb"));
                    RDFNode bnbNode = bnb.getObject();
                    // System.out.println(nameNode+" "+titleNode+" "+contributors+" "+creators+" "+isbnNode+" "+dateNode+" "+bnbNode);
                    Book book = new Book(nameNode.toString(), titleNode.toString(), creatorNames, contributorNames, isbnNode.toString(), bnbNode.toString(), dateNode.toString(),100);
                    books.add(book);

                }


            }
        }



    }

    public static List<Book> searchByBookName(String param){
        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        List<Book> result=new ArrayList<Book>();

        String queryString = "" +
                "PREFIX schema: <http://schema.org/>\n" +
                "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                "SELECT ?name   " +
                "WHERE { " +
                "?resource rdfs:label ?name ." +
                "filter (contains(?name, \""+param+"\"))" +
                "}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, m);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.get("name").toString();
                Book book=books.stream().filter(b->b.getName().equals("name")).findFirst().orElseThrow(InvalidBookNameException::new);
                    result.add(book);



            }
        } finally {
            qexec.close();
        }

        return result;
    }

    public static List<Book> orderByLatest(){
        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        List<Book> result=new ArrayList<Book>();

        String queryString = "" +
                "PREFIX schema: <http://schema.org/>\n" +
                "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                "SELECT ?name ?datePublished   " +
                "WHERE { " +
                "?resource rdfs:label ?name ." +
                "?resource schema:datePublished ?datePublished ." +
                "}"+
                "order by desc(?datePublished)";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, m);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.get("name").toString();
                Book book=books.stream().filter(b->b.getName().equals(name)).findFirst().orElseThrow(InvalidBookNameException::new);
                result.add(book);


            }
        } finally {
            qexec.close();
        }

        return result;
    }
    public static List<Book> orderByOldest(){
        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        List<Book> result=new ArrayList<Book>();

        String queryString = "" +
                "PREFIX schema: <http://schema.org/>\n" +
                "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                "SELECT ?name ?datePublished   " +
                "WHERE { " +
                "?resource rdfs:label ?name ." +
                "?resource schema:datePublished ?datePublished ." +
                "}"+
                "order by asc(?datePublished)";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, m);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.get("name").toString();
                Book book=books.stream().filter(b->b.getName().equals(name)).findFirst().orElseThrow(InvalidBookNameException::new);
                result.add(book);


            }
        } finally {
            qexec.close();
        }

        return result;
    }

    public static List<Book> searchByAuthorName(String param){
        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        String queryString="";

        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        List<Book> result=new ArrayList<Book>();
        Author author= authors.stream().filter(a->a.getName().contains(param)).findFirst().orElseThrow(InvalidAuthorNameException::new);
        List<String> hasCreated=author.getHasCreated();
        for (String created: hasCreated) {


            queryString = "" +
                    "PREFIX schema: <http://schema.org/>\n" +
                    "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                    "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                    "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                    "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                    "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                    "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                    "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                    "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                    "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                    "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                    "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                    "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                    "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                    "SELECT ?name ?datePublished   " +
                    "WHERE { " +
                    "?resource rdfs:label ?name ." +
                    "?resource schema:datePublished ?datePublished ." +
                    "filter contains(?name,\"" + created + "\")" +
                    "}";

            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, m);
            try {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    String name = soln.get("name").toString();
                    String date = soln.get("datePublished").toString();
                    System.out.println(name + " " + date);
                    Book book = books.stream().filter(b -> b.getName().equals(name)).findFirst().orElseThrow(InvalidBookNameException::new);
                    result.add(book);


                }
            } finally {
                qexec.close();
            }

        }

        return result;
    }
    public static List<Book> searchByContributorName(String param){
        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        String queryString="";

        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        List<Book> result=new ArrayList<Book>();
        Contributor contributor= contributors.stream().filter(a->a.getName().contains(param)).findFirst().orElseThrow(InvalidAuthorNameException::new);
        List<String> hasContributed=contributor.getHasContributed();
        for (String con: hasContributed) {


            queryString = "" +
                    "PREFIX schema: <http://schema.org/>\n" +
                    "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                    "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                    "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                    "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                    "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                    "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                    "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                    "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                    "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                    "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                    "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                    "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                    "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                    "SELECT ?name ?datePublished   " +
                    "WHERE { " +
                    "?resource rdfs:label ?name ." +
                    "?resource schema:datePublished ?datePublished ." +
                    "filter contains(?name,\"" + con + "\")" +
                    "}";

            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, m);
            try {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    String name = soln.get("name").toString();
                    String date = soln.get("datePublished").toString();
                    System.out.println(name + " " + date);
                    Book book = books.stream().filter(b -> b.getName().equals(name)).findFirst().orElseThrow(InvalidBookNameException::new);
                    result.add(book);


                }
            } finally {
                qexec.close();
            }

        }

        return result;
    }

    public static List<Book> searchByYear(String year){
        String file = "C:\\Users\\arife\\Desktop\\books.ttl";
        Model m = ModelFactory.createDefaultModel();
        String queryString="";

        InputStream in = FileManager.get().open(file);
        if (in == null) {
            throw new IllegalArgumentException("File " + in + " is not found");
        }
        m.read(in, "", "TTL");
        List<Book> result=new ArrayList<Book>();




            queryString = "" +
                    "PREFIX schema: <http://schema.org/>\n" +
                    "PREFIX org:   <http://www.w3.org/ns/org#>\n" +
                    "PREFIX owl:   <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX skos:  <http://www.w3.org/2004/02/skos/core#>\n" +
                    "PREFIX bio:   <http://purl.org/vocab/bio/0.1/>\n" +
                    "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX blt:   <http://www.bl.uk/schemas/bibliographic/blterms#>\n" +
                    "PREFIX geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                    "PREFIX rdau:  <http://rdaregistry.info/Elements/u/>\n" +
                    "PREFIX dct:   <http://purl.org/dc/terms/>\n" +
                    "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX bibo:  <http://purl.org/ontology/bibo/>\n" +
                    "PREFIX interval: <http://reference.data.gov.uk/def/intervals/>\n" +
                    "PREFIX time:  <http://www.w3.org/2006/time#>\n" +
                    "PREFIX event: <http://purl.org/NET/c4dm/event.owl#>\n" +
                    "PREFIX isbd:  <http://iflastandards.info/ns/isbd/elements/>\n" +
                    "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +


                    "SELECT ?name ?datePublished   " +
                    "WHERE { " +
                    "?resource rdfs:label ?name ." +
                    "?resource schema:datePublished ?datePublished ." +
                    "filter contains(?datePublished,\"" + year + "\")" +
                    "}";

            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, m);
            try {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    String name = soln.get("name").toString();
                    String date = soln.get("datePublished").toString();
                    System.out.println(name + " " + date);
                    Book book = books.stream().filter(b -> b.getName().equals(name)).findFirst().orElseThrow(InvalidBookNameException::new);
                    result.add(book);


                }
            } finally {
                qexec.close();
            }


        return result;
    }

}
