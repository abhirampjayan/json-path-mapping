import com.google.gson.*;
import jsonpath.JsonPathValuePicker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;

public class Mapping {
    static JsonPathValuePicker jsonPathValuePicker=null ;
    public static void main(String[] args) {
        try{
            jsonPathValuePicker=new JsonPathValuePicker("msg.json");
            BufferedReader reader=new BufferedReader(new FileReader("mapping.json"));
            JsonElement jsonElement = JsonParser.parseReader(reader);
            System.out.println(jsonElement.getAsJsonObject().toString());
            JsonObject object =jsonElement.getAsJsonObject();
            jsonElement=traverse(object);
            System.out.println(jsonElement.getAsJsonObject().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static JsonElement traverse(JsonElement jsonElement){
        JsonObject json=new JsonObject();
        JsonObject jsonObject= jsonElement.getAsJsonObject();
        Set<String> keys=jsonObject.keySet();
        for (String item: keys
             ) {
            if (jsonObject.get(item)!=null) {
                JsonObject subObj;
                if (jsonObject.get(item) instanceof JsonObject) {
                    subObj= (JsonObject) traverse(jsonObject.get(item));
                    json.add(item,subObj);
                }else if (jsonObject.get(item) instanceof JsonArray){
                    JsonArray array=new JsonArray();
                    JsonArray jsonArray=jsonObject.getAsJsonArray(item);
                    for (JsonElement element:
                         jsonArray) {
                        subObj = (JsonObject) traverse(element);
                        array.add(subObj);
                    }
                    json.add(item,array);
                }
                else {
                    Object response = jsonPathValuePicker.getValue(jsonObject.get(item).getAsString());
                    switch (response.getClass().getSimpleName()){
                        case "Number":
                            json.addProperty(item, (Double) response);
                            break;
                        case "String":
                            json.addProperty(item, (String) response);
                            break;
                        case "Boolean":
                            json.addProperty(item, (Boolean) response);
                            break;
                        case "Character":
                            json.addProperty(item,(Character) response);
                            break;
                        default:
                            break;
                    }

                }
            }
        }
        return json;
    }
}
