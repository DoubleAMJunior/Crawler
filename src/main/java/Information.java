import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Information {
    private String title;
    private String description;
    private String link;
    private String width;
    private String constructionDate;
    private String price;
    private String district;
    private String numberOfRooms;
    private String publisher;
}
