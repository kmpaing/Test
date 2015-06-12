package com.f6.tests;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
 
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
 
 
 
 
public class RestTest {
    private static final String iiqIP = "192.168.218.135";
    private static final int iiqPort = 8080;
    private static final String iiqUser = "spadmin";
    private static final String iiqPass = "admin";
 
    public static void main(String[] args) {
      
    try {
 
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(
                new AuthScope(iiqIP,iiqPort),
                new UsernamePasswordCredentials(iiqUser, iiqPass));
        //
        // Call to ping
        //
      
      
        String iiqRequest = "http://" + iiqIP + ":" + String.valueOf(iiqPort) + "/identityiq/rest/ping";
        System.out.println("\nRequest: " + iiqRequest);
        HttpGet request = new HttpGet(iiqRequest);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null)
                System.out.println(line);
 
        //
        // Call to get identity
        //
      
        iiqRequest = "http://" + iiqIP + ":" + String.valueOf(iiqPort) + "/identityiq/rest/identities/spadmin";
        System.out.println("\nRequest: " + iiqRequest);
        request = new HttpGet(iiqRequest);
        response = client.execute(request);
        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
      
      
        //
        // Request to checkPasswordPolicies
        //
      
 
        iiqRequest = "http://" + iiqIP + ":" + String.valueOf(iiqPort) + "/identityiq/rest/policies/checkPasswordPolicies";
        System.out.println("\nRequest: " + iiqRequest);
        HttpPost post = new HttpPost(iiqRequest);
        JSONObject json = new JSONObject();
        json.put("id", "Adam.Kennedy");
        json.put("password", "test");
        StringEntity se = new StringEntity(json.toString());
        System.out.println("Outbound JSON: " + json.toString());
        se.setContentType("application/json");
        post.setEntity(se);
        response = client.execute(post);
        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        
        //Call to test showIdentity
        iiqRequest = "http://" + iiqIP + ":" + String.valueOf(iiqPort) + "/identityiq/rest/authentication";
        System.out.println("\nRequest: " + iiqRequest);
        request = new HttpGet(iiqRequest);
        
        request.setHeader("userID", iiqUser);
        request.setHeader("password", "admin");
        
        response = client.execute(request);
        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        
      
        //
        // Call Test Workflow
        //
      
 
        iiqRequest = "http://" + iiqIP + ":" + String.valueOf(iiqPort) + "/identityiq/rest/workflows/Test/launch";
        System.out.println("\nRequest: " + iiqRequest);
        post = new HttpPost(iiqRequest);
        json = new JSONObject();
        HashMap map = new HashMap();
        map.put("foo1","value1");
        map.put("foo2","value2");
        json.put((String)"workflowArgs", map);
        se = new StringEntity(json.toString());
        System.out.println("Outbound JSON: " + json.toString());
        se.setContentType("application/json");
        post.setEntity(se);
        response = client.execute(post);
        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
      
      
      
      
        } catch (Exception e) {
            System.out.println("Exception:" + e.toString());
        }
    }
}
