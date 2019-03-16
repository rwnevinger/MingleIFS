package com.oneoncology;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.io.InputStream;
import java.util.Properties;
import java.lang.reflect.*;

// json parser
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

// log4j logger
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.commons.codec.binary.Base64;

// oltu oauth2
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuth.HttpMethod;
import org.apache.oltu.oauth2.common.OAuth.ContentType;

// local custom class
import com.oneoncology.OAuth2;
import com.oneoncology.MingleUser;
import com.oneoncology.InvalidJSONArray;

/**
 * Infor Cloudsute Mingle UserXml generator
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

public class UserXml {

// logger
static Logger LOG = Logger.getLogger("UserXml.class");

String ACCESS_TOKEN_URL = new String();
String GRANT_TYPE = new String();
String CLIENT_ID = new String();
String CLIENT_SECRET  = new String();
String USERNAME  = new String();
String PASSWORD = new String();
String SECURITY_TEMPLATE = new String();
String nextURL = new String();
String prevURL = new String();
int CLASSCOUNT = 0;
boolean setClassCount = true;
String HOSTNAME = new String();
String HCM_CLASSES = new String();
String FSM_CLASSES = new String();
String TENANT_ENV = new String();
String FOLDER = new String();
OAuth2 OAuth2 = new OAuth2();
//OAuth OAuthContentType = new OAuth.ContentType.JSON
//ContentType JSON = org.apache.oltu.oauth2.common.OAuth.ContentType.JSON;
//ContentType JSON = new OAuth.ContentType.JSON();
ContentType ContentType = new org.apache.oltu.oauth2.common.OAuth.ContentType();
boolean RequestToken = true;

public Hashtable<String,Hashtable> AllEmployees = new Hashtable<String,Hashtable>();
public Hashtable<String,String> CSROLES = new Hashtable<String,String>();
public Hashtable<String,Object> MINGLEUSERS = new Hashtable<String,Object>();
public Hashtable<String,String> RESTAPIs = new Hashtable<String,String>();
public Hashtable<String,String> EmployeeRecord = new Hashtable<String,String>();
public Hashtable<String,String> MingleUserName = new Hashtable<String,String>();
public Hashtable<String,String> LandmarkActors = new Hashtable<String,String>();
public Hashtable<String,String> MingleIDActor = new Hashtable<String,String>();

// email regex
public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


   /**
   * explicit new Constructor
   *
   */
   public UserXml() {


   }

