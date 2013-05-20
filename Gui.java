package com.viralrain.dns;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Gui extends JFrame{

    public static JTextField hostname;      // input text field
    public static JTextArea output;         // output text area
    public static JLabel status;
    public static JProgressBar progress;
    public static JButton lookup;

    public static String[] subs = FileIO.getList( "subs.txt" );

    protected Lookup task;

    public Gui(){
        super("DNS Enum V1 - Charles Smith");

        // setup input panel, with padding, grid layout
        JPanel input = new JPanel();
        input.setBorder( BorderFactory.createMatteBorder( 0, 0, 5, 0, this.getBackground() ) );
        input.setLayout( new GridLayout( 1, 2, 5, 5 ) );
            JLabel inLbl = new JLabel( "Host Name" );
            hostname = new JTextField();                // input text field
            hostname.setToolTipText("Test");
        input.add(inLbl);
        input.add( hostname );

        // output text area
        output = new JTextArea();
        JScrollPane jsp = new JScrollPane(output);
        jsp.setBorder( BorderFactory.createLineBorder( Color.lightGray ) );



        // setup buttons panel, with padding, grid layout
        JPanel buttons = new JPanel();
        buttons.setBorder( BorderFactory.createMatteBorder( 5, 0, 0, 0, this.getBackground() ) );
        buttons.setLayout( new GridLayout( 1, 2, 5, 5 ) );
            lookup = new JButton( "Lookup" );
            lookup.addActionListener( new LookupListener() );
            JButton clear = new JButton( "Clear" );
            clear.addActionListener( new ClearListener() );
        buttons.add( lookup );
        buttons.add( clear );

        JPanel statusBar = new JPanel();
        statusBar.setLayout(new GridLayout(1, 2, 5, 5));
        statusBar.setPreferredSize(new Dimension(100, 23));
        statusBar.setBorder( BorderFactory.createCompoundBorder(
                    BorderFactory.createLoweredSoftBevelBorder(),
                    BorderFactory.createMatteBorder( 2, 2, 2, 2, this.getBackground() )
                ));
            status = new JLabel( "Idle" );
            progress = new JProgressBar( 0, subs.length );
            progress.setStringPainted( true );
            progress.setMaximum( subs.length );
        statusBar.add( status );
        statusBar.add(progress);

        // main layout panel, padding, with border layout
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, this.getBackground()));
        main.add( input, BorderLayout.NORTH );
        main.add( jsp, BorderLayout.CENTER );
        main.add( buttons, BorderLayout.SOUTH );

        this.setLayout( new BorderLayout() );
        this.add( main, BorderLayout.CENTER );
        this.add( statusBar, BorderLayout.SOUTH );
        this.setSize( 350, 500 );
        this.setLocationRelativeTo( null );
    }

    // click listener for the lookup button
    private class LookupListener implements ActionListener, PropertyChangeListener {
        @Override
        public void actionPerformed( ActionEvent e ) {
            task = new Lookup();
            output.append( "----- Looking up hosts on: " + hostname.getText() + " -----\n" );
            task.addPropertyChangeListener(this);
            task.execute();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals("progress") ){
                int p = (Integer)evt.getNewValue();
                progress.setValue( p );
            }
        }
    }

    // click listener for the clear button
    private class ClearListener implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent e ) {
            // clear the output and input texts
            hostname.setText( "" );
            output.setText( "" );
            progress.setValue(0);
            status.setText("Idle");
        }
    }
}