package twitter4j;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Yuuto Uehara
 * @since Twitter4J 4.0.3
 */
public class OkHttpResponse extends HttpResponse {

	private Response response;
	private HashMap<String,List<String>> headerFields;

	OkHttpResponse() {
		super();
	}

	public OkHttpResponse(HttpClientConfiguration conf) {
		super(conf);
	}

	// for test purpose
	public OkHttpResponse(Response response,HttpClientConfiguration conf) throws IOException {
		super(conf);
		this.response = response;

		Headers headers = response.headers();
		Set<String> names = headers.names();
		HashMap<String,List<String>> headerFields = new HashMap<String,List<String>>();
		for(String name:names){
			headerFields.put(name,headers.values(name));
		}
		this.headerFields = headerFields;

		is = response.body().byteStream();
		if(is!=null && "gzip".equals(response.header("Content-Encoding"))){
			is = new StreamingGZIPInputStream(is);
		}
	}

	/*package*/ OkHttpResponse(String content) {
		super();
		this.responseAsString = content;
	}


	@Override
	public int getStatusCode() {
		return response.code();
	}


	@Override
	public String getResponseHeader(String name) {
		return this.response.header(name);
	}



	@Override
	public Map<String, List<String>> getResponseHeaderFields() {
		return headerFields;
	}

	@Override
	public void disconnect() throws IOException {
	}


}
