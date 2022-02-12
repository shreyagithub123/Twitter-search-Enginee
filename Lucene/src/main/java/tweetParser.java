import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

public class tweetParser{
    
    private JSONObject tweetObj;
    private JSONObject twitter_user;
    private boolean found_twitter_user = false;

    public void setTweetObject(JSONObject new_tweetObj) throws ParseException{
        tweetObj = new_tweetObj;
        System.out.println(new_tweetObj.getClass().getName());
        if(tweetObj.containsKey("twitter_user")){
        	JSONParser jsonparser = new JSONParser();
        	String twitter_user_string= (String) tweetObj.get("twitter_user"); 
        	
        	//twitter user details :-
        	String jsonString_user="{\"twitter_user\": \"";
        	String string_end="\"}";
        	String concat_string=jsonString_user.concat(twitter_user_string);
        	String final_s=concat_string.concat(string_end);
        	System.out.println(final_s);

        	JSONObject json = (JSONObject) jsonparser.parse(final_s);

        	twitter_user= json;

            found_twitter_user = true;
        }
    }
    
    public String getTextOutOfTweet(){
        if(tweetObj.get("text") != null){
            System.out.println("YES");
            return (String) tweetObj.get("text");
        }
        System.out.println("YES");

        return "";
    }
    
    public String getTimeStamp(){
        return (String) tweetObj.get("created_at");
    }
    
    public String getBoundingCoordinatesForTweet(){
        JSONArray co_ordinates = null;
        JSONObject obj = new JSONObject();
       if(tweetObj.get("place") != null){
            JSONObject json_place = (JSONObject)tweetObj.get("place");
            JSONObject json_bound_box =(JSONObject)json_place.get("bounding_box");
        
            co_ordinates = (JSONArray)json_bound_box.get("coordinates");
            co_ordinates = (JSONArray)co_ordinates.get(0);
         
            System.out.println("TwitterParser::getBoundingCoordinates::coords");
            obj.put("coords",co_ordinates.get(0));
            System.out.println(obj.toString());
            return obj.toString();
        } 
        return null;
    }
    
    public String getFullCityNameForTweet(){
        String cityName = null;
        if(tweetObj.get("place") != null){
            JSONObject place = (JSONObject)tweetObj.get("place");
            cityName = (String) place.get("full_name");
        }
        return cityName;
    }

    public String getTwitter_userName(){
        if(found_twitter_user){
            return (String) twitter_user.get("name");
        }
        else{
            return null;
        }
    }

    public String getTwitter_userScreenName(){
        if(found_twitter_user){
            return (String) twitter_user.get("screen_name");
        }
        else{
            return null;
        }
    }
   


    public String gettwitter_userLocation(){
        if(found_twitter_user){
            return (String)twitter_user.get("location");
        }
        else{
            return null;
        }
    }
    
}
