package twitter4j.http;

/**
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public class HttpResponseEvent {

	private String url;
	private PostParameter[] postParams;
	private boolean authenticated;
	private Response response;

	/* package */ HttpResponseEvent(String url, PostParameter[] postParams,
            boolean authenticated, Response response){
		this.url = url;
		this.postParams = postParams;
		this.response = response;
		this.authenticated = authenticated;
	}

	public String getUrl() {
		return url;
	}

	public PostParameter[] getPostParams() {
		return postParams;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public Response getResponse() {
		return response;
	}
	
}
