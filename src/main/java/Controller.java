import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    static boolean updateForDay=false;
    public static void main(String[] args) throws Exception {

        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        config.setCrawlStorageFolder("items/storage");

        //respect politeness
        config.setPolitenessDelay(1000);

        config.setMaxDepthOfCrawling(1);

        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        config.setMaxPagesToFetch(1000);

        // ignore binary data such as pdf, image , ...
        config.setIncludeBinaryContentInCrawling(false);


        // This config parameter can be used to set your crawl to be resumable
        // (meaning that you can resume the crawl from a previously
        // interrupted/crashed crawl). Note: if you enable resuming feature and
        // want to start a fresh crawl, you need to delete the contents of
        // rootFolder manually.
        config.setResumableCrawling(false);

        // Set this to true if you want crawling to stop whenever an unexpected error
        // occurs. You'll probably want this set to true when you first start testing
        // your crawler, and then set to false once you're ready to let the crawler run
        // for a long time.
//        config.setHaltOnError(true);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        int numberOfPages = 1;
        for (int i = 1; i < numberOfPages; i++) {
            controller.addSeed("https://divar.ir/s/chabahar/buy-residential?page=" + i);
        }

        // Number of threads to use during crawling.
        int numberOfCrawlers = 8;

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<Crawler> factory = Crawler::new;

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        controller.start(factory, numberOfCrawlers);


        FetchedDataUtils.parseHTMLs();
        FetchedDataUtils.printInformation();
        FetchedDataUtils.saveInJSONFile();
    }
}
