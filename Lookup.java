package com.viralrain.dns;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

// class: Lookup
// purpose: thread for looking up DNS hosts
public class Lookup extends SwingWorker<Void, Void> {
    String[] curAddresses = new String[0];

    // doInBackground()
    // main method for thread
    @Override
    protected Void doInBackground() throws Exception {
        // disable the start button
        Gui.lookup.setEnabled( false );

        // get the domain from GUI and change
        // the status text
        String domain = Gui.hostname.getText();
        Gui.status.setText( "Scanning" );

        // set up the progress bar
        int p = 0;
        double max = Gui.subs.length;

        SubDomain subd = new SubDomain("");

        // for each subdomain in the list
        for ( String sub : Gui.subs ) {
            p++;
            // lookup the current sub-domain
            try{
                curAddresses = DNSLookup( sub + '.' + domain );
                subd = new SubDomain( sub + '.' + domain );
                Gui.outputSubsList.addElement( subd );
            }catch( UnknownHostException he ){
                curAddresses = new String[0];
            }

            // output the found sub-domain addresses
            for( String cur : curAddresses ){
                cur = cur.substring( cur.lastIndexOf( '/' ) + 1 );
                subd.ipList.addElement( new IPs( cur ) );
            }
            // increment the progress bar
            setProgress( (int)((p / max)*100) );
        }
        return null;
    }

    // done()
    // when the thread is finished running do
    // some cleanup to the GUI and beep
    @Override
    protected void done(){
        Toolkit.getDefaultToolkit().beep();
        Gui.lookup.setEnabled(true);
        Gui.status.setText("Done");
    }

    // DNSLookup
    // inputs - String: hostname
    // output - string array holding all addresses
    //          associated with a host
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

class SubDomain{
    public DefaultListModel<IPs> ipList= new DefaultListModel<IPs>();
    public String sub;

    public SubDomain( String sub ){
        this.sub = sub;
    }

    public void addSub( IPs ip ){
        ipList.addElement( ip );
    }

    public String getSubName(){
        return this.sub;
    }
}

class IPs{
    String ip;

    public IPs( String ip ){
        this.ip = ip;
    }

    public String getIP(){
        return this.ip;
    }
}