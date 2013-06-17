/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mytest;

import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.Pages;
import org.pdfclown.documents.contents.composition.BlockComposer;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;

import org.pdfclown.files.File;
import org.pdfclown.files.SerializationModeEnum;
import org.pdfclown.objects.PdfReference;
import org.pdfclown.tools.PageManager;

import java.util.List;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.pdfclown.documents.contents.FontResources;
import org.pdfclown.documents.contents.composition.XAlignmentEnum;
import org.pdfclown.documents.contents.composition.YAlignmentEnum;
import org.pdfclown.documents.contents.fonts.Font;
import org.pdfclown.documents.contents.fonts.StandardType1Font;
import org.pdfclown.documents.contents.layers.LayerDefinition;
import org.pdfclown.objects.PdfName;
import org.pdfclown.tools.PageStamper;

/**
 *
 * @author yehster
 */
public class FileSplitter {
    protected File pdf_file;
    protected Document doc;
    protected Pages pages;
    public FileSplitter(String filename) throws Exception
    {
        try
        {
            pdf_file=new File(filename);
            doc = pdf_file.getDocument();
            pages = doc.getPages();
            System.out.println(pages.size());
            PageManager pm = new PageManager(doc);
            int[] splitIndexes = new int[pages.size()/2-1];
            for(int pageIdx=0; pageIdx<(pages.size()/2-1);pageIdx++)
            {
                splitIndexes[pageIdx]=(pageIdx+1)*2;
            }
            List<Document> splitDocuments = pm.split(splitIndexes);
            System.out.println(splitDocuments.size());
            for(int fileIdx=0;fileIdx<splitDocuments.size();fileIdx++)
            {
                Document cur_doc=splitDocuments.get(fileIdx);
                Page cur_page = cur_doc.getPages().get(0);

                PageStamper stamper = new PageStamper();
                stamper.setPage(cur_page);
                PrimitiveComposer composer = stamper.getForeground();
                String font_name=                "C:/Users/yehster/git/clown-code/main/res/samples/input/fonts" + java.io.File.separator + "TravelingTypewriter.otf";
                font_name= "D:/downloads/GildaDisplay-Regular.ttf";
                Font bodyFont = Font.get(
                cur_doc,
                font_name
                );
                composer.setFont(bodyFont,12);
                composer.beginLocalState();            
                /*
                if(fileIdx<7)
                {
                    composer.showText("Today's Date",new Point2D.Double(650, 287));
                    composer.showText("First Name",new Point2D.Double(620, 310));
                    composer.showText("Age",new Point2D.Double(620, 335));
                    composer.showText("Len",new Point2D.Double(620, 358));
                    composer.showText("Weight",new Point2D.Double(715, 358));
                    composer.showText("AptDate",new Point2D.Double(620, 413));
                    composer.showText("Time",new Point2D.Double(715, 413));                    
                }
                else
                {
                    composer.showText("Today's Date",new Point2D.Double(650, 285));
                    composer.showText("First Name",new Point2D.Double(620, 310));
                    composer.showText("Age",new Point2D.Double(620, 332));
                    composer.showText("Len",new Point2D.Double(620, 355));
                    composer.showText("Weight",new Point2D.Double(715, 355));
                    composer.showText("BMI",new Point2D.Double(740, 380));               
                    composer.showText("AptDate",new Point2D.Double(620, 425));
                    composer.showText("Time",new Point2D.Double(715, 425));                    
                    
                }
                */
                composer.end();
                composer.flush();
                stamper.flush();
                Integer position=filename.indexOf(".pdf");
                String newName=filename.substring(0, position)+"-"+fileIdx+".pdf";
                
                cur_doc.getFile().save(newName,SerializationModeEnum.Standard);
            }
            
        }
        catch(Exception e)
        {
            throw(e);
        }
    }
}
