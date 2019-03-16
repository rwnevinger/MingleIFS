package com.oneoncology;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.OAuth.HttpMethod;

// json parser
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.io.*;
import java.text.*;
import java.util.Arrays.*;
import java.util.Map;
import java.util.regex.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.DailyRollingFileAppender;


/**
 * Infor Cloudsute oauth2 oltu client that authenticates to ION backend service credentials
 * <br>
 * License & Copyright 
 * <br>
 * Copyright (c) 2019 Grant Thornton Technology Services 
 * <br>
 * 3rd party use must be licensed by Grant Thornton
 * <br>
 * @author  robert nevinger robert.nevinger@us.gt.com
 * <br>
 * github:  https://github.com/rwnevinger
*/

public class OAuth2 extends Object {


//public Logger LOG = Logger.getLogger("");
public Logger LOG = Logger.getLogger("OAuth2.class");

// required oauth2 token parameters
private String SCOPE;
private String GRANT_TYPE;
private String CLIENT_ID;
private String CLIENT_SECRET;
private String ACCESS_TOKEN;
private String ACCESS_TOKEN_URL;
private String REFRESH_TOKEN;
private String USERNAME;
private String PASSWORD;
private String AUTHENTICATION_SERVER_URL;
private String RESOURCE_SERVER_URL;
private boolean IS_ACCESS_TOKEN_REQUEST;
private String TOKEN_TYPE;
private int EXPIRES_IN;
private OAuthClient OAUTH2CLIENT;
private OAuthClientRequest OAUTH2REQUEST;

/**
* explicit new Constructor
*
*/
public OAuth2() {

  // init object when instantiated
  this.InitProperties();

}

  private void InitProperties() {

    LOG.info("Begin InitProperties()");

    Properties oauth2Properties = new Properties();

    InputStream input = null;

    try {

        // CONFIG
        String propertiesFile = "oauth2.properties";

        LOG.info("property file " +  propertiesFile);

        input = getClass().getClassLoader().getResourceAsStream(propertiesFile);

        // load property file
        oauth2Properties.load(input);

        LOG.info(oauth2Properties.getProperty("grant_type"));
        LOG.info(oauth2Properties.getProperty("access_token_url"));
        LOG.info(oauth2Properties.getProperty("client_id"));
        LOG.info(oauth2Properties.getProperty("client_secret"));
        LOG.info(oauth2Properties.getProperty("username"));
        LOG.info(oauth2Properties.getProperty("password"));

        this.GRANT_TYPE = oauth2Properties.getProperty("grant_type");
        this.ACCESS_TOKEN_URL = oauth2Properties.getProperty("access_token_url");
        this.CLIENT_ID = oauth2Properties.getProperty("client_id");
        this.CLIENT_SECRET = oauth2Properties.getProperty("client_secret");
        this.USERNAME = oauth2Properties.getProperty("username");
        this.PASSWORD = oauth2Properties.getProperty("password");


    }
    catch (Exception e) {
      e.printStackTrace();
    }

    LOG.info("End InitProperties()");

 }


