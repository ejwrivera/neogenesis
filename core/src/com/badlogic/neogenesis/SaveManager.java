package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * The SaveManager Class for managing save files.
 */
public class SaveManager {
    
    /** Whether the save is encoded encoded. */
    private boolean encoded;
    
    /** The file. */
    private FileHandle file = Gdx.files.local("bin/save.json"); 
    
    /** The save. */
    private Save save = getSave();
    
    /**
     * Instantiates a new save manager.
     *
     * @param encoded the encoded
     */
    public SaveManager(boolean encoded){
        this.encoded = encoded;
    }
    
    /**
     * The Class Save.
     */
    public static class Save{
        
        /** The data. */
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
    
    /**
     * Gets the save.
     * @return the save
     */
    private Save getSave(){
        Save save = new Save();
        if(file.exists()){
	        Json json = new Json();
	        if(encoded)save = json.fromJson(Save.class, Base64Coder.decodeString(file.readString()));
	        else save = json.fromJson(Save.class,file.readString());
        }
        return save==null ? new Save() : save;
    }
    
    /**
     * Save to json.
     */
    public void saveToJson(){
        Json json = new Json();
        json.setOutputType(OutputType.json);
        if(encoded) file.writeString(Base64Coder.encodeString(json.prettyPrint(save)), false);
        else file.writeString(json.prettyPrint(save), false);
    }
    
    /**
     * Load data value.
     * @param <T> the generic type
     * @param key the key
     * @param type the type
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <T> T loadDataValue(String key, Class<?> type){
        if(save.data.containsKey(key))return (T) save.data.get(key);
        else return null;   //this if() avoids and exception, but check for null on load.
    }
    
    /**
     * Save data value.
     * @param key the key
     * @param object the object
     */
    public void saveDataValue(String key, Object object){
        save.data.put(key, object);
        saveToJson(); //Saves current save immediately.
    }
    
    /**
     * Gets the all data.
     * @return the all data
     */
    public ObjectMap<String, Object> getAllData(){
        return save.data;
    }
}