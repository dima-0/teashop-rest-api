package teashop;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;


public class Main {

	public static void main(String[] args) {
		DatabaseService.initEmFactory();

	    ResourceConfig rc = new ResourceConfig().packages("teashop.resources");
	    HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create("http://localhost:8080/"), rc);
	}
}
