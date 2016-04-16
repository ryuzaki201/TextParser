/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Adrian Jiwantoro
 */
public class TextParser {

    /**
     * Mapping is a method to store input (corpus) to hashmap then 
     * write all sentence without tag to output file
     */
    public void mapping() {
        // Set pattern regex to read tag <kalimat id=num>X</kalimat>
        Pattern awalTag = Pattern.compile("<kalimat id=(\\S+)>");
        Pattern akhirTag = Pattern.compile("</kalimat>");
        Matcher aTag = awalTag.matcher("");
        Matcher eTag = akhirTag.matcher("");

        String sCurrentLine;
        String id = "";
        // Data Structure used to store all sentence with tag from corpus with given id
        Map<String, String> map = new HashMap<String, String>();

        try {
            // Set testing data file
            BufferedReader br = new BufferedReader(new FileReader("input/Indonesian_Manually_Tagged_Corpus_ID.tsv"));
            // Set output data file
            File file = new File("output/Indonesian_Manually_Tagged_Corpus_ID_Text.tsv");

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // Read line text
            while ((sCurrentLine = br.readLine()) != null) {

                String temp = sCurrentLine;
                // Token to separate input by tab
                StringTokenizer st = new StringTokenizer(temp, "\t");
                aTag.reset(sCurrentLine);
                eTag.reset(sCurrentLine);

                // Set id if it matches the regex "<kalimat id=num>", if it matches "</kalimat>"
                // do nothing. Else insert data to map with id and value
                if (aTag.find()) {
                    id = aTag.group(1);

                } else if (eTag.find()) {

                } else {
                    if (map.get(id) == null) {
                        map.put(id, st.nextToken() + " ");
                    } else {
                        map.put(id, map.get(id) + st.nextToken() + " ");
                    }
                }
            }
            // Write all Map Value (Random) into output file
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bw.write(entry.getValue() + "\n");
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * removeTag is a method to remove all tag "<kalimat id=num>x</kalimat>" 
     * from input (corpus), so that it can be read and trained into model tagger
     */
    public void removeTag() {
        // Set pattern regex to read tag <kalimat id=num>X</kalimat>
        Pattern awalTag = Pattern.compile("<kalimat id=(\\S+)>");
        Pattern akhirTag = Pattern.compile("</kalimat>");
        Matcher aTag = awalTag.matcher("");
        Matcher eTag = akhirTag.matcher("");

        String sCurrentLine;

        try {
            // Set testing data file
            BufferedReader br = new BufferedReader(new FileReader("input/Indonesian_Manually_Tagged_Corpus_ID.tsv"));
            // Set output data file
            File file = new File("output/Indonesian_Manually_Tagged_Corpus_ID_Edit.tsv");

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // Read line text
            while ((sCurrentLine = br.readLine()) != null) {

                String temp = sCurrentLine;
                aTag.reset(sCurrentLine);
                eTag.reset(sCurrentLine);
                
                // If it matches the regex "<kalimat id=num>" do nothing, if it matches 
                // "</kalimat>" write new line into output file. Else insert data to map 
                // write currentdata to output file
                if (aTag.find()) {

                } else if (eTag.find()) {
                    bw.write("\n");
                } else {
                    bw.write(sCurrentLine + "\n");
                }

            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * parsing is a method to separate 1000 sentence of 10000 sentence of
     * corpus. It separate randomly because of HashMap data structure
     */
    public void parsing() {
        // Set pattern regex to read tag <kalimat id=num>X</kalimat>
        Pattern awalTag = Pattern.compile("<kalimat id=(\\S+)>");
        Pattern akhirTag = Pattern.compile("</kalimat>");
        Matcher aTag = awalTag.matcher("");
        Matcher eTag = akhirTag.matcher("");

        String sCurrentLine;
        String id = "";
        // Data Structure used to store all sentence with tag from corpus with given id
        Map<String, String> map = new HashMap<String, String>();

        try {
            // Set testing data file
            BufferedReader br = new BufferedReader(new FileReader("input/Indonesian_Manually_Tagged_Corpus_ID.tsv"));
            // Set output data file, 1000 sentence which will be tagged
            File file = new File("output/Indonesian_Manually_Tagged_Corpus_ID_Final.tsv");
            // Set output data file, 9030 sentence which will be trained
            File file2 = new File("output/Indonesian_Manually_Tagged_Corpus_ID_Model.tsv");

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            // Read line text
            while ((sCurrentLine = br.readLine()) != null) {

                String temp = sCurrentLine;
                aTag.reset(sCurrentLine);
                eTag.reset(sCurrentLine);
                
                // Set id if it matches the regex "<kalimat id=num>", if it matches "</kalimat>"
                // do nothing. Else insert data to map with id and value
                if (aTag.find()) {
                    id = aTag.group(1);

                } else if (eTag.find()) {

                } else {
                    if (map.get(id) == null) {
                        map.put(id, temp + "\n");
                    } else {
                        map.put(id, map.get(id) + temp + "\n");
                    }
                }

            }

            int i = 0;
            Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
            // Looping to separate 1000 sentences from hashmap then write into
            // output/Indonesian_Manually_Tagged_Corpus_ID_Final.tsv and all the
            // rest of sentences to Indonesian_Manually_Tagged_Corpus_ID_Model.tsv
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (i < 1000) {
                    bw.write(entry.getValue() + "\n");
                    iter.remove();
                    i = i + 1;
                } else {
                    bw2.write(entry.getValue() + "\n");
                }
            }

            bw.close();
            bw2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TextParser test = new TextParser();
        test.removeTag();
        test.mapping();
        test.parsing();
    }

}