 public void requestToken() {

   try {

       // oauth2 client
       OAuthClient client = new OAuthClient(new URLConnectionClient());

       // OAuthClientRequest.tokenLocation(this.ACCESS_TOKEN_URL)
       OAuthClientRequest oAuth2Request =
                    OAuthClientRequest.tokenLocation(this.ACCESS_TOKEN_URL)
                    .setGrantType(GrantType.PASSWORD)
                    .setRedirectURI("")
                    .setUsername(this.USERNAME)
                    .setPassword(this.PASSWORD)
                    .setClientId(this.CLIENT_ID)
                    .setClientSecret(this.CLIENT_SECRET)
                    // .setScope() here if you want to set the token scope
                    .buildQueryMessage();

       OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
       OAuthResourceResponse oAuthResponse  = oAuthClient.resource(oAuth2Request, org.apache.oltu.oauth2.common.OAuth.HttpMethod.POST, OAuthResourceResponse.class);

       //System.out.println("OAuth2.requestToken() oAuthResponse " + oAuthResponse.getBody());
       LOG.info("oAuthResponse " + oAuthResponse.getBody());

       //converting JSON to object
       ObjectMapper mapper = new ObjectMapper();

       // oauth2 response can be mapphed to hashmap 
       Map<String, Object> tokenMap = new HashMap<String, Object>();

       tokenMap = mapper.readValue(oAuthResponse.getBody(), new TypeReference<Map<String,String>>(){});

       for ( String key : tokenMap.keySet() ) {
         // System.out.println( key  + ":" + tokenMap.get(key));
         // switch used to assign object the returned key values 
         // access_token:2K2quE080wBoFgUnvAXvtvyjazQG
         // refresh_token:Nfvu9IJ9C8JntDzbDk5jlZQTqDLj4bha1bBp6BXuU7
         // token_type:Bearer
         // expires_in:7200
         switch (key) {

           case "access_token":
                    this.ACCESS_TOKEN = tokenMap.get(key).toString();
                    LOG.info("setting key:" + key + " " + this.ACCESS_TOKEN);
                    break;
           case "refresh_token":
                    this.REFRESH_TOKEN = tokenMap.get(key).toString();
                    LOG.info("setting key:" + key + " " + this.REFRESH_TOKEN);
                    break;
           case "token_type":
                    this.TOKEN_TYPE = tokenMap.get(key).toString();
                    LOG.info("setting key:" + key + " " + this.TOKEN_TYPE);
                    break;
           case "expires_in":
                    this.EXPIRES_IN = Integer.parseInt(tokenMap.get(key).toString());
                    LOG.info("setting key:" + key + " " + this.EXPIRES_IN);
                    break;
           default:
             LOG.info("Warning undefined key:" + key);


         } 

       }

       // set class client so it can be fetched from calling class
       this.OAUTH2CLIENT = client;
       this.OAUTH2REQUEST = oAuth2Request;


    }
    catch (JsonGenerationException e) {
             e.getMessage();
    }
    catch (JsonMappingException e) {
             e.getMessage();
    }
    catch (Exception e) {
            e.getMessage();
    }

  }


  public String getScope() {
		return SCOPE;
  }
  public void setScope(String scope) {
		this.SCOPE = scope;
  }
  public String getGrantType() {
		return GRANT_TYPE;
  }
  public void setGrantType(String grantType) {
		this.GRANT_TYPE = grantType;
  }
  public String getClientId() {
		return CLIENT_ID;
  }
  public void setClientId(String clientId) {
		this.CLIENT_ID = clientId;
  }
  public String getClientSecret() {
		return CLIENT_SECRET;
  }
  public void setClientSecret(String clientSecret) {
		this.CLIENT_SECRET = clientSecret;
  }
  public void setAccessToken(String accessToken) {
		this.ACCESS_TOKEN = accessToken;
  }
  public String getAccessToken() {
		return ACCESS_TOKEN;
  }
  public String getAccessTokenUrl() {
		return ACCESS_TOKEN_URL;
  }
  public void setAccessTokenUrl(String accessTokenUrl) {
		this.ACCESS_TOKEN_URL = accessTokenUrl;
  }
  public String getRefreshToken() {
	return REFRESH_TOKEN;
  }
  public void setRefreshToken(String refreshToken) {
		this.REFRESH_TOKEN = refreshToken;
  }
  public String getAuthenticationServerUrl() {
	return AUTHENTICATION_SERVER_URL;
  }
  public void setAuthenticationServerUrl(String authenticationServerUrl) {
	this.AUTHENTICATION_SERVER_URL = authenticationServerUrl;
  }
  public String getUsername() {
		return USERNAME;
  }
  public void setUsername(String username) {
		this.USERNAME = username;
  }
  public String getPassword() {
		return PASSWORD;
 }
 public void setPassword(String password) {
		this.PASSWORD = password;
 }
 public boolean isAccessTokenRequest() {
		return IS_ACCESS_TOKEN_REQUEST;
 }
 public void setAccessTokenRequest(boolean isAccessTokenRequest) {
		this.IS_ACCESS_TOKEN_REQUEST = isAccessTokenRequest;
 }
 public String getResourceServerUrl() {
		return RESOURCE_SERVER_URL;
 }
 public void setResourceServerUrl(String resourceServerUrl) {
		this.RESOURCE_SERVER_URL = resourceServerUrl;
 }
 public OAuthClient getOAuth2Client() {
		return OAUTH2CLIENT;
 }
 public void setResourceServerUrl(OAuthClient client) {
		this.OAUTH2CLIENT = client;
 }
 public OAuthClientRequest getOAuth2Request() {
		return OAUTH2REQUEST;
 }
 public void setResourceServerUrl(OAuthClientRequest request) {
		this.OAUTH2REQUEST = request;
 }

}
