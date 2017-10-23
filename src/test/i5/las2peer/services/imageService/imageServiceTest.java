package i5.las2peer.services.imageService;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.security.UserAgent;
import i5.las2peer.testing.MockAgentFactory;
import i5.las2peer.webConnector.WebConnector;
import i5.las2peer.webConnector.client.ClientResponse;
import i5.las2peer.webConnector.client.MiniClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 *
 * Test Image 2 - Test Class
 *
 * This class provides a basic testing framework for the microservice Test Image 2. It was
 * generated by the CAE (Community Application Framework).
 *
 */
public class imageServiceTest {

  private static final String HTTP_ADDRESS = "http://127.0.0.1";
  private static final int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

  private static LocalNode node;
  private static WebConnector connector;
  private static ByteArrayOutputStream logStream;

  private static UserAgent testAgent;
  private static final String testPass = "adamspass";

  // version does not matter in tests
  private static final ServiceNameVersion testTemplateService = new ServiceNameVersion(imageService.class.getCanonicalName(),"0.1");

  private static final String mainPath = "http://ec2-18-220-35-172.us-east-2.compute.amazonaws.com:8888";


  /**
   *
   * Called before the tests start.
   *
   * Sets up the node and initializes connector and users that can be used throughout the tests.
   *
   * @throws Exception
   *
   */
  @BeforeClass
  public static void startServer() throws Exception {

    // start node
    node = LocalNode.newNode();
    testAgent = MockAgentFactory.getAdam();
    testAgent.unlockPrivateKey(testPass); // agent must be unlocked in order to be stored
    node.storeAgent(testAgent);
    node.launch();

    ServiceAgent testService = ServiceAgent.createServiceAgent(testTemplateService, "a pass");
    testService.unlockPrivateKey("a pass");

    node.registerReceiver(testService);

    // start connector
    logStream = new ByteArrayOutputStream();

    connector = new WebConnector(true, HTTP_PORT, false, 1000);
    connector.setLogStream(new PrintStream(logStream));
    connector.start(node);
    Thread.sleep(1000); // wait a second for the connector to become ready
    testAgent = MockAgentFactory.getAdam();
    
     
  }


  /**
   * 
   * Test for the getImage method.
   * 
   */
  @Test
  public void testgetImage() {
    MiniClient c = new MiniClient();
    c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
    try {
      String imageId = "initialized";
      c.setLogin(Long.toString(testAgent.getId()), testPass);
      ClientResponse result = c.sendRequest("GET", mainPath + "//image", imageId,
        MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, new HashMap<String,String>());
      assertTrue(true); // change here
      System.out.println("Result of 'testgetImage': " + result.getResponse().trim());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception: " + e);
    }
  }

  /**
   * 
   * Test for the postImage method.
   * 
   */
  @Test
  public void testpostImage() {
    MiniClient c = new MiniClient();
    c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
    try {
      JSONObject imagePost = new JSONObject();
      c.setLogin(Long.toString(testAgent.getId()), testPass);
      ClientResponse result = c.sendRequest("POST", mainPath + "//image", imagePost.toJSONString(),
        MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, new HashMap<String,String>());
      assertTrue(true); // change here
      System.out.println("Result of 'testpostImage': " + result.getResponse().trim());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception: " + e);
    }
  }





  /**
   *
   * Called after the tests have finished. Shuts down the server and prints out the connector log
   * file for reference.
   *
   * @throws Exception
   *
   */
  @AfterClass
  public static void shutDownServer() throws Exception {
	
	 

    connector.stop();
    node.shutDown();

    connector = null;
    node = null;

    LocalNode.reset();

    System.out.println("Connector-Log:");
    System.out.println("--------------");

    System.out.println(logStream.toString());

  }

}
