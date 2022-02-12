import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class tweetIndexer{
	
    private IndexWriter indexWriter = null;
    private tweetParser tweetParserObj = new tweetParser();
    private String indexPath = null;
    public ArrayList<String> indexFieldList = new ArrayList<String>();

    public static void main(String[] args) throws IOException{
        System.out.println("IndexCreator main...");
        tweetIndexer tweetIndexerObj = new tweetIndexer();
        tweetIndexerObj.createIndexForIndexer();
    }
             
    public tweetIndexer()throws IOException {
        
        this.indexPath = new File(".").getCanonicalPath()+ "/indices/";	
        Directory indexDir = FSDirectory.open( new File(this.indexPath).toPath() );
        System.out.println("IndexPath is: " + indexPath);

       
        IndexWriterConfig indexConfig = new IndexWriterConfig( new StandardAnalyzer()); 
        indexWriter = new IndexWriter(indexDir, indexConfig);
    }
    
    public Document getDocumentForIndexer(JSONObject json_obj) throws IOException, ParseException{
        Document doc = new Document();
        tweetParserObj.setTweetObject(json_obj);
        if(tweetParserObj.getTextOutOfTweet() != null){

            String indexWriter = "text";
            Field text = new TextField(indexWriter,tweetParserObj.getTextOutOfTweet(), Field.Store.YES);
            doc.add(text);        
            this.addToIndexFieldList(indexWriter);
        }

        if(tweetParserObj.getTimeStamp() != null){
     
            String indexWriter = "timestamp";
            Field timestamp = new StringField(indexWriter,tweetParserObj.getTimeStamp(), Field.Store.YES);
            doc.add(timestamp);        
            this.addToIndexFieldList(indexWriter);
            }

        if(tweetParserObj.gettwitter_userLocation() != null){
            
            String indexWriter = "location";
            Field location = new StringField(indexWriter,tweetParserObj.gettwitter_userLocation(), Field.Store.YES);
            doc.add(location);        
            this.addToIndexFieldList(indexWriter);
            }
        if(tweetParserObj.getBoundingCoordinatesForTweet() != null){
           
            String indexWriter = "coords";
            Field location = new StringField(indexWriter,tweetParserObj.getBoundingCoordinatesForTweet(), Field.Store.YES);
            doc.add(location);        
            this.addToIndexFieldList(indexWriter);
        }
        if(tweetParserObj.getTwitter_userName() != null){
           
            String indexWriter = "username";
            Field username = new StringField(indexWriter,tweetParserObj.getTwitter_userName(), Field.Store.YES);
            doc.add(username);        
            this.addToIndexFieldList(indexWriter);
            }

        if(tweetParserObj.getTwitter_userScreenName() != null){
           
            String indexWriter = "userscreenname";
            Field userscreenname = new StringField(indexWriter,tweetParserObj.getTwitter_userScreenName(), Field.Store.YES);
            doc.add(userscreenname);        
            this.addToIndexFieldList(indexWriter);
            }
        return doc;    
    }

    public void createIndexForIndexer() throws IOException{
        try{
        	File file = new File("C:\\Users\\EndUser\\eclipse-workspace\\beep\\data\\data.json");
        	Scanner scan = new Scanner(file, "UTF-8");       	
            JSONObject obj;
            System.out.println("inside createIndexForIndexer() : ");
            while(scan.hasNext()) {
            	System.out.println("iterating createIndexForIndexer(): ");
                    obj = (JSONObject) new JSONParser().parse(scan.nextLine());
                    Document doc = this.getDocumentForIndexer(obj);
                    System.out.println("creating document : ");
                    this.indexWriter.addDocument(doc);
                    System.out.println("added document to index Writer");

            }
            this.indexWriter.close();
            scan.close();
        }
        catch(ParseException pe){
            System.out.println(pe);
        }   
    }

    private void addToIndexFieldList(String indexWriter){
        if(!this.indexFieldList.contains(indexWriter)){
            this.indexFieldList.add(indexWriter);
            }
        }
}