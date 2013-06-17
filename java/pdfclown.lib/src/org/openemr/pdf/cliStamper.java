/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openemr.pdf;

/**
 *
 * @author yehster
 */
public class cliStamper {
    public static void main(String[] args)
  {
    try
    {
        String layout="C:/Users/yehster/git/clown-code/java/pdfclown.lib/src/GrowingUpEnglishYoungLayout.xml";
        String source="D:/downloads/GrowingUpHealthy-EN-0.pdf";
        String destination="D:/downloads/testout.pdf";
        if(args.length>=3)
        {
            source=args[0];
            layout=args[1];
            destination=args[2];
            System.out.println(source);
        }
        PDFStamper ps = new PDFStamper(source,layout);
        for(Integer idx=3;idx<args.length;idx++)
        {
            String data=args[idx];
            Integer delim=data.indexOf("|");
            String field=data.substring(0,delim);
            String text=data.substring(delim+1);
            ps.showField(field, text);
        }
        ps.save(destination);
    }
    catch(Exception e)
    {throw new RuntimeException("Whoops",e);}  }
}
