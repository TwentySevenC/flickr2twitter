/**
 * 
 */
package com.googlecode.ebay.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.googlecode.ebay.domain.Item;
import com.googlecode.ebay.http.URLHelper;

/**
 * @author hochen
 * 
 */
public class GetSellerListDAO {
	
	private static final String GET_SELLER_LIST_REQUEST = 
			"<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
			"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
			"  <soapenv:Header>\n" +
			"    <RequesterCredentials soapenv:mustUnderstand=\"0\" xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n" +
//			"      <ebl:Credentials xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\">\n" +
//			"        <ebl:DevId>%s</ebl:DevId>\n" +
//			"        <ebl:AppId>%s</ebl:AppId>\n" +
//			"        <ebl:AuthCert>%s</ebl:AuthCert>\n" +
//			"        <ebl:Username>%s</ebl:Username>\n" +
//			"        <ebl:Password>%s</ebl:Password>\n" +
//			"      </ebl:Credentials>\n" +
			"        <ebl:eBayAuthToken xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\">%s</ebl:eBayAuthToken>\n" +
			"    </RequesterCredentials>\n" +
			"  </soapenv:Header>\n" +
			"  <soapenv:Body>\n" +
			"    <GetSellerListRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\n" +
			"      <DetailLevel>ReturnAll</DetailLevel>\n" +
			"      <Version>711</Version>\n" +
			"      <UserID>%s</UserID>\n" +
			"      <StartTimeFrom>%s</StartTimeFrom>\n" +
			"      <StartTimeTo>%s</StartTimeTo>\n" +
			"      <Pagination>\n" +
			"        <EntriesPerPage>%s</EntriesPerPage>\n" +
			"      </Pagination>\n" +
			"    </GetSellerListRequest>\n" +
			"  </soapenv:Body>\n" +
			"</soapenv:Envelope>\n";

	
	String EBAY_SANDBOX_API_SERVER = "api.sandbox.ebay.com";
	String EBAY_SANDBOX_API_PATH = "/wsapi";
	
	String EBAY_API_SERVER = "api.ebay.com";
	String EBAY_API_PATH = "/wsapi";
	
	
	public List<Item> getSellerList(
			boolean isSandbox,
			String appId,
			String token,
			String userId,
			Date StartTimeFrom,
			Date StartTimeTo,
			int entriesPerPage
	) throws IOException, SAXException {

		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put("siteid", "0");
		parameters.put("callname", "GetSellerList");
		parameters.put("appid", appId);
		parameters.put("client", "Nemo");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


		
		URL url = null;
		if (isSandbox) {
			url = URLHelper.buildUrl(true, EBAY_SANDBOX_API_SERVER, -1, EBAY_API_PATH, parameters);
		} else {
			url = URLHelper.buildUrl(true, EBAY_API_SERVER, -1, EBAY_API_PATH, parameters);
		}
		
		String requestXml = String.format(GET_SELLER_LIST_REQUEST, token, userId, sdf.format(StartTimeFrom), sdf.format(StartTimeTo), String.valueOf(entriesPerPage) );
		
		System.out.println(url);
		System.out.println(requestXml);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("SOAPAction", "");
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");

		
		
		DataOutputStream out = new DataOutputStream(
				conn.getOutputStream());
		out.writeBytes(requestXml);
		out.flush ();
		out.close();

		
		conn.connect();

		InputStream in = null;
		Document document = null;
		try {

			System.out.println(conn.getResponseCode());
			
			if (conn.getResponseCode() >= 400) {
				in = conn.getErrorStream();
				System.out.println(getInputStream(in));
			} else {
				in = conn.getInputStream();
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = builderFactory.newDocumentBuilder();

				document = builder.parse(in);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}

		if (document == null) {
			return null;
		}

		NodeList nodeList = document.getElementsByTagName("Item");

		List<Item> lstItem = new ArrayList<Item>();
		if (nodeList != null) {

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				NodeList itemRootNode = node.getChildNodes();
				Item item = new Item();
				lstItem.add(item);
				generateItem(itemRootNode, item);

			}
		}

		return lstItem;
	}
	
	private void generateItem(NodeList itemNode, Item item) {
		
		if (itemNode == null || item == null) {
			return;
		}
		
		for (int k = 0; k < itemNode.getLength(); k++) {

			Node firstLevelNode = itemNode.item(k);
			if (itemNode != null) {

				String nodeName = firstLevelNode.getNodeName();
				String nodeValue = getNodeValue(firstLevelNode);
				
				if (nodeName != null) {
					if ("ItemID".equals(nodeName)) {
						try {
							item.setItemId(Long.parseLong(nodeValue));
						} catch (NumberFormatException nfe) {

						}
					} else if ("Title".equals(nodeName)) {
						item.setTitle(nodeValue);
					} else if ("ListingDetails".equals(nodeName)) {
						generateItem(firstLevelNode.getChildNodes(), item);
					} else if ("ViewItemURL".equals(nodeName)) {
						item.setViewItemURL(nodeValue);
					} else if ("PictureDetails".equals(nodeName)) {
						generateItem(firstLevelNode.getChildNodes(), item);
					} else if ("GalleryURL".equals(nodeName)) {
						item.setGalleryURL(nodeValue);
					} 
				}

			}

		}
	}
	
	private String getInputStream(InputStream is) throws IOException {
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    String line = "";
		    StringBuilder builder = new StringBuilder();
		    while((line = reader.readLine()) != null) {
		        builder.append(line);
		    }
		    return builder.toString();

	}

	private String getNodeValue(Node node) {

		if (node == null || node.getFirstChild() == null) {
			return null;
		}

		return node.getFirstChild().getNodeValue();

	}

	public static void main(String args[]) throws Exception {

		Calendar tenMinBeforecurrentTime = Calendar.getInstance();
		tenMinBeforecurrentTime.add(Calendar.MINUTE, -10);
		

		Calendar currentTime = Calendar.getInstance();
		
		GetSellerListDAO sessionDao = new GetSellerListDAO();
		System.out.println(
				sessionDao.getSellerList(
				false,
				"ebaysjinternal",
				"AgAAAA**AQAAAA**aAAAAA**xre1Sw**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFlYCjDpaGowudj6x9nY+seQ**AgAAAA**AAMAAA**DIWV/4lHrc6zyd2bHG+Y7H7wFMfkNM72DZNhtS8iwb/DBPJph72ORDyQtnuBDNeHZPs89atJAIFQBRM/bgNTl9BOyjlUcCJFPuf2mPI4q8904jW07I8kLx1hp5aVAtJ8Z4CsAuELGfVg+f7JaFdw2+4vOHvRCDK6v/o2ucDauhz/wchtpfTnZw6djKIrmaUvJNTt6/YI0C8a5VV91DDNFSgEbzPMZ6hsDntknFDgGkCLyGf5InwJLtXvtZ7UmJ/KX86Lm8i8bTGXRNjLuwPBtjhNLzGfRyTIYK2MdIkJd8mLO9NClwuwSNPqPR4o4XoprREun4cm5YRdtXZ8ZCY4fkK2nC3FuHrgFUq5mcrJzqeKCwbqGiGXotJAfd4aU07rSCsNcuto2w0Bg067rFQNeHRubjWyky0rksVsbdV1sdkD5sOHkyp85oFoKX8F9fLNCjgKy2KoKtzc4f1RYcZBtjRd57+ViWW2Hn8mK6Trqa+wFcJ3uhomvFQkbUiIIieAX6gBkYTmovQTYgiJ4vlyM5uhLf8/DstejnCctM/azwZj8VaS9yV8SVR51bmp2p1KAXkNbGU/eVKqNdxUw06BTJfJstfEycBzhchGzC4D7OERKzpS2e7VlhhIezL+8HWXKvSfzK1MqG/KWenb0Yv8evQ0WnymnzXKkTT7emy3daD5FSanavnUmsjwHqrm3eT/5q7G1CG8LUi/Lv2Bomwj0mkAzMXQDu6w7mOl6/EFhZssD8l5WX1vj61+pJGxiYDp", 
				"eforcity", 
				tenMinBeforecurrentTime.getTime(), 
				currentTime.getTime(),
				20));
		

		System.out.println(
				sessionDao.getSellerList(
				true,
				"eBayb1609-29f8-4684-aadb-6ba5a05a182",
				"AgAAAA**AQAAAA**aAAAAA**VyBeTQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wJnY+iAZeEqQ2dj6x9nY+seQ**rS0BAA**AAMAAA**aoGraHo8YuNvBclGXrIgdnxGZm7y5Hvm45WprtR0mG0ViRWk7xjw6P6rYtmUCUHP/4T+dYgt4jugQp8ZGzMpyYwd8H0Srf9dDS7lwMhwMkBL4RZ0UFvcbYCR1ukgXWlXWQ3PtGdjnBCAjacHlrGiwJr/XdlaBET/yNwyQpi3Qt0NMRhT0XAXjgaV+Me9SfvJ3oTY6U4vXj7Ui1K7OpUxPBPsg3RQ+PI055EttgYMe2Gw1z0RxxxETzruKX30g2KzvAx+SRF5jtoDECvl0eMJMN+pC8VOSLwQ8UBTsdds3GH+w+iS47fV0l3pvGOFlH/iJ0uIxM0cHGVctqUT246JBBp3Va1BDU3L9P78frLBzkReB8NHza020geqMf2oklt6mFb7gepfqVNvJEAF/SyRUC2CbuV1gYKXf2AAALazne6MNYP4H5EHb44eq3ncQx7P9QXkLOXQO+CT5AxxdrCwFfPh9E6ybqK1k4+vyBzKJEebLTZ1Q9cJBZy6iZ/hMO0YW3k2tcZOXwlI6E8xrQhW0nzrzv32a9uALTSFP+nS9ozFMEkR10HiDEZQlcba+Nm2T+8kJ2fFkzW5Tm741rIvrDkmSHP0I9nOqeUblrxv9lNMF+DFQyJShZEPd3Pn/NphDohhjcmdYEOFnnd2j6Xs2ojoS7BKplISME6we/eebk/W+7Xe1RJarStmJusrKcneYmTVlpK072bVaQcTdkSI4y10QcjbEG1ZVyaDm77b6XuEmDb8pHdXyQoStDNR30+J", 
				"tuser1", 
				//"testuser_sandboxfgofh",
				tenMinBeforecurrentTime.getTime(), 
				currentTime.getTime(),
				20));
		


	}

}
