package de.mediathekview.mserver.crawler.zdf;

public final class ZdfConstants {

  private ZdfConstants() {
  }

  /**
   * Name of the header required for authentification.
   */
  public static final String HEADER_AUTHENTIFICATION = "Api-Auth";

  /**
   * Base url of the ZDF website.
   */
  public static final String URL_BASE = "https://www.zdf.de";

  /**
   * Base url of the ZDF api.
   */
  public static final String URL_API_BASE = "https://api.zdf.de";

  /**
   * Url to search the films.
   */
  public static final String URL_DAY = URL_API_BASE
      + "/search/documents?hasVideo=true&q=*&types=page-video&sortOrder=desc&from=%sT00:00:00.000%%2B01:00&to=%sT23:59:59.999%%2B01:00&sortBy=date&page=1";

  /**
   * The language key of english.
   */
  public static final String LANGUAGE_ENGLISH = "eng";
  /**
   * The language key of german.
   */
  public static final String LANGUAGE_GERMAN = "deu";
}