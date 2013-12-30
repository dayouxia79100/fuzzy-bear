package com.example.app;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by dayouxia on 12/30/13.
 */
public class CrimeJSONSerializer {

    private Context mContext;
    private  String mFilename;

    public CrimeJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes){
        JSONArray array = new JSONArray();
        for(Crime c: crimes){
            try{
            array.put(c.toJSON());
            } catch (Exception e){

            }
        }

        Writer writer = null;
        try{
            OutputStream out = mContext
                    .openFileOutput(mFilename,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }
        catch (IOException e ){

        }

        finally{
            if(writer != null){
                try{
                    writer.close();
                } catch (Exception e){

                }
            }
        }

    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException{
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;

        try{
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();

            for(int i = 0; i < array.length(); i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }

            Log.d("CrimeJSONSerializer","crimes being loaded");
        } catch (FileNotFoundException e){

        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return crimes;




    }
}
