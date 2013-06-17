/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mytest;

/**
 *
 * @author yehster
 */
public class testmain {
    public static void main(String[] args)
  {
    try
    {
        FileSplitter fs = new FileSplitter("D:/downloads/GrowingUpHealthy-SP.pdf");
    }
    catch(Exception e)
    {throw new RuntimeException("Whoops",e);}  }
}
