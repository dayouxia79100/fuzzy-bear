package com.wiley.fordummies.androidsdk.tictactoe;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LocationKMLHandler extends DefaultHandler{
    private boolean inLocalityName = false;
    private boolean inAddress = false;
    private boolean finishedLocality=false, finishedAddress = false;
    private StringBuilder localityBuilder, addressBuilder;
    private String localityName=null;
    private String address=null;
   
    public String getLocalityName(){
        return this.localityName;
    }
    
    public String getAddress(){
        return this.address;
    }
   
    @Override
    public void characters(char[] ch, int start, int length)
           throws SAXException {
        super.characters(ch, start, length);
        if (this.inLocalityName && !this.finishedLocality){
            if ((ch[start] != '\n') && (ch[start] != ' ')){
            	localityBuilder.append(ch, start, length);
            }
        }
        if (this.inAddress && !this.finishedAddress){
            if ((ch[start] != '\n') && (ch[start] != ' ')){
                addressBuilder.append(ch, start, length);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException
    {
        super.endElement(uri, localName, name);
        if (!this.finishedLocality){
            if (localName.equalsIgnoreCase("LocalityName")){
                this.localityName = localityBuilder.toString();
                this.finishedLocality = true;
            }
            if (localityBuilder != null){
            	localityBuilder.setLength(0);
            }
        }
        if (!this.finishedAddress){
            if (localName.equalsIgnoreCase("Address")){
                this.address = addressBuilder.toString();
                this.finishedAddress = true;
            }
            if (addressBuilder != null){
            	addressBuilder.setLength(0);
            }
        }
    }

    @Override
    public void startDocument() throws SAXException{
        super.startDocument();
        localityBuilder = new StringBuilder();
        addressBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException{
        super.startElement(uri, localName, name, attributes);
        if (localName.equalsIgnoreCase("LocalityName")){
            this.inLocalityName = true;
        }
        if (localName.equalsIgnoreCase("Address")){
            this.inAddress = true;
        }
    }
}