package com.kevin.mmitestx;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigHandler extends DefaultHandler {
	private final static boolean DBG = false;
	private final String TAG = "TestHandler";
	List<TestItem> list;
	TestItem currentTestItem;

	public List<TestItem> getList(){
		return list;
	}

	public void startDocument() throws SAXException {
		super.startDocument();
		list = new ArrayList<TestItem>();
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (DBG) Log.d(TAG, "startElement: uri=" + uri +
			"; localName=" + localName +
			"; qName=" + qName +
			"; attributes=" + attributes.getValue("data"));
		if(qName!=null&&qName.equals("test_item")){
			currentTestItem = new TestItem();
		}
		if(qName!=null&&qName.equals("name")){
			String name = attributes.getValue("data");
			if(currentTestItem!=null){
				currentTestItem.setName(name);
			}	
		}
		if(qName!=null&&qName.equals("title")){
			String title = attributes.getValue("data");
			if(currentTestItem!=null){
				currentTestItem.setTitle(title);
			}	
		}
		if(qName!=null&&qName.equals("test")){
			String test = attributes.getValue("data");
			if(currentTestItem!=null){
				if (test.equals("true")){
					currentTestItem.setTest(true);
				} else {
					currentTestItem.setTest(false);
				}
			}
		}
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
	}
	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (DBG) Log.d(TAG, "endElement: uri=" + uri +
            "; localName=" + localName +
           	"; qName=" + qName);
		if(qName!=null&&qName.equals("test_item")){
				list.add(currentTestItem);
		}
	}
	
	public void endDocument() throws SAXException {
		super.endDocument();
	}
}
