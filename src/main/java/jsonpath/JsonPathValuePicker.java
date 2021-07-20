package jsonpath;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonPathValuePicker {
    private Object document;
    public JsonPathValuePicker(String path) {
        BufferedReader reader;
        try {
            LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            ch.qos.logback.classic.Logger log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");
            log.setLevel(Level.INFO);
            reader = new BufferedReader(new FileReader(path));
            JsonElement jsonElement = JsonParser.parseReader(reader);
            this.document=Configuration.defaultConfiguration().jsonProvider().parse(jsonElement.getAsJsonObject().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Object getValue(String jsonPath){
        return JsonPath.read(document,jsonPath);
    }
}
