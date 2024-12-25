package jav;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Flow;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.sound.sampled.*;
import java.net.*;

public class NovaOS {
    private JFrame frame;
    private JDesktopPane desktopPane;
    private JPanel taskbar;
    private JPopupMenu startMenu;
    private boolean isRollingBall; // True for Windows 10 rolling balls, false for Windows XP loading bar

    Map<String, String> ssc = Map.of(
        "setting", "option.json"
    );

    public NovaOS() {
        selectLoadingBarType();
        createStartupScreen();
    }

    private void selectLoadingBarType() {
        System.err.println("Nah I Dont Use This Part");
    }

    private void createStartupScreen() {
        frame = new JFrame("NovaOS - Startup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Set black background
        frame.getContentPane().setBackground(Color.BLACK);

        // Icon label
        JLabel iconLabel = new JLabel(new ImageIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\icon.png"));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(iconLabel, BorderLayout.CENTER);

        // Loading bar panel
        JPanel loadingBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loadingBarPanel.setOpaque(false);
        JComponent loadingBar = createLoadingBar();
        loadingBarPanel.add(loadingBar);
        frame.add(loadingBarPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        new Timer(50, new ActionListener() {
            int value = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (loadingBar instanceof JProgressBar) {
                    ((JProgressBar) loadingBar).setValue(++value);
                }
                if (value >= 100) {
                    ((Timer) e.getSource()).stop();
                    frame.dispose();
                    createMainOS();
                }
            }
        }).start();
    }

    private JComponent createLoadingBar() {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(200, 20));
        progressBar.setForeground(new Color(30, 144, 255)); // XP-like Blue
        return progressBar;
    }

    private void createMainOS() {
        JFrame mainFrame = new JFrame("NovaOS - Main");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1366, 768);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());

