package de.mediathekview.mserver.crawler.ard;

public class ArdConstants {

  public static final String BASE_URL = "https://api.ardmediathek.de/public-gateway";
  
  public static final String DEFAULT_CLIENT = "ard";
  public static final String DEFAULT_DEVICE = "pc";

  public static final int QUERY_DAY_SEARCH_VERSION = 1;
  public static final String QUERY_DAY_SEARCH_HASH = "a62c5366a603ad60a46e4b0dd0f52b1855d401026016a639bbbfbe88e2118989";
  
  public static final int QUERY_FILM_VERSION = 1;
  public static final String QUERY_FILM_HASH = "b69efa74d0e2623a9104fb94c9ed2e8f1418a68f6457594126d719a2e8ca7174";

  public static final int QUERY_TOPICS_VERSION = 1;
  public static final String QUERY_TOPICS_HASH = "fdbab76da7d6aeb1ae859e1758dd1db068824dbf1623c02bc4c5f61facb474c2";

  public static final int QUERY_TOPIC_VERSION = 1;
  public static final String QUERY_TOPIC_HASH = "f97ef19f4425f287f4c73f94032233bc3f35a04ceb12a1a9d9a60a96ffc30636";

  public static final String WEBSITE_URL = "https://www.ardmediathek.de/ard/player/%s";

  public static final String BASE_URL_SUBTITLES = "https://classic.ardmediathek.de";

  private ArdConstants() {}
}