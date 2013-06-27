/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openemr.pdf;

import org.pdfclown.files.File;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;
import org.pdfclown.documents.contents.fonts.Font;
import org.pdfclown.tools.PageStamper;

import org.pdfclown.files.SerializationModeEnum;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author yehster
 */
public class PDFStamper {
    
    protected File pdf_file;
    protected Document doc;
    protected org.w3c.dom.Document layout;
    protected PageStamper stamper;
    protected PrimitiveComposer foreground;
    protected Map<String,Point2D> fieldMap= new HashMap<String,Point2D>();
    protected Map<String,Integer> fieldFontSizes= new HashMap<String,Integer>();
    protected Map<String,String> fieldFontSuffixes= new HashMap<String,String>();
    
    protected Font bodyFont;
    public PDFStamper(String source,String layout_file) throws Exception
    {
        try
        {
            pdf_file=new File(source);
            doc = pdf_file.getDocument();          

            java.io.File layoutFile=new java.io.File(layout_file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            layout = dBuilder.parse(layoutFile);
            NodeList fields =layout.getElementsByTagName("field");
            for(Integer idx=0;idx<fields.getLength();idx++)
            {
                 NamedNodeMap attrs=fields.item(idx).getAttributes();
                 String id=attrs.getNamedItem("id").getNodeValue();
                 Float xpos=Float.parseFloat(attrs.getNamedItem("xpos").getNodeValue());
                 Float ypos=Float.parseFloat(attrs.getNamedItem("ypos").getNodeValue());
                 fieldMap.put(id,new Point2D.Float(xpos,ypos));
                 Node fontSize=attrs.getNamedItem("font-size");
                 if(fontSize!=null)
                 {
                     fieldFontSizes.put(id, Integer.parseInt(fontSize.getNodeValue()));
                 }
                 
                 Node suffix=attrs.getNamedItem("suffix");
                 if(suffix!=null)
                 {
                     fieldFontSuffixes.put(id,suffix.getNodeValue());
                 }
                         
            }
            
            stamper = new PageStamper();        
            stamper.setPage(doc.getPages().get(0));
            foreground=stamper.getForeground();
         
            String font_name=layout.getElementsByTagName("font").item(0).getAttributes().getNamedItem("filename").getNodeValue();
            bodyFont = Font.get(doc,font_name);
            foreground.setFont(bodyFont, 11);           
            foreground.beginLocalState();
            
        }
        catch(Exception e)
        {
            throw e;
        }

    }
    public boolean showField(String fieldName,String text)
    {
        try
        {
            Integer fontSize=fieldFontSizes.get(fieldName);
            if(fontSize==null)
            {
                foreground.setFont(bodyFont, 11);                       
            }
            else
            {
                foreground.setFont(bodyFont, fontSize);           
            }
            String suffix=fieldFontSuffixes.get(fieldName);
            if(suffix!=null)
            {
                text=text+suffix;
            }
            foreground.showText(text,fieldMap.get(fieldName));
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return false;
        }
    }
    public void save(String outputFile) throws Exception
    {
        try
        {
            foreground.end();
            foreground.flush();
            stamper.flush();
            doc.getFile().save(outputFile,SerializationModeEnum.Standard);
        }
        catch(Exception e)
        {
            throw e;
        }
    }
}