        // Set black background
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.BLACK);
        mainFrame.add(desktopPane, BorderLayout.CENTER);

        // Taskbar
        taskbar = new JPanel();
        taskbar.setBackground(Color.WHITE);
        taskbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        mainFrame.add(taskbar, BorderLayout.SOUTH);

        // Start menu button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> toggleStartMenu(startButton));
        taskbar.add(startButton);

        initializeStartMenu();
        addDesktopShortcuts();
        mainFrame.setVisible(true);
    }

    private void initializeStartMenu() {
        startMenu = new JPopupMenu();

        JMenu programMenu = new JMenu("Programs");

        JMenuItem thisPC = new JMenuItem("This PC");
        thisPC.addActionListener(e -> openThisPC());
        programMenu.add(thisPC);

        JMenuItem serverApp = new JMenuItem("Server");
        serverApp.addActionListener(e -> openApp("Server"));
        programMenu.add(serverApp);

        JMenuItem mscPlr = new JMenuItem("Music Player");
        mscPlr.addActionListener(e -> mscPLRapp());
        programMenu.add(mscPlr);

        startMenu.add(programMenu);

        JMenuItem shutdownItem = new JMenuItem("Shutdown");
        shutdownItem.addActionListener(e -> shutdownSequence());
        startMenu.addSeparator();
        startMenu.add(shutdownItem);
        taskbarAdd();
    }

    private void toggleStartMenu(JButton startButton) {
        if (startMenu.isVisible()) {
            startMenu.setVisible(false);
        } else {
            startMenu.show(startButton, 0, -startMenu.getPreferredSize().height);
        }
    }

    private void addDesktopShortcuts() {
        int yOffset = 50; // Initial vertical offset
        int xOffset = 50; // Horizontal offset for all shortcuts
        int shortcutHeight = 90; // Height of each shortcut, including spacing
    
        JButton thisPC = new JButton("This PC", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\blehbleh.png", 50, 50));
        thisPC.setVerticalTextPosition(SwingConstants.BOTTOM);
        thisPC.setHorizontalTextPosition(SwingConstants.CENTER);
        thisPC.addActionListener(e -> openThisPC());
        thisPC.setBackground(Color.BLACK);
        thisPC.setForeground(Color.WHITE);
        thisPC.setFocusPainted(false);
        thisPC.setBorder(null);
        thisPC.setBounds(xOffset, yOffset, 70, shortcutHeight);
        desktopPane.add(thisPC);
    
        yOffset += shortcutHeight + 10; // Increment offset for the next button
    
        JButton server = new JButton("Server", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\app_icon1.png", 50, 50));
        server.setVerticalTextPosition(SwingConstants.BOTTOM);
        server.setHorizontalTextPosition(SwingConstants.CENTER);
        server.addActionListener(e -> openApp("Server"));
        server.setBackground(Color.BLACK);
        server.setForeground(Color.WHITE);
        server.setFocusPainted(false);
        server.setBorder(null);
        server.setBounds(xOffset, yOffset, 70, shortcutHeight);
        desktopPane.add(server);
    
        yOffset += shortcutHeight + 10;
    
        JButton mscPlr = new JButton("Music Player", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\nvsmp.png", 50, 50));
        mscPlr.setVerticalTextPosition(SwingConstants.BOTTOM);
        mscPlr.setHorizontalTextPosition(SwingConstants.CENTER);
        mscPlr.addActionListener(e -> mscPLRapp());
        mscPlr.setBackground(Color.BLACK);
        mscPlr.setForeground(Color.WHITE);
        mscPlr.setFocusPainted(false);
        mscPlr.setBorder(null);
        mscPlr.setBounds(xOffset, yOffset, 70, shortcutHeight);
        desktopPane.add(mscPlr);

        yOffset += shortcutHeight + 10;

        JButton internet = new JButton("Internet Explorer", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\intrbrws.png", 50, 50));
        internet.setVerticalTextPosition(SwingConstants.BOTTOM);
        internet.setHorizontalTextPosition(SwingConstants.CENTER);
        internet.addActionListener(e -> internetBrw());
        internet.setBackground(Color.BLACK);
        internet.setForeground(Color.WHITE);
        internet.setFocusPainted(false);
        internet.setBorder(null);
        internet.setBounds(xOffset, yOffset, 70, shortcutHeight);
        desktopPane.add(internet);

        yOffset += shortcutHeight + 10;

        JButton SFXPlayer = new JButton("SFX Player", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\nvsmp.png", 50, 50));
        SFXPlayer.setVerticalTextPosition(SwingConstants.BOTTOM);
        SFXPlayer.setHorizontalTextPosition(SwingConstants.CENTER);
        SFXPlayer.addActionListener(e -> sfxPLRapp());
        SFXPlayer.setBackground(Color.BLACK);
        SFXPlayer.setForeground(Color.WHITE);
        SFXPlayer.setFocusPainted(false);
        SFXPlayer.setBorder(null);
        SFXPlayer.setBounds(xOffset, yOffset, 70, shortcutHeight);
        desktopPane.add(SFXPlayer);

        // Welcome Info, Dont Put Button After This Line
        JLabel info = new JLabel("Welcome To NovaOS v6.1!");
        taskbar.add(info);
    }    

    private ImageIcon scaleIcon(String iconPath, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error loading or scaling icon: " + iconPath);
            e.printStackTrace();
            return null; // Return null if the icon fails to load
        }
    }
    
       
    private void addShortcut(String name, String iconPath, ActionListener action) {
        JButton shortcut = new JButton(name, new ImageIcon(iconPath));
        shortcut.setVerticalTextPosition(SwingConstants.BOTTOM);
        shortcut.setHorizontalTextPosition(SwingConstants.CENTER);
        shortcut.addActionListener(action);
        shortcut.setBackground(Color.BLACK);
        shortcut.setForeground(Color.WHITE);
        shortcut.setFocusPainted(false);
        shortcut.setBorder(null);
        shortcut.setBounds(50, 50 + desktopPane.getComponentCount() * 70, 70, 70);
        desktopPane.add(shortcut);
    }

    private void openThisPC() {
        JInternalFrame explorerFrame = new JInternalFrame("This PC", true, true, true, true);
        explorerFrame.setSize(400, 300);
        explorerFrame.setVisible(true);

        JPanel explorerPanel = new JPanel();
        explorerPanel.setLayout(new GridLayout(0, 1));
        explorerPanel.setBackground(Color.WHITE);

        File[] drives = File.listRoots();
        for (File drive : drives) {
            JLabel driveLabel = new JLabel(drive.getAbsolutePath());
            driveLabel.setIcon(new ImageIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\disk_icon.png"));
            explorerPanel.add(driveLabel);
        }

        JScrollPane scrollPane = new JScrollPane(explorerPanel);
        explorerFrame.add(scrollPane);

        desktopPane.add(explorerFrame);
        try {
            explorerFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    private void internetBrw() {
        JInternalFrame intrbrw = new JInternalFrame("Internet Explorer", true, true, true, true);
        intrbrw.setSize(400, 300);
        intrbrw.setVisible(true);

        JPanel intrContent = new JPanel();
        intrContent.setBackground(Color.GRAY);
        intrbrw.add(intrContent);

        JLabel notifier = new JLabel("Internet Explorer Is Currently In Development");
        notifier.setVisible(true);
        intrContent.add(notifier);

        desktopPane.add(intrbrw);
    }

    private void mscPLRapp() {
        JInternalFrame mscplrs = new JInternalFrame("NovaOS Music Player", true, true, true, true);
        mscplrs.setSize(400, 300);
        mscplrs.setVisible(true);

        JInternalFrame Utilities = new JInternalFrame("Music Player Utils.");
        Utilities.setSize(400, 300);
        Utilities.setVisible(true);

        JPanel utilsPanel = new JPanel();
        utilsPanel.setLayout(new GridLayout(0, 1));
        utilsPanel.setBackground(Color.GRAY);
        Utilities.add(utilsPanel);

        JPanel mscPLRpane = new JPanel();
        mscPLRpane.setLayout(new GridLayout(0, 1));
        mscPLRpane.setBackground(Color.GRAY);
        mscplrs.add(mscPLRpane);

        // Songs map (song name to file path)
        Map<String, String> songs = new HashMap<>();
        songs.put("Her Smile", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\HerSmile.wav");
        songs.put("Time Back Bad Style", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\TimeBack.wav");
        songs.put("Monody", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Monody.wav");
        songs.put("Summer Time", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\SummerTime.wav");
        songs.put("Threatning Domain", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\ThreatningDomain.wav");
        songs.put("Memory Reboot", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\MemoryReboot.wav");
        songs.put("God Only Khows", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\GodOnlyKhow.wav");
        songs.put("PLUH", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Pluh.wav");
        songs.put("Kerosene", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Kerosene.wav");
        songs.put("Unkhown Source", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\ET.wav");
        songs.put("My Ordinary Life", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\MOL.wav");
        songs.put("Sigma Boy", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\SigmaBoi.wav");
        songs.put("Genoxensis", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\OUTRO.wav");
        songs.put("Stronger", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Stronger.wav");
        songs.put("Untrust Us", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\UU.wav");
        songs.put("Clarity", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Clarity.wav");
        songs.put("Centuries", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Centuries.wav");
        songs.put("The Greatest Strategize", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\2005.wav");
        songs.put("EMPIRE", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Empire.wav");
        songs.put("AURA", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\Aura.wav");



        // Music handling logic
        final boolean[] isPlaying = {false};
        final Clip[] currentClip = {null};
        final String[] currentSong = {null};

        for (Map.Entry<String, String> songEntry : songs.entrySet()) {
            JButton songButton = new JButton(songEntry.getKey());
            mscPLRpane.add(songButton);

            songButton.addActionListener(e -> {
                String selectedSong = songEntry.getKey();
                String musicFile = songEntry.getValue();

                if (isPlaying[0] && selectedSong.equals(currentSong[0])) {
                    System.out.println("You Cannot Change Song Now");
                } else {
                    try {
                        if (currentClip[0] != null) {
                            currentClip[0].close();
                        }

                        // Load and play the selected song
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFile));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        currentClip[0] = clip;
                        currentSong[0] = selectedSong;

                        // Start playing the music
                        clip.start();
                        isPlaying[0] = true;

                        // Add a listener to reset state when the music ends
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                clip.close();
                                isPlaying[0] = false;
                                currentSong[0] = null;
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        
        desktopPane.add(mscplrs);
    }

    private void sfxPLRapp() {
        JInternalFrame mscplrs = new JInternalFrame("NovaOS SFX Player", true, true, true, true);
        mscplrs.setSize(400, 300);
        mscplrs.setVisible(true);

        JInternalFrame Utilities = new JInternalFrame("SFX Player Utils.");
        Utilities.setSize(400, 300);
        Utilities.setVisible(true);

        JPanel utilsPanel = new JPanel();
        utilsPanel.setLayout(new GridLayout(0, 1));
        utilsPanel.setBackground(Color.GRAY);
        Utilities.add(utilsPanel);

        JPanel mscPLRpane = new JPanel();
        mscPLRpane.setLayout(new GridLayout(0, 1));
        mscPLRpane.setBackground(Color.GRAY);
        mscplrs.add(mscPLRpane);

        // Songs map (song name to file path)
        Map<String, String> songs = new HashMap<>();
        songs.put("Fire In The Hole!", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\FITH.wav");
        songs.put("Vine Boom", "C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\VB.wav");



        // Music handling logic
        final boolean[] isPlaying = {false};
        final Clip[] currentClip = {null};
        final String[] currentSong = {null};

        for (Map.Entry<String, String> songEntry : songs.entrySet()) {
            JButton songButton = new JButton(songEntry.getKey());
            mscPLRpane.add(songButton);

            songButton.addActionListener(e -> {
                String selectedSong = songEntry.getKey();
                String musicFile = songEntry.getValue();

                if (isPlaying[0] && selectedSong.equals(currentSong[0])) {
                    System.out.println("You Cannot Change Song Now");
                } else {
                    try {
                        if (currentClip[0] != null) {
                            currentClip[0].close();
                        }

                        // Load and play the selected song
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFile));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        currentClip[0] = clip;
                        currentSong[0] = selectedSong;

                        // Start playing the music
                        clip.start();
                        isPlaying[0] = true;

                        // Add a listener to reset state when the music ends
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                clip.close();
                                isPlaying[0] = false;
                                currentSong[0] = null;
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        
        desktopPane.add(mscplrs);
    }    

    private void openApp(String appName) {
        JInternalFrame appFrame = new JInternalFrame(appName, true, true, true, true);
        appFrame.setSize(400, 300);
        appFrame.setLayout(new BorderLayout());
        appFrame.setVisible(true);
    
        if (appName.equals("Server")) {
            JPanel serverPanel = new JPanel();
            serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.Y_AXIS)); // Stack buttons vertically
    
            // Add space at the top
            serverPanel.add(Box.createVerticalGlue());
    
            // Server_Rack1 button
            JButton serverRack1 = new JButton("Server_Rack1", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\app_icon1.png", 20, 20));
            serverRack1.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment within the BoxLayout
            serverRack1.addActionListener(e -> showError("This Java has no internet rip"));
            serverPanel.add(serverRack1);
            serverPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between buttons
    
            // Network button
            JButton network = new JButton("Network", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\app_icon1.png", 20, 20));
            network.setAlignmentX(Component.CENTER_ALIGNMENT);
            network.addActionListener(e -> showError("This Java has no internet rip"));
            serverPanel.add(network);
            serverPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            JButton ProtogenNet = new JButton("Protogen Network Node", scaleIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\app_icon1.png", 20, 20));
            ProtogenNet.setAlignmentX(Component.CENTER_ALIGNMENT);
            ProtogenNet.addActionListener(e -> showError("Could Not Connect To Protogen Network Node Via Ethernet Nor Wireless"));
            serverPanel.add(ProtogenNet);
            serverPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Add space at the bottom
            serverPanel.add(Box.createVerticalGlue());
    
            // Add the panel to the right side of the app frame
            appFrame.add(serverPanel, BorderLayout.WEST);
        }
    
        desktopPane.add(appFrame);
        try {
            appFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }    
    
    private void showError(String message) {
        JInternalFrame errorFrame = new JInternalFrame("Error", false, false, false, false);
        errorFrame.setSize(300, 150);
        errorFrame.setLocation(50, 50); // Adjust location as needed
        errorFrame.setLayout(new BorderLayout());
    
        JLabel errorLabel = new JLabel(message, SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorFrame.add(errorLabel, BorderLayout.CENTER);
    
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> desktopPane.remove(errorFrame));
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);
        errorFrame.add(closeButton, BorderLayout.SOUTH);
    
        errorFrame.setVisible(true);
        desktopPane.add(errorFrame);
    
        try {
            errorFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }
    


    private void shutdownSequence() {
        JInternalFrame shutdownFrame = new JInternalFrame("Shutting Down", false, false, false, false);
        shutdownFrame.setSize(500, 400);
        shutdownFrame.setLocation(150, 100);
        shutdownFrame.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(new ImageIcon("C:\\Luas-Backup\\Luas\\jav\\app\\src\\main\\java\\jav\\resource\\icon.png"));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shutdownFrame.add(iconLabel, BorderLayout.CENTER);

        JPanel loadingBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loadingBarPanel.setOpaque(false);
        JComponent loadingBar = createLoadingBar();
        loadingBarPanel.add(loadingBar);
        shutdownFrame.add(loadingBarPanel, BorderLayout.SOUTH);

        desktopPane.add(shutdownFrame);
        shutdownFrame.setVisible(true);

        new Timer(50, new ActionListener() {
            int value = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (loadingBar instanceof JProgressBar) {
                    ((JProgressBar) loadingBar).setValue(++value);
                }
                if (value >= 100) {
                    ((Timer) e.getSource()).stop();
                    System.exit(0);
                }
            }
        }).start();
    }

    private void taskbarAdd() {
        JButton Explorer = new JButton("This PC");
        try {
            Explorer.setBounds(null);
        } catch (NullPointerException errs) {
            errs.printStackTrace();
            System.out.println("Oopsie");
        }

        Explorer.setSize(20, 50);
        Explorer.addActionListener(e -> openThisPC());
        Explorer.addActionListener(e -> advSetText(Explorer, "Bleh Bleh Cat"));
        taskbar.add(Explorer);

        JButton Servers = new JButton("Server");
        try {
            Servers.setBounds(null);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Oopsie");
        }

        Servers.setSize(20, 50);
        Servers.addActionListener(e -> openApp("Server"));
        Servers.addActionListener(e -> advSetText(Servers, "BLEH BLEH CAT"));
        taskbar.add(Servers);

        JButton uhhh1 = new JButton("NovaOS Music Player");
        try {
            uhhh1.setBounds(null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        uhhh1.setSize(20, 50);
        uhhh1.addActionListener(e -> mscPLRapp());
        taskbar.add(uhhh1);
    }

    private void advSetText(JButton any, String text) {
        System.err.println("Attention! This Method Is Only Functional To JButton!");
        any.setText(text);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NovaOS::new);
    }
}
