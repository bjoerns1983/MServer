package de.mediathekview.mserver.base.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class UrlUtilsTestGetProtocol {
    private final String inputUrl;
    private final Optional<String> expectedProtocol;

    public UrlUtilsTestGetProtocol(String aInputUrl, Optional<String> aExpectedProtocol) {
        inputUrl = aInputUrl;
        expectedProtocol = aExpectedProtocol;
    }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
      return Arrays.asList(
              new Object[][]{
                      {null, Optional.empty()},
                      {"", Optional.empty()},
                      {"https://www.testurl.de/resource?query=3", Optional.of("https:")},
                      {"www.urlohneschema.de/child", Optional.empty()},
                      {"http://www.test.de", Optional.of("http:")},
                      {"rtmp://www.test.de", Optional.of("rtmp:")},
              });
  }

  @Test
  public void getBaseUrlTest() {
    Optional<String> actual = UrlUtils.getProtocol(inputUrl);

      assertThat(actual, equalTo(expectedProtocol));
  }
}
