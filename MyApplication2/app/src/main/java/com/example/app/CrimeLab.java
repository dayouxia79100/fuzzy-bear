package com.example.app;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by dayouxia on 12/30/13.
 */
public class CrimeLab {

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";


    private ArrayList<Crime> mCrimes;
    private CrimeJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context appContext){
        mAppContext = appContext;

        mSerializer = new CrimeJSONSerializer(mAppContext,FILENAME);
        try{
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e){
            mCrimes = new ArrayList<Crime>();
        }
    }

    public static CrimeLab get(Context c){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }

        return sCrimeLab;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime c: mCrimes){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
}
