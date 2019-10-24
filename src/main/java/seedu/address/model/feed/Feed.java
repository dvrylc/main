package seedu.address.model.feed;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import org.jsoup.Jsoup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;

/**
 * Represents a feed in the feed list.
 * Guarantees: details are present and not null, `name` and `address` field values are validated, immutable.
 */
public class Feed {
    private final String name;
    private final String address;
    private String data = "";

    /**
     * Every field must be present and not null.
     */
    public Feed(String name, String address) {
        requireAllNonNull(name, address);
        this.name = name;
        this.address = address;
    }

    /**
     * Constructor that takes in initial feed data in addition to the usual fields.
     */
    public Feed(String name, String address, String data) {
        this(name, address);
        requireAllNonNull(data);
        this.data = data;
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
     * address. If unsuccessful, it returns the cached feed data.
     * @return String representing the feed data.
     */
    private String fetchFeedData() {
        String feedData = null;

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

            // Update local cache
            this.data = feedData;

            // Cleanup
            in.close();
            conn.disconnect();

            LogsCenter.getLogger(Feed.class).info(
                    String.format("[Feed: %s] Successfully fetched remote feed data and updated local cache", name));
        } catch (Exception e) {
            LogsCenter.getLogger(Feed.class).warning(
                    String.format("[Feed: %s] Failed to fetch remote feed data, using local cache", name));

            feedData = this.data;
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

    public String getData() {
        return data;
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
