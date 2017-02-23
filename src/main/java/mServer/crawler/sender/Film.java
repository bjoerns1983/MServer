package mServer.crawler.sender;

import mServer.crawler.sender.newsearch.Qualities;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a found film.
 */
public class Film
{
    private Map<Qualities,URI> urls;

    public Film()
    {
        urls = new ConcurrentHashMap<>();
    }

    public void addUrl(Qualities aQuality, URI aUrl)
    {
        if(aQuality != null && aUrl != null)
        {
            urls.put(aQuality,aUrl);
        }
    }
}