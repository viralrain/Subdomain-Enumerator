package com.viralrain.dns;

import java.io.*;
import java.util.*;

public class FileIO {
    public static String[] getList( String filename ){

        Scanner sc = null;
        try {
            sc = new Scanner( new File( "subs.txt" ) );
        } catch (FileNotFoundException e) {
            System.out.println( "Error opening file" );
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        ArrayList<String> list = new ArrayList<String>();

        String s = "";
        while( sc.hasNextLine() ){
            s = sc.nextLine();
            list.add( s );
        }

        String[] subs = new String[ list.size() ];

        for( int i = 0; i < list.size(); i++ ){
            subs[i] = list.get( i );
        }

        return subs;
    }
    public static void putList( File filename ){

    }
}
