package com.money.transfer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.money.transfer.Application;
import com.money.transfer.domain.Account;

public class AccountsMoneyTransferTest {

	@Before
	public void setUp() {
		Application.startServer();
	}

	@After
	public void tearDown() {
		Application.stopServer();
	}

	@Test
	public void transferMoney() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final HttpPut httpput = new HttpPut("http://localhost:8080/transfer/432178538321");
			final Account payload = new Account();
			payload.setAccNumber("541387692467");
			payload.setAmount(100.0);
            final String jsonPayload = new Gson().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpput.setEntity(stringEntity);
			try (CloseableHttpResponse response = httpClient.execute(httpput)) {
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
	public void transferMoneyFromAccountValidationFail() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final HttpPut httpput = new HttpPut("http://localhost:8080/transfer/43217853831");
			final Account payload = new Account();
			payload.setAccNumber("541387692467");
			payload.setAmount(99100.0);
            final String jsonPayload = new Gson().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpput.setEntity(stringEntity);
			try (CloseableHttpResponse response = httpClient.execute(httpput)) {
				assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
				final HttpEntity entity = response.getEntity();
				assertNotNull(entity);
				assertEquals("application/json", entity.getContentType().getValue());
				final String json = EntityUtils.toString(entity);
				assertTrue(json.contains("\"status\":\"ERROR\""));
				assertTrue(json.contains("\"statusCode\":\"400\""));
				assertTrue(json.contains("\"message\":\"Please provide valid Account Number\""));
			}
		}
	}
	
	@Test
	public void transferMoneyToAccountValidationFail() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final HttpPut httpput = new HttpPut("http://localhost:8080/transfer/432178538321");
			final Account payload = new Account();
			payload.setAccNumber("54138769246");
			payload.setAmount(100.0);
            final String jsonPayload = new Gson().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpput.setEntity(stringEntity);
			try (CloseableHttpResponse response = httpClient.execute(httpput)) {
				assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
				final HttpEntity entity = response.getEntity();
				assertNotNull(entity);
				assertEquals("application/json", entity.getContentType().getValue());
				final String json = EntityUtils.toString(entity);
				assertTrue(json.contains("\"status\":\"ERROR\""));
				assertTrue(json.contains("\"statusCode\":\"400\""));
				assertTrue(json.contains("\"message\":\"Please provide valid Account Number\""));
			}
		}
	}
	
	@Test
	public void transferMoneyInsufficientBalanceTest() throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			final HttpPut httpput = new HttpPut("http://localhost:8080/transfer/432178538321");
			final Account payload = new Account();
			payload.setAccNumber("541387692467");
			payload.setAmount(99100.0);
            final String jsonPayload = new Gson().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpput.setEntity(stringEntity);
			try (CloseableHttpResponse response = httpClient.execute(httpput)) {
				assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
				final HttpEntity entity = response.getEntity();
				assertNotNull(entity);
				assertEquals("application/json", entity.getContentType().getValue());
				final String json = EntityUtils.toString(entity);
				assertTrue(json.contains("\"status\":\"ERROR\""));
				assertTrue(json.contains("\"statusCode\":\"400\""));
				assertTrue(json.contains("\"message\":\"Insufficient Money\""));
			}
		}
	}

}
