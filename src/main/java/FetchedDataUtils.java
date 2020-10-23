import com.google.gson.Gson;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.util.*;

@Data
public class FetchedDataUtils {
    private static Stack<WebURL> fetchedLinked = new Stack<>();
    private static Stack<String> fetchedHTMLs = new Stack<>();
    private static List<Information> information = new ArrayList<>();

    public static void saveURL(WebURL url) {
        fetchedLinked.push(url);
    }

    public static void saveHTML(String html) {
        fetchedHTMLs.push(html);
    }

    public static void parseHTMLs() {
        while (!fetchedHTMLs.empty()) {
            String html = fetchedHTMLs.pop();
            String link = fetchedLinked.pop().toString();
            HTMLParser parser = HTMLParser.builder()
                    .html(html)
                    .link(link)
                    .build();
            information.add(parser.extractInformation());
        }
    }

    public static void printInformation(){
        information.forEach(System.out::println);
    }

    @SneakyThrows
    public static void saveInJSONFile(){
        PrintWriter writer = new PrintWriter("information.json");
        Gson gson = new Gson();
        for (Information value : information) {
            String json = gson.toJson(value);
            writer.println(json);
            writer.flush();
        }
    }


}


