package it.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

// before execute, run PersonAppMain.
//TODO Run App from JUnit testcase.
public class PersonApiTest {

	 HttpResponse res;
	 HttpRequestFactory requestFactory
	 	= (new NetHttpTransport()).createRequestFactory();
	
	@Test public void requestParam() throws Exception {
		
		// exe
		res = requestFactory.buildGetRequest(
			new GenericUrl("http://localhost:8080/person?id=123")
        ).execute();
        
		// check
        assertThat(res.getStatusCode()).isEqualTo(200);
        
        assertThat(
        		res.getContentType()
        ).isEqualTo("application/json;charset=utf-8");
        
        assertThat(
        		res.parseAsString()
        ).isEqualTo("{\"id\":\"123\"}");
	}
	
	@Test public void requestParams() throws Exception {
		
		// exe
		res = requestFactory.buildRequest(
				"POST",
				new GenericUrl("http://localhost:8080/person/params"),
				ByteArrayContent.fromString(
					"application/x-www-form-urlencoded",
					"id=1234&name=Tom&langs=Perl&langs=PHP"
				)
        ).execute();
        
		// check
        assertThat(res.getStatusCode()).isEqualTo(200);
        
        assertThat(
        		res.getContentType()
        ).isEqualTo("application/json;charset=utf-8");
                
        assertThat(
        		res.parseAsString()
        ).isEqualTo(
        		"{\"id\":1234,\"name\":\"Tom\",\"langs\":[\"Perl\",\"PHP\"]}"
        );
	}
	
	@Test public void requestJson() throws Exception {
		
		// pre
		String bodyJson = 
		"{\"id\":12345,\"name\":\"Bob\",\"langs\":[\"C\",\"Java\",\"JS\"]}";
		
		// exe
		res = requestFactory.buildRequest(
				"POST",
				new GenericUrl("http://localhost:8080/person/json"),
				ByteArrayContent.fromString(
					"application/json",	bodyJson
				)
        ).execute();
		
		// check
        assertThat(res.getStatusCode()).isEqualTo(200);
        
        assertThat(
        		res.getContentType()
        ).isEqualTo("application/json;charset=utf-8");
        
        assertThat(res.parseAsString()).isEqualTo(bodyJson);
	}
}
