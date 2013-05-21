package com.viralrain.dns;

import java.io.*;
import java.util.*;
import java.net.*;

// Class: FileIO
// Purpose: provides ability to read lines from a file
//          and return them in a string array
//          soon to allow output to file as well
public class FileIO {

    // getList
    // read lines from a file and return the lines as a
    // array of strings
    // input - String: filename
    // output - string array holding each line of the file
    public static String[] getList( String filename ){

        // create scanner for reading from file
        Scanner sc = null;
        URL url = FileIO.class.getResource( filename );
        File location = new File( url.getPath() );

        // try to open the file
        try {
            sc = new Scanner( location );
        } catch (FileNotFoundException e) {
            System.out.println( "Error opening file" );
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // put each line into arraylist
        ArrayList<String> list = new ArrayList<String>();
        String s = "";
        while( sc.hasNextLine() ){
            s = sc.nextLine();
            list.add( s );
        }

        // put lines from arraylist into string array
        String[] subs = new String[ list.size() ];
        for( int i = 0; i < list.size(); i++ ){
            subs[i] = list.get( i );
        }

        // return array with lines of file
        return subs;
    }

    // putList
    // input - String: filename
    public static void putList( String filename ){

    }
}
