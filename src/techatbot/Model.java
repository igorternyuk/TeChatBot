package techatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class Model {
    private final ArrayList<String> commonPhrases;
    private final ArrayList<String> elusiveAnswers;
    private final ArrayList<String> patterns;
    private final Map<String, ArrayList<String>> answersByPatterns;
    private final Map<String, String> patternsForAnalisys;
    private final Random rand;
    private final Date date;
    public Model(){
        commonPhrases = new ArrayList<>();
        elusiveAnswers = new ArrayList<>();
        patterns = new ArrayList<>();
        answersByPatterns = new HashMap<>();
        patternsForAnalisys = new HashMap<>();
        try {
            readFromFile("txt/ruCommonPhrases.txt", commonPhrases);
            readFromFile("txt/ruElusiveAnsers.txt", elusiveAnswers);
            readFromFile("txt/patternsList.txt", patterns);
            for(int i = 0; i < patterns.size(); i += 3){
                ArrayList<String> pKeys = new ArrayList<>();
                readFromFile(patterns.get(i + 1), pKeys);
                for(String key: pKeys){
                    patternsForAnalisys.put(key, patterns.get(i));
                }
                ArrayList<String> pAswers = new ArrayList<>();
                readFromFile(patterns.get(i + 2), pAswers);
                answersByPatterns.put(patterns.get(i), pAswers);                
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }

        rand = new Random();
        date = new Date();
    }
    
    public String answer(String message, boolean ai){
        String result;
        String phrase = (message.trim().endsWith("?"))?
        elusiveAnswers.get(rand.nextInt(elusiveAnswers.size() - 1)):
        commonPhrases.get(rand.nextInt(commonPhrases.size() - 1));
        if(ai){
            String msg =
                String.join(" ", message.toLowerCase().split("[ {,|.}?;']+"));
            for (Map.Entry<String, String> o : patternsForAnalisys.entrySet()) {
                Pattern pattern = Pattern.compile(o.getKey());
                if (pattern.matcher(msg).find()){
                    ArrayList<String> answerOptions = answersByPatterns.get(o.getValue());
                    int randNum = rand.nextInt(answerOptions.size());
                    String res = answerOptions.get(randNum);
                    if(o.getValue().equals("whattime")){
                        res += ". Но мои друзья подсказали мне точную дату и время:" + date.toString();
                    }
                    return res;
                }
            }
        } 
        return phrase;
    }
    
    private void readFromFile(String pathToFile, ArrayList<String> list) throws UnsupportedEncodingException{
        try(InputStream in = getClass().getResourceAsStream(pathToFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));) {          
            String str;
            
            while((str = br.readLine()) != null){
                list.add(str);
            }
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
