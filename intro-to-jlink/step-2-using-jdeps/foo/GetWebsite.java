package foo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetWebsite {

  private static final Logger LOGGER = Logger.getLogger(GetWebsite.class.getName());
  public static void main(String... args) throws InterruptedException {
    String url = args[0];
    try (HttpClient client = HttpClient.newHttpClient();) {
      LOGGER.log(Level.INFO, "Calling: " + url);
      HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
      client.sendAsync(request, BodyHandlers.ofString())
	.thenApply(HttpResponse::body).thenAccept(r -> LOGGER.log(Level.INFO, r));
      client.awaitTermination(Duration.ofMillis(1000L));
    }
  }
}
