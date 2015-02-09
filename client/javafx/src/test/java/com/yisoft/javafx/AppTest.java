package com.yisoft.javafx;

import java.util.Arrays;

import javafx.geometry.Rectangle2D;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testRand(){
    	double c=0.9999d;
    	assertEquals(8, (int)(c*9));
    			
    }
    
    public void testArray(){
    	Rectangle2D[][] b= new Rectangle2D[2][3];
    	System.out.println(Arrays.toString(b[0]));
    	
    	int[][] a= new int[2][3];
    	System.out.println(Arrays.toString(a[0]));
    	
    	for(int i=0;i<2;i++)
    		for(int j=0;j<3;j++)
    			a[i][j]=1;
    }
}
