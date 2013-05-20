package com.viralrain.dns;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Lookup extends SwingWorker<Void, Void> {
    String[] curAddresses = new String[0];

    @Override
    protected Void doInBackground() throws Exception {
        Gui.lookup.setEnabled( false );

        String domain = Gui.hostname.getText();
        Gui.status.setText( "Scanning" );

        int p = 0;

        for ( String sub : Gui.subs ) {
            try{
                curAddresses = DNSLookup( sub + '.' + domain );
                p++;
            }catch( UnknownHostException he ){
                p++;
                curAddresses = new String[0];
                setProgress( p );
                continue;
            }
            System.out.println( sub + '.' + domain );
            for( int i = 0; i < curAddresses.length; i++ ){
                Gui.output.append(curAddresses[i] + "\n");
            }
            curAddresses = curAddresses;

            setProgress( p );
        }
        return null;
    }

    @Override
    protected void done(){
        Toolkit.getDefaultToolkit().beep();
        Gui.lookup.setEnabled( true );
        Gui.status.setText( "Done" );
        Gui.output.append( "----- Finished! -----\n\n" );
    }

    protected String[] getCurAddress(){
        return this.curAddresses;
    }

    public static String[] DNSLookup( String hostname ) throws UnknownHostException{
        String[] output = new String[0];
        InetAddress[] addresses = InetAddress.getAllByName( hostname );
        output = new String[ addresses.length ];
        for( int i = 0; i < addresses.length; i++ ){
            output[i] = addresses[i].toString();
        }
        return output;
    }
}
