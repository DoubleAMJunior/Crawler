import edu.uci.ics.crawler4j.url.WebURL;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@Data
public class HTMLParser {
    private String html;
    private String link;
    private Document document;

    @Builder
    public HTMLParser(String html,String link) {
        this.html = html;
        this.link = link;
        loadPage();
    }

    @SneakyThrows
    private void loadPage() {
        document = Jsoup.parse(html);
    }

    public Information extractInformation() {
        System.out.println("extracting data");
        String title = "";
        String description = "";
        String link = this.link;
        String width = "";
        String constructionDate = "";
        String price = "";
        String district = "چابهار";
        String numberOfRooms = "";
        String publisher = "";

        Elements topicElement = document.getElementsByClass("kt-page-title__title kt-page-title__title--responsive-sized");
        title = topicElement.get(0).text();

        if(Controller.updateForDay){
            Elements dateElement=document.getElementsByClass("kt-page-title__subtitle kt-page-title__subtitle--responsive-sized");
            String date=dateElement.get(0).text();
            if(date.contains("روز")) {
                System.exit(0);
            }
        }

        // extract width, construction year, numberOfRooms
        Elements elements = document.getElementsByClass("kt-group-row-item kt-group-row-item--info-row");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String elementText = element.text();
            String value = elementText.replaceAll("[\\p{InArabic}&&\\PN]", "");
            switch (i) {
                case 0:
                    width = value;
                    break;
                case 1:
                    constructionDate = value;
                    break;
                case 2:
                    numberOfRooms = value;
                    break;
            }
        }

        // extract publisher, price and in some cases width
        Elements newElements = document.getElementsByClass("kt-base-row kt-unexpandable-row");
        for (Element element : newElements) {
            Elements innerElementsValues = element.getElementsByClass("kt-base-row__end kt-unexpandable-row__value-box");
            Elements innerElementsTopics = element.getElementsByClass("kt-base-row__start kt-unexpandable-row__title-box");
            for (int j = 0; j < innerElementsValues.size(); j++) {
                String innerElementTopic = innerElementsTopics.get(j).text();
                String innerElementValue = innerElementsValues.get(j).text();
                switch (innerElementTopic) {
                    case "قیمت کل":
                        price = innerElementValue;
                        break;
                    case "آگهی\u200Cدهنده":
                        publisher = innerElementValue;
                        break;
                    case "متراژ":
                        width = innerElementValue;
                        break;
                }
            }
        }

        //extract description
        Elements descriptionElement = document.getElementsByClass("kt-description-row__text post-description kt-description-row__text--primary");
        description = descriptionElement.text();

        DataBaseConnector.addEntery(title,description,link,width,constructionDate,price,district,numberOfRooms,publisher);

        return Information.builder()
                .title(title)
                .publisher(publisher)
                .constructionDate(constructionDate)
                .price(price)
                .numberOfRooms(numberOfRooms)
                .description(description)
                .district(district)
                .link(link)
                .width(width)
                .build();
    }

}
