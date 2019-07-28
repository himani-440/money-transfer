package com.money.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SparkServerTest {
	
	@Before
    public void setUp() {
		Application.startServer();
    }

    @After
    public void tearDown() {
    	Application.stopServer();
    }
    
    @Test
    public void sparkServerTest() throws Exception{
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:8080/hello");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                final String json = EntityUtils.toString(entity);
                assertTrue(json.equalsIgnoreCase("Hello!!"));
            }
        }
    }
  
}
