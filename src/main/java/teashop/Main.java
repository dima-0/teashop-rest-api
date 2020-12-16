package teashop;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


public class Main {
	private final static String RESOURCES_PACKAGE = "teashop.resources";
	private final static URI API_BASE_PATH = URI.create("http://localhost:8080/");
	
	public static void main(String[] args) {
		DatabaseService.initEmFactory();
	    ResourceConfig rc = new ResourceConfig().packages(RESOURCES_PACKAGE);
	    JdkHttpServerFactory.createHttpServer(API_BASE_PATH, rc);
	}
}
