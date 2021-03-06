package clj_httpc;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.HttpContext;

/**
 * A redirect strategy that overrides the default, and logs all the URIs that have
 * been encountered while handling redirects.
 */
public class LoggingRedirectStrategy extends DefaultRedirectStrategy {
  private List<URI> uris = new ArrayList<URI>();

  public List<URI> getURIs() {
    return this.uris;
  }

  /**
   * DefaultRedirectStrategy#getLocationURI will create and set a RedirectLocations
   * object (which wraps a HashSet) that holds all of the URIs that DefaultHttpClient
   * has encountered while handling redirects for you.
   */
  @Override
  public URI getLocationURI(
      final HttpRequest request,
      final HttpResponse response,
      final HttpContext context) throws ProtocolException {
    URI uri = super.getLocationURI(request, response, context);
    RedirectLocations redirectLocations = (RedirectLocations) context.getAttribute(REDIRECT_LOCATIONS);
    synchronized (this) {
      this.uris = redirectLocations.getAll();
    }
    return uri;
  }
}
