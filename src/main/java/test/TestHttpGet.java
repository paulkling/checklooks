package test;

import java.io.IOException;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;

public class TestHttpGet {
	
	//CIR metadata services call
	//static String requestUrl = "http://qa.cir.lifetouch.net/CIR-MetadataServices/srv/amrs?sourceLocation=Lab+-+Minneapolis&ssid=36487672&orgUnit=LNSS";
	//static String requestUrl = "http://cir.lifetouch.net/CIR-MetadataServices/srv/amrs?sourceLocation=Lab+-+Minneapolis&ssid=LN51M562Y001001214&orgUnit=LNSS";
	static String requestUrl ="http://cir.lifetouch.net/Ingestion/rest/assetData?AssetId=686C22B4-0CFA-4038-A5C4-44B97DF7AF97";
	//CIR calling cache
	//static String requestUrl = "http://cir.lifetouch.net/cache/aggressivecache?url=http://esb-ws.lifetouch.net/layout/v1.0/layoutsInTheme/11009";
	
	
	public static void main(String[] args) throws IOException {
		TestHttpGet testHttpGet = new TestHttpGet();
		testHttpGet.something();
	}
	
	public void something() throws IOException {
		Request request = new Request(Method.GET, requestUrl);
		Client client = new Client(Protocol.HTTP);

		Response response = client.handle(request);
		Representation representation = response.getEntity();
		System.out.println("http response: " +response.getStatus().getCode()+" "+response.getStatus().getDescription());
		System.out.println("text: \n"+representation.getText());
		
	}
	
	
	/*
	public void something() throws IOException {
		new ClientResource(requestUrl).get().write(System.out);
	}*/
}
