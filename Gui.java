package com.viralrain.dns;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// class: Gui
// purpose: create the program's GUI
public class Gui extends JFrame{

    public static JTextField hostname;      // input text field
    public static JTextArea output;         // output text area
    public static DefaultListModel<SubDomain> outputSubsList = new DefaultListModel<SubDomain>();
    public static JList<SubDomain> outputSubs = new JList<SubDomain>(outputSubsList);
    public static DefaultListModel<IPs> outputAddrssList = new DefaultListModel<IPs>();
    public static JList<IPs> outputAddrss = new JList<IPs>(outputAddrssList);
    public static JLabel status;            // status label
    public static JProgressBar progress;    // progress bar
    public static JButton lookup;           // lookup button

    // get the list of sub-domains
    public static String[] subs = FileIO.getList( "subs-test.txt" );

    // create the window
    public Gui(){
        super("DNS Enum V1 - Charles Smith");

        // setup input panel, with padding, grid layout
        JPanel input = new JPanel();
        input.setBorder( BorderFactory.createMatteBorder( 0, 0, 5, 0, this.getBackground() ) );
        input.setLayout( new GridLayout( 1, 2, 5, 5 ) );
            JLabel inLbl = new JLabel( "Host Name" );
            hostname = new JTextField();                // input text field
            hostname.setToolTipText("Test");
            hostname.setBorder( BorderFactory.createLineBorder( Color.lightGray ));
        input.add(inLbl);
        input.add( hostname );

        // output subdomain list

        outputSubs.addListSelectionListener( new ListListener() );
        outputSubs.setCellRenderer( new SubListRenderer() );
        JScrollPane jsp1 = new JScrollPane( outputSubs );
        jsp1.setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 0, this.getBackground()),
                BorderFactory.createLineBorder(Color.lightGray)
        ));

        // output ip address list
        outputAddrss.setCellRenderer( new IPListRenderer() );
        JScrollPane jsp2 = new JScrollPane( outputAddrss );
        jsp2.setBorder( BorderFactory.createLineBorder( Color.lightGray ) );
        jsp2.setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, this.getBackground()),
                BorderFactory.createLineBorder(Color.lightGray)
        ));

        JPanel outputs = new JPanel();
        outputs.setLayout(new GridLayout(2, 1));
        outputs.add( jsp1 );
        outputs.add( jsp2 );

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

        // status bar with border, status label and progress bar
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new GridLayout(1, 2, 5, 5));
        statusBar.setPreferredSize(new Dimension(100, 23));
        statusBar.setBorder( BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.lightGray),
                    BorderFactory.createMatteBorder( 2, 2, 2, 2, this.getBackground() )
                ));
            status = new JLabel( "Idle" );
            progress = new JProgressBar( 0, 100 );
            progress.setStringPainted(true);
        statusBar.add( status );
        statusBar.add(progress);

        // main layout panel, padding, with border layout
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, this.getBackground()));
        main.add( input, BorderLayout.NORTH );
        main.add( outputs, BorderLayout.CENTER );
        main.add( buttons, BorderLayout.SOUTH );

        JMenuBar topMenu = new JMenuBar();

        JMenu fileMenu = new JMenu( "File" );
            fileMenu.add( makeMenuItem("Exit") );

        JMenu editMenu = new JMenu( "Edit" );
            editMenu.add( makeMenuItem( "Clear" ) );

        topMenu.add(fileMenu);
        topMenu.add(editMenu);
        this.setJMenuBar( topMenu );


        // setup window options and add stuff to window
        this.setLayout(new BorderLayout());
        this.add( main, BorderLayout.CENTER );
        this.add( statusBar, BorderLayout.SOUTH );
        this.setSize( 350, 500 );
        this.setLocationRelativeTo( null );
    }

    private JMenuItem makeMenuItem(String name){
        JMenuItem m = new JMenuItem( name );
        m.addActionListener( new MenuListener() );
        return m;
    }

    private void clearGUI(){
        // clear the output and input texts
        hostname.setText( "" );
        outputAddrssList.removeAllElements();
        outputAddrss.removeAll();
        outputSubsList.removeAllElements();
        outputSubsList.clear();
        outputSubs.removeAll();
        progress.setValue(0);
        status.setText("Idle");
    }

    // click listener for the lookup button
    private class LookupListener implements ActionListener, PropertyChangeListener {
        @Override
        public void actionPerformed( ActionEvent e ) {
            // create new task for DNS lookups
            Lookup task = new Lookup();
            // reset progress bar
            progress.setValue( 0 );

            // add listener for thread and execute the task
            task.addPropertyChangeListener(this);
            task.execute();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // when the thread updates the progress
            // update the progress bar
            if(evt.getPropertyName().equals("progress") ){
                int p = (Integer)evt.getNewValue();
                progress.setValue( p );
            }
        }
    }

    private class ListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            if( !outputSubs.isSelectionEmpty() ){
                int cur = outputSubs.getSelectedIndex();
                outputAddrssList.clear();
                outputAddrss.removeAll();
                for( int i = 0; i < outputSubsList.get( cur ).ipList.getSize(); i++ ){
                    outputAddrssList.addElement( outputSubsList.get( cur ).ipList.get( i ) );
                }
            }
        }
    }

    // click listener for the clear button
    private class ClearListener implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent e ) {
            clearGUI();
        }
    }

    private class MenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if( e.getActionCommand().equals("Clear")){
            clearGUI();
            }
            if( e.getActionCommand().equals("Exit")){
                System.exit(0);
            }
        }
    }
    private class SubListRenderer extends DefaultListCellRenderer{
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean iss,
                boolean chf
        ){
            super.getListCellRendererComponent(list, value, index, iss, chf);

            SubDomain subd = (SubDomain)value;
            setText( subd.getSubName() );

            return this;
        }
    }

    private class IPListRenderer extends DefaultListCellRenderer{
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean iss,
                boolean chf
        ){
            super.getListCellRendererComponent(list, value, index, iss, chf);

            IPs ip = (IPs)value;
            setText( ip.getIP() );

            return this;
        }
    }
}