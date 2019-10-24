package seedu.address.model.feed;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

import org.jsoup.Jsoup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;

/**
 * Represents a feed in the feed list.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Feed {
    private final String name;
    private final String address;

    /**
     * Every field must be present and not null.
     */
    public Feed(String name, String address) {
        requireAllNonNull(name, address);
        this.name = name;
        this.address = address;
    }

    /**
     * Fetches the most recent 5 posts from this feed.
     *
     * @return List of maximum 5 posts.
     */
    public ObservableList<FeedPost> fetchPosts() {
        String feedData = this.fetchFeedData();
        ObservableList<FeedPost> feedPosts = this.parseFeedData(feedData);

        return feedPosts;
    }

    /**
     * Fetches the feed's data as a String. This method first attempts to fetch data from the feed's remote
     * address. If unsuccessful, it attempts to read the local cached feed data.
     * @return String representing the feed data.
     */
    private String fetchFeedData() {
        String feedData = null;
        String xmlFile = String.format("data/cache/%s.xml", name.toLowerCase().replaceAll(" ", "-"));

        try {
            // Fetch remote
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            int c;
            StringBuilder buffer = new StringBuilder();

            while ((c = in.read()) != -1) {
                buffer.append((char) c);
            }
            feedData = buffer.toString();

            // Write
            FileUtil.createFile(Paths.get(xmlFile));
            BufferedWriter out = new BufferedWriter(new FileWriter(xmlFile));
            out.write(feedData);

            // Cleanup
            out.close();
            in.close();
            conn.disconnect();

            LogsCenter.getLogger(Feed.class).info(
                    String.format("[Feed: %s] Successfully fetched remote feed data and updated local cache", name));
        } catch (Exception e) {
            LogsCenter.getLogger(Feed.class).warning(
                    String.format("[Feed: %s] Failed to fetch remote feed data, attempting to use local cache", name));

            // Attempt to fetch feed data from local cache
            try {
                BufferedReader in = new BufferedReader(new FileReader(xmlFile));

                int c;
                StringBuilder buffer = new StringBuilder();

                while ((c = in.read()) != -1) {
                    buffer.append((char) c);
                }
                feedData = buffer.toString();

                LogsCenter.getLogger(Feed.class).info(
                        String.format("[Feed: %s] Successfully fetched feed data from local cache", name));
            } catch (Exception e2) {
                LogsCenter.getLogger(Feed.class).warning(
                        String.format("[Feed: %s] Failed to fetch feed data from both remote and local cache", name));
            }
        }

        return feedData;
    }

    /**
     * Parses the raw feed data into an observable list of feed posts.
     * @param feedData String representing the raw feed data.
     * @return List of feed posts.
     */
    private ObservableList<FeedPost> parseFeedData(String feedData) {
        ObservableList<FeedPost> feedPosts = FXCollections.observableArrayList();

        try {
            Scanner sc = new Scanner(feedData);
            int count = 0;
            String lineBuffer1;
            String lineBuffer2;
            String title = null;
            String link = null;

            while ((lineBuffer1 = sc.nextLine()) != null && count < 5) {
                if (lineBuffer1.contains("<title>")) {
                    lineBuffer2 = sc.nextLine();

                    if (lineBuffer2.contains("<link>")) {
                        title = Jsoup.parse(lineBuffer1).text()
                                .replace("<title>", "").replace("</title>", "").strip();
                        link = Jsoup.parse(lineBuffer2).text()
                                .replace("<link>", "").replace("</link>", "").strip();

                        FeedPost feedPost = new FeedPost(name, title, link);
                        feedPosts.add(feedPost);
                        count = count + 1;
                    }
                }
            }

            sc.close();
        } catch (Exception e) {
            LogsCenter.getLogger(Feed.class).warning(
                    String.format("[Feed: %s] Failed to parse feed posts", name));
        }

        return feedPosts;
    }

    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }

    /**
     * Returns true if both feeds have the same name and address.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Feed)) {
            return false;
        }

        Feed otherFeed = (Feed) other;
        return otherFeed.getName().equals(getName())
                && otherFeed.getAddress().equals(getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getName(), getAddress());
    }
}
