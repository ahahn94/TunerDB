package de.ahahn94.tunerdb;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Class containing the database methods.
 */
public class Database {
    private LinkedHashMap<String, String> database;   //Database
    private String databasePath;            //Path to database file.
    private int restoreFileResID;             //ResID of restore file.
    private Context context;

    /**
     * Constructor.
     * @param databasePath Res-ID of the database file.
     * @param restorePath Res-ID of restore file.
     * @param context App context.
     */
    public Database(String databasePath, int restorePath, Context context) {
        this.databasePath = databasePath;
        this.restoreFileResID = restorePath;
        this.context = context;
        initDatabaseFile();
    }

    /**
     * Initialize database with defaults if it does not exist.
     * Load database at program start.
     */
    private void initDatabaseFile(){
        if (!new File(databasePath).exists()){
            restoreDatabase();
        }
        else {
            database = importDatabase();
        }
        Log.d("---INFO---", "initDatabaseFile: done");
    }

    /**
     * Import database from file.
     * @return Map of the database content.
     */
    private LinkedHashMap<String, String> importDatabase(){
        LinkedHashMap<String, String> database = new LinkedHashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(databasePath));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] parts = line.split("=");
                database.put(parts[0], parts[1]);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("---INFO---", "importDatabase: done");
        return database;
    }

    /**
     * Export database to file.
     */
    private void exportDatabase(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(databasePath));
            for (Map.Entry<String, String> entry: database.entrySet()){
                bufferedWriter.write(entry.getKey() + "=" + entry.getValue());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("---INFO---", "exportDatabase: done");
    }

    /**
     * Update an entry of the database.
     * @param key Key.
     * @param value Value.
     */
    public void updateEntry(String key, String value){
        if (database.containsKey(key)){
            database.put(key, value);
            exportDatabase();
        }
        Log.d("---INFO---", "exportDatabase: done");
    }

    /**
     * Restore the database to the defaults.
     */
    public void restoreDatabase(){
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(restoreFileResID)));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(databasePath));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        database = importDatabase();
        Log.d("---INFO---", "restoreDatabase: done");
    }

    /**
     * Getter-Method for the database.
     * @return database.
     */
    public Map<String, String> getDatabase() {
        return database;
    }
}
