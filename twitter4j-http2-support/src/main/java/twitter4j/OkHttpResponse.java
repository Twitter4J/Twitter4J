/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package twitter4j;

import okhttp3.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Yuuto Uehara - muemi.himazin at gmail.com
 * @since Twitter4J 4.0.3
 */
public class OkHttpResponse extends HttpResponse {

	private OkHttpClient okHttpClient;
	private Call call;
	private Response response;
	private HashMap<String,List<String>> headerFields;

	OkHttpResponse() {
		super();
	}

	public OkHttpResponse(HttpClientConfiguration conf) {
		super(conf);
	}

	// for test purpose
	public OkHttpResponse(Call call, OkHttpClient okHttpClient, HttpClientConfiguration conf) throws IOException {
		super(conf);
		this.okHttpClient = okHttpClient;
		this.call = call;
		this.response = call.execute();

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

		statusCode = response.code();
	}

	/*package*/ OkHttpResponse(String content) {
		super();
		this.responseAsString = content;
	}

	public Protocol getProtocol(){
		return response.protocol();
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
		call.cancel();
	}


}