/**
 * Optional  configure interface through properties file
*/

   public void initProperties() {

    LOG.info("Begin initProperties()");

    Properties classProperties = new Properties();

    InputStream input = null;

    try {

        // CONFIG
        //String propertiesFile = "src/main/resources/userxml.properties";
        String propertiesFile = "userxml.properties";

        LOG.info("property file " +  propertiesFile);

        // assign property file
        //input = new FileInputStream(propertiesFile);

        input = getClass().getClassLoader().getResourceAsStream(propertiesFile);
 
       
        // load property file
        classProperties.load(input);


        this.HOSTNAME = classProperties.getProperty("hostname");
        this.HCM_CLASSES = classProperties.getProperty("hcm_classes");
        this.FSM_CLASSES = classProperties.getProperty("fsm_classes");
        this.TENANT_ENV = classProperties.getProperty("tenant_env");
        this.FOLDER = classProperties.getProperty("folder");

        LOG.info("hostname:" + this.HOSTNAME);
        LOG.info("hcm_classes:" + this.HCM_CLASSES);
        LOG.info("fsm_classes:" + this.FSM_CLASSES);
        LOG.info("tenant_env:" + this.TENANT_ENV);
        LOG.info("folder:" + this.FOLDER);

    }
    catch (Exception e) {
      e.printStackTrace();
    }

    LOG.info("End initProperties()");

  }


  /**
   * parse security template
  */

  public void parseTemplate() {

    LOG.info("Begin parseTemplate()");

    String securityTemplate = this.FOLDER + "/" + this.SECURITY_TEMPLATE;

    BufferedReader  bReader = null;
    String line = new String();
    int recCount = 0;
    int initAssignedIndex = 0;

   // TODO
   // REST API calls on the following fields
   // field to search on:  EmployeeMingleId::kloggins@tnonc.com
   // attribute I want returned:  EmployeeActor::kim.loggins
   // API call that returns Actor based on MingleID
   // https://fin-oneoncology-tst.inforcloudsuite.com/hcm/soap/classes/Employee/lists/
   // _generic?_fields=EmployeeActor&_limit=1&_filter=EmployeeMingleId%3A%3Ajmccarty%40tnonc.com

    try {

      File fileTest = new File(securityTemplate);

      if(fileTest.isFile()) {
         //System.out.println("UserXml.parseTemplate " + securityTemplate + " exists");
         System.out.println("Parsing Security template " + securityTemplate);
         LOG.info(securityTemplate + " exists");
            bReader = new BufferedReader(new FileReader(securityTemplate));
            while ((line = bReader.readLine()) != null) {

              String key = new String();
              if(recCount > 0) {

                  // pipe delimited
                  String[] userRecord = line.split("\\|");

                  // instantiate object
                  MingleUser MingleUser = new MingleUser();

                  // set properties
                  MingleUser.setEmployee(userRecord[0]);
                  MingleUser.setName(userRecord[1]);
                  MingleUser.setFirstName(userRecord[2]);
                  MingleUser.setLastName(userRecord[3]);
                  // save email address as lowercase
                  MingleUser.setEmail(userRecord[4].toLowerCase());
                  // objects keyed by email address
                  // remove leading & trailing whitespace
                  // convert email to lowercase, this will match MingleId lowercase key
                  key = userRecord[4].trim().toLowerCase();

                  LOG.info("setting Employee " + userRecord[0]);
                  LOG.info("setting Name " + userRecord[1]);
                  LOG.info("setting FirstName " + userRecord[2]);
                  LOG.info("setting LastName " + userRecord[3]);
                  LOG.info("setting Email " + userRecord[4]);

                  // iterate record to determine assigned roles
                  for(int i = 0; i < userRecord.length; i++) {
                   if(i > 4) {
                     String assigned = userRecord[i].toUpperCase();
                     if(assigned.equals("X")) {
                       // i = column index...this way we can pull the column name by column index 
                       //String role = header[i];
                       //RESTAPIs.put(Integer.toString(i),role);
                       //LOG.info("Setting Roles index " + i + ":" + role);
                       // i = our current column index
                       // debug only....
                       String assignedRole = CSROLES.get(Integer.toString(i));
                       MingleUser.setAssignedRoles(assignedRole);
                       LOG.info("  setAssignedRoles(" + assignedRole + ")");
                     }
                   }
                 }
                 // all properties have been set
                 // the email address must be unique
                 LOG.info("  MINGLEUSERS put() " + key); 
                 MINGLEUSERS.put(key,MingleUser);
                 // 
                 setActor(key);
              }
              else {
                // header rec
                LOG.info("Setting CloudSuite Roles CSROLES");
                // pipe delimited
                String[] header = line.split("\\|");

                for(int i = 0; i < header.length; i++) {
                   if(i > 4) {
                     // i = column index...this way we can pull the column name by column index 
                     String role = header[i];
                     CSROLES.put(Integer.toString(i),role);
                     LOG.info("Setting Roles index " + i + ":" + role);
                     // set method propety which will be used to size roleAssignmentIndex
                     // it's overkill but it will work
                     initAssignedIndex = i;
                   }

                }
                recCount++;
              }

            } // read next line

      }

    }
    catch (Exception e) {
    }

    /*
    catch(FileNotFoundException e) {
        e.getMessage();
        e.printStackTrace();
    }
    catch(IOException e) {
        e.getMessage();
        e.printStackTrace();
    }
    */


    LOG.info("End parseTemplate()");


  }

  /*
   * set Landmark Actor 
   *
  */
  public void setActor(String email) {

    LOG.info("Begin setActor(" + email + ")");

    String Actor = new String();

    boolean createActor = false;;

    MingleUser MingleUser = new MingleUser();

    try {

       // read Mingle Object
       MingleUser = (MingleUser)  MINGLEUSERS.get(email);

       if(MingleUserName.containsKey(email)) {

         // read MingleUsername
         String UserName = MingleUserName.get(email);

         LOG.info("Existing Mingle User " + email + " read IdentityActor by Service " + UserName);

         // Search IdentityActor by Service
         // https://hcm-oneoncology-tst.inforcloudsuite.com/hcm/soap/classes/IdentityActor/lists/_generic?_fields=Actor&_filter=Service%3A%3AUser%3Aee38e43d-7a9a-43d0-b80a-43b40fc1a2dd&_limit=1&access_token=mprB4qxLGHjJiNXX1z6Gu6w5p5Cv

        // step 1 build API and set count
        String ionAPI = this.HOSTNAME + this.TENANT_ENV + this.HCM_CLASSES + "IdentityActor/lists/_generic?_fields=Actor&_filter=Identity%3A%3AUser%3A";
               ionAPI += UserName;

        // step 2 get a token
        if(this.RequestToken) {
           LOG.info("request token");
           // instantiate OAuth2
           OAuth2.requestToken();
           this.RequestToken = false;
        }

        // step 3 execute API
        LOG.info("request API " + ionAPI);
        OAuthResourceResponse resourceResponse = execAPI(ionAPI);

        //converting JSON to object
        ObjectMapper mapper = new ObjectMapper();

        // ION generic_list retuns top level array e.g. [ {},{} ]
        // IF _pagNav=false or is disabled
        JsonNode rootNode = mapper.readTree(resourceResponse.getBody());

        LOG.info(resourceResponse.getBody());

        // generic_lists returns two primary nodes
        JsonNode countNode = mapper.readTree("[]");
        JsonNode fieldNode = mapper.readTree("[]");


       if(rootNode.isArray()) {
        for (JsonNode node : rootNode) {
          // read fields node
          JsonNode _fields = node.path("_fields");
          Actor = _fields.path("Actor").asText();
        }
       }
       else {
        throw new InvalidJSONArray("UserXml setLandmarkActors IONAPI call did not return an array");
       }

      }
      else if(MingleIDActor.containsKey(email)) {
         // for some reason...MingleIDs are in User Managment, but not Mingle
         // as a precaution check if the MingleID is in the Actor table
         Actor = MingleIDActor.get(email);
         System.out.println("*** WARNING Email Address " + email + " is assigned to Actor " + Actor + " but not reported by Mingle");
         System.out.println("    this MingleID will not be created, please verify Mingle User Manangement and Rich Client Actor Managment");
         LOG.info("*** WARNING Email Address " + email + " is assigned to Actor " + Actor + " but not reported by Mingle");
         LOG.info("    this actor will not be created, please verify Mingle User Manangement and Rich Client Actor Managment");
      }
      else {
          // redudant but such is life
          createActor = true;
      }
    }
    catch (Exception e) {
        e.getMessage();
        e.printStackTrace();
    }
  
    if(createActor) { 
      Actor = MingleUser.getFirstName().toLowerCase() + "." + MingleUser.getLastName().toLowerCase() + "."  + "1"; 
      // validate that we don't have a duplicate actor
      if(LandmarkActors.containsKey(Actor)) {
        System.out.println("*** Duplicate Actor " + LandmarkActors.get(Actor));
        // read current actor
        String currentActor = LandmarkActors.get(Actor);
        // split on period
        String[] actorArray = currentActor.split("\\.");
        // read current version which is 3 field or 2 array index
        int version = Integer.parseInt(actorArray[2]);
        // increment version
        version++;
        // create new key
        Actor = MingleUser.getFirstName().toLowerCase() + "." + MingleUser.getLastName() + "." + version;
        System.out.println("*** Incrementing Actor " + Actor);
      }
      System.out.println("*** Create Actor " + Actor + " for MingleId::" + email);
      LOG.info("  *** Create Actor " + Actor + " for MingleId::" + email);
      MingleUser.setLandmarkActor(Actor);
    }
    else {
      MingleUser.setLandmarkActor(Actor);
      System.out.print("MingleId::" + email + " Roles will updated");
      System.out.println(" **If you did not intend to change this user remove them from the Security Template");
      LOG.info("MingleId::" + email + " Roles will updated");
      LOG.info("If you did not intend to change this user remove them from the Security Template");
    }

    // update collection
    MINGLEUSERS.put(email,MingleUser);

    LOG.info("End setActor");

  }
 
  /*
   * initialize LandmarkActors hashtable
   * key=actor value=username
   * this is the link to the ssopv2 identity
   *
  */
  public void setLandmarkActors() {

    LOG.info("Begin setLandmarkActors");
    System.out.println("Setting LandmarkActors where IsMingleUser is true");

    try {

      Pattern emailPattern = Pattern.compile(this.EMAIL_PATTERN);

      // step 1 build API and set count
      String ionAPI = this.HOSTNAME + this.TENANT_ENV + this.HCM_CLASSES + "Actor/lists/_generic?_fields=Actor&_filter=IsMingleUser::true&_limit=1";

      // step 2 get a token
      if(this.RequestToken) {
         LOG.info("request token");
         // instantiate OAuth2
         OAuth2.requestToken();
         this.RequestToken = false;
      }

       // step 3 execute API
       LOG.info("request API " + ionAPI);
       OAuthResourceResponse resourceResponse = execAPI(ionAPI);

       //converting JSON to object
       ObjectMapper mapper = new ObjectMapper();

       // ION generic_list retuns top level array e.g. [ {},{} ]
       // IF _pagNav=false or is disabled
       JsonNode rootNode = mapper.readTree(resourceResponse.getBody());

       // generic_lists returns two primary nodes
       JsonNode countNode = mapper.readTree("[]");
       JsonNode fieldNode = mapper.readTree("[]");

       int i = 0;

       // if _pageNav=false or is disabled  should always have an array returned
       if(rootNode.isArray()) {
        for (JsonNode node : rootNode) {
         switch (i) {
           case 0:
             countNode = node;
             i++;
           break;
           case 1:
             fieldNode = node;
             i++;
           break;
           default:
           i++;
         }
        }
       }
       else {
        throw new InvalidJSONArray("UserXml setLandmarkActors IONAPI call did not return an array");
       }

      // set Mingle Actor Count 
      int MingleActorCount = 0;
      MingleActorCount = Integer.parseInt(countNode.path("_count").toString());
      System.out.println("MingleActorCount " + countNode.path("_count").asText());
      LOG.info("MingleActorCount " + countNode.path("_count").asText());


      // now that we have MingleActorCount we need to read all actors
      // _fields=Actor,UniqueIDl&_filter=IsMingleUsertrue&_limit=1000&page_nav=false
      // _fields=Actor,UniqueID&_filter=IsMingleUser::true&_limit=1000&page_nav=false
      ionAPI = this.HOSTNAME + this.TENANT_ENV + this.HCM_CLASSES + "Actor/lists/_generic?"; 
      // append name value pairs
      ionAPI += "_fields=Actor,MingleId&_filter=IsMingleUser%3A%3Atrue&_limit=" + MingleActorCount + "&_pageNav=false&_OUT=JSON";

      LOG.info("execAPI " + ionAPI);
      resourceResponse = execAPI(ionAPI);

      //  _pageNav=false returns a single _fields node
      rootNode = mapper.readTree(resourceResponse.getBody());

      LOG.info("is rootNode an array ? "  + rootNode.isArray());

      if(rootNode.isArray()) {
        for (JsonNode node : rootNode) {
          // read fields node
          JsonNode _fields = node.path("_fields");
          String Actor = _fields.path("Actor").asText();
          String MingleId = _fields.path("MingleId").asText();
          if(LandmarkActors.containsKey(Actor)) {
            System.out.println("***WARNING duplicate actor + " + Actor + " assigned MingleId " + MingleId );
            LOG.info("***WARNING duplicate actor + " + Actor + " assigned MingleId " + MingleId );
          }
          else if(MingleIDActor.containsKey(MingleId)) {
            // read assignment
            String assignedActor = MingleIDActor.get(MingleId);
            System.out.println("***WARNING Actor " + assignedActor  + " is assigned to MingleID " + MingleId);
            System.out.println("  " +  Actor + " should be assigned to a unique email address validate in Rich Client Actor Management");
            LOG.info("***WARNING Actor " + assignedActor + " is assigned to MingleID " + MingleId);
            LOG.info("  " +  Actor + " should be assigned to a unique email address");
          }
          else {
            LOG.info ("Add key Actor::" + Actor + " value MingleId::" + MingleId + " to LandmarkActors hashtable");
            // key actor to mingleid
            LandmarkActors.put(Actor,MingleId);
            // we only want legit email address 
            // match ampersam
            // String regex = "\\@";
            Matcher emailMatcher = emailPattern.matcher(MingleId);
            if(emailMatcher.matches()) {
              // key lowercase mingleid to actor 
              MingleIDActor.put(MingleId.toLowerCase(),Actor);
              LOG.info ("Add key MingleId::" + MingleId+ " value Actor::" + Actor + " to MingleIDActor hashtable");
            }
          }
        }
       }
       else {
        throw new InvalidJSONArray("UserXml setLandmarkActors IONAPI call did not return an array");
       }
   }
   catch (Exception e) {
        e.getMessage();
        e.printStackTrace();
   }

    LOG.info("End setLandmarkActors");

  }

  /*
   * initialize MingleUserName hash table
   * key=mingle email id value=username
   * usernmame links the mingleid to the the ssopv2 identity
   *
  */
  public void setMingleUserName() {

    LOG.info("Begin setMingleUserName");
    System.out.println("Setting MingleUserName ssopv2 identity");

    MingleUser MingleUser = new MingleUser();

    try {

       //MingleUser = (MingleUser)  MINGLEUSERS.get(email);

      // step 1 build API
      String ionAPI = this.HOSTNAME + this.TENANT_ENV + "Mingle/SocialService.Svc/User/%20/AllUsers";

      // step 2 get a token
      if(this.RequestToken) {
         LOG.info("request token");
         // instantiate OAuth2
         OAuth2.requestToken();
         this.RequestToken = false;
      }

       // step 3 execute API
       LOG.info("request API " + ionAPI);
       OAuthResourceResponse resourceResponse = execAPI(ionAPI);

       //LOG.info(resourceResponse);

       //converting JSON to object
       ObjectMapper mapper = new ObjectMapper();

       // ION generic_list retuns top level array e.g. [ {},{} ]
       // IF _pagNav=false or is disabled
       JsonNode rootNode = mapper.readTree(resourceResponse.getBody());

       JsonNode userDetailListNode = rootNode.path("UserDetailList");

       LOG.info("is rootNode an array ? "  + rootNode.isArray());
       LOG.info("is UserDetailList an array ? "  + userDetailListNode.isArray());

       int i = 0;
       if(userDetailListNode.isArray()) {
        for (JsonNode userNode : userDetailListNode) {
          String UserName = userNode.path("UserName").asText();
          String MingleId = userNode.path("Email").asText();
          // key lowercase email addresse to username which is ssopv2 identity
          MingleUserName.put(MingleId.toLowerCase(),UserName);
          LOG.info ("Add key MingleId::" + MingleId + " value UserName::" + UserName + " to MingleUserName hashtable");
          i++;
        }
       }
       else {
        throw new InvalidJSONArray("UserXml setMingleUserName IONAPI call did not return an array");
       }

       // read _count node
       LOG.info ("Processed : " + i + " Mingle Users");
   }
   catch (Exception e) {
        e.getMessage();
        e.printStackTrace();
   }

    LOG.info("End setMingleUserName");

  }
 

  public OAuthResourceResponse execAPI(String ionAPI) {

       LOG.info("Begin execAPI");

       OAuthResourceResponse resourceResponse = new OAuthResourceResponse();

     try {

       LOG.info("  issued oauth2 token " + OAuth2.getAccessToken());

       // new constructor
       OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(ionAPI).buildQueryMessage();

       // setHeader() inherited from org.apache.oltu.oauth2.client.request.OAuthClientRequest
       // token streamed in header
       bearerClientRequest.setHeader("Authorization", "Bearer " + OAuth2.getAccessToken());

       // set header to accept application/json
       // this is ignored
       // NOTE:  using Chrome developer tools, the Mingle json is added to the Request Header as
       // accept: application/json, so the first addHeader option should be used
       // On the response header I see:  content-type: application/json
       bearerClientRequest.addHeader("Accept", "application/json");
       //bearerClientRequest.addHeader("Content-Type", "application/json");
       bearerClientRequest.setHeader("Accept", "application/json");

       LOG.info("OAuth2ClientRequest bearerClientRequest initialized using Oauth2 token " + OAuth2.getAccessToken());
       LOG.info(bearerClientRequest.toString());

       OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

       // override a private method using reflection
       // declare reflecton method NOTE:  setContentType accepts a String as an argument...so we have String.class
       //Method privateSetContentType = resourceResponse.getClass().getDeclaredMethod("setContentType", String.class);
       //privateSetContentType.setAccessible(true);
       //privateSetContentType.invoke(resourceResponse,ContentType.JSON);

       // set this for Mingle API calls, HCM/FSM use _OUT parameter
       //resourceResponse.setContentType(ContentType.JSON);

       resourceResponse = (OAuthResourceResponse)oAuthClient.resource(bearerClientRequest, "GET", OAuthResourceResponse.class);

       //System.out.println("  API Request:  " + ionAPI);
       //System.out.println("  API Response:  " + resourceResponse.getResponseCode());

       LOG.info("  API Request:  " + ionAPI);
       LOG.info("  API Response:  " + resourceResponse.getResponseCode());

       if(resourceResponse.getResponseCode() != 200) {
         System.out.println(" Error API Response:  " + resourceResponse.getResponseCode());
         System.out.println(" Body:  " + resourceResponse.getBody());
         LOG.info(" Error API Response:  " + resourceResponse.getResponseCode());
         LOG.info(" Body:  " + resourceResponse.getBody());
       }

       //LOG.info(resourceResponse.getBody());

      }
      catch (Exception e) {
        e.getMessage();
        e.printStackTrace();
      }

       LOG.info("End execAPI");

       return resourceResponse;
   }

    /**
     * genericList API call
     *
     * @param 
     */
   public void genericList() {

     LOG.info("Begin genericList()");

     try {

       Vector <String> sortedKeys = new Vector<String>();

       // read collection keys
       Enumeration keys = RESTAPIs.keys();

       while(keys.hasMoreElements()) {
          sortedKeys.add( (String) keys.nextElement());
       }

       // iterate keys and build 
       for (int i=0; i<sortedKeys.size(); i++) {

          String Employee = sortedKeys.get(i);
          String ionAPI = RESTAPIs.get(Employee);

          LOG.info("Employee:" + Employee + "::" + ionAPI);
          OAuthResourceResponse resourceResponse = execAPI(ionAPI);

          //converting JSON to object
          ObjectMapper mapper = new ObjectMapper();

          //  _pageNav=false returns a single _fields node
          JsonNode rootNode = mapper.readTree(resourceResponse.getBody());

          LOG.info("is rootNode an array ? "  + rootNode.isArray());

          if(rootNode.isArray()) {

            for (JsonNode node : rootNode) {

        
              // hashmap key 
              String EmployeeKey = new String();
              // read fields node
              JsonNode _fields = node.path("_fields");

              //LOG.info(_fields);

              // read field names
              Iterator<String> names = _fields.fieldNames();
              while (names.hasNext()) {
                 String name = names.next();
	         String value = _fields.path(name).asText();
                 // omit EmployeePurgeReport
                 if (name.equals("EmployeePurgeReport")) { continue; }
                 if (name.equals("Employee")) { 
                   EmployeeKey = value; 
                 }
                 LOG.info(name + "::" + value);
                 if(EmployeeRecord.containsKey(name)) {
                   System.out.println("WARNING:: duplicate Employee map key " + name);
                   LOG.info("WARNING:: duplicate key " + name);
                 }
                 else {
                   EmployeeRecord.put(name,value);
                 }
             }

             if(AllEmployees.containsKey(EmployeeKey)) {
               System.out.println("WARNING:: duplicate AllEmployees map key " + EmployeeKey);
               LOG.info("WARNING:: duplicate key " + EmployeeKey);
             }
             else {
               LOG.info("Add " + EmployeeKey + " to AllEmployee Hashtable");
               AllEmployees.put(EmployeeKey,EmployeeRecord);
             }
 
             // clear Employee
             LOG.info("Clear Employee Hashtable");
             EmployeeRecord.clear();

           } // next node
        }
        else {
          System.out.println("Warning genericList() rootNode is not an array");
          LOG.info("Warning generickList() rootNode is not an array");
        }

      } // read and executie next API

     }
     catch (Exception e) {
      e.getMessage();
      e.printStackTrace();
     }


     LOG.info("End genericList()");

    }

   /**
    * Generate mingle loaduser.xml 
    * 
    * @throws Exception               
   */
   public void loadUserXml() {

      LOG.info("Begin");

      System.out.println("Creating Mingle loaduser xml file");

      Vector <String> sortedKeys = new Vector<String>();
      
      try {

        // date format
        DateFormat mmddyyyy = new SimpleDateFormat("MMddyyyyms");
 
        Calendar calendar = Calendar.getInstance();

        Date date = new Date();
        String dateStamp = mmddyyyy.format(date);

        String lawDir = System.getenv("LAWDIR");

        File xmlFile = null;
        String fileName = new String();

        fileName = this.FOLDER + "/loaduser_" + dateStamp + ".xml";

        // create file
        xmlFile = new File(fileName);
        xmlFile.createNewFile();

        // create filewriter object
        FileWriter fwXml = new FileWriter(xmlFile);
        BufferedWriter xml = new BufferedWriter(fwXml);

        System.out.println("xml FileWriter " + fileName);
        LOG.info("xml FileWriter " + fileName);

        xml.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        xml.write("<Users>\n\n");

        // read colleciton keys
        Enumeration keys = MINGLEUSERS.keys();

        while(keys.hasMoreElements()) {
          sortedKeys.add( (String) keys.nextElement());
        }

        // sort vector
        Collections.sort(sortedKeys);

        for (int i=0; i<sortedKeys.size(); i++) {

          MingleUser MingleUser = new MingleUser();
          String email = sortedKeys.get(i);

          if(MINGLEUSERS.containsKey(email)) {

            LOG.info("  valid mingle user " + email);

            MingleUser = (MingleUser) MINGLEUSERS.get(email);

            xml.write("<User>\n");
            xml.write("  <FirstName>");
            xml.write(MingleUser.getFirstName());
            xml.write("</FirstName>\n");

            xml.write("  <LastName>");
            xml.write(MingleUser.getLastName());
            xml.write("</LastName>\n");

            xml.write("  <CommonName>");
            xml.write(MingleUser.getFirstName() + " " + MingleUser.getLastName() );
            xml.write("</CommonName>\n");

            xml.write("  <Department>");
            xml.write("</Department>\n");

            xml.write("  <Email>");
            xml.write(email);
            xml.write("</Email>\n");

            xml.write("  <FederatedId>");
            xml.write("</FederatedId>\n");

            xml.write("  <PersonId>");
            xml.write("</PersonId>\n");

            xml.write("  <Status>Active");
            xml.write("</Status>");

            xml.write("  <Title>>");
            xml.write("</Title>\n");

            xml.write("  <UPN>");
            xml.write(email);
            xml.write("</UPN>\n");

            xml.write("  <UserName>");
            xml.write(email);
            xml.write("</UserName>\n");

            xml.write("  <UserGuid>");
            xml.write("</UserGuid>\n");

            xml.write("  <GenericProperties>\n");
            xml.write("    <GenericProperty>");
            xml.write(MingleUser.getLandmarkActor());
            xml.write("</GenericProperty>\n");
            xml.write("  </GenericProperties>\n");

            xml.write("  <SecurityRoles>\n");

            ArrayList<String> assignedRoles = MingleUser.getAssignedRoles();

            for(String role: assignedRoles) {
              xml.write("    <SecurityRole Name=\"" + role + "\">" + role  + "</SecurityRole>\n");
            }
           
            xml.write("  </SecurityRoles>\n");
            xml.write("</User>\n\n");

          }
          else {
            LOG.info("  **WARNING valid mingle user " + email);

          }

        } // read next key

        xml.write("</Users>\n");
        xml.flush();
        xml.close();

     }
     catch (NullPointerException e) {
        System.out.println("AutoEss.loadUserXml SimpleDateFormat Date exception " + e.toString());
        LOG.error("possible SimpleDateFormat exception " + e.toString());
     }
     catch (IllegalArgumentException  e) {
        System.out.println("AutoEss.loadUserXml SimpleDateFormat Date exception " + e.toString());
        LOG.error("possible SimpleDateFormat exception " + e.toString());

     }
     catch (Exception e) {
      e.getMessage();
      e.printStackTrace();
     }

     LOG.info("End loadUserXml()");


   }


  /**
     * @param args String array Command line arguments are ignored.
     */
    public static void main(String[] args) {

    try {


      // instantiate objects
      UserXml UserXml = new UserXml();
      UserXml.initProperties();
      UserXml.SECURITY_TEMPLATE = args[0];
      UserXml.setMingleUserName();
      UserXml.setLandmarkActors();
      UserXml.parseTemplate();
      UserXml.loadUserXml();


    }
    catch (Exception e) {
            e.printStackTrace();
    }


    }

}

