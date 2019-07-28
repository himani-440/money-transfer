package com.money.transfer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.money.transfer.Application;

public class GetAccountBalanceTest {


	@Before
	public void setUp() {
		Application.startServer();
	}

	@After
	public void tearDown() {
		Application.stopServer();
	}

	@Test
	public void getAccountBalance() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final HttpGet httpget = new HttpGet("http://localhost:8080/balance/432178538321");
			try (CloseableHttpResponse response = httpClient.execute(httpget)) {
				assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
				final HttpEntity entity = response.getEntity();
				assertNotNull(entity);
				assertEquals("application/json", entity.getContentType().getValue());
				final String json = EntityUtils.toString(entity);
				assertTrue(json.contains("\"status\":\"SUCCESS\""));
				assertTrue(json.contains("\"statusCode\":\"200\""));
			}
		}
	}

	@Test
	public void getAccountBalanceValidationFail() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final HttpGet httpget = new HttpGet("http://localhost:8080/balance/4321785383");
			try (CloseableHttpResponse response = httpClient.execute(httpget)) {
				assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
				final HttpEntity entity = response.getEntity();
				assertNotNull(entity);
				assertEquals("application/json", entity.getContentType().getValue());
				final String json = EntityUtils.toString(entity);
				assertTrue(json.contains("\"status\":\"ERROR\""));
				assertTrue(json.contains("\"statusCode\":\"400\""));
			}
		}
	}



}
