package main;

import java.awt.BorderLayout;   import java.awt.Color;              import java.awt.Container;      
import java.awt.Dimension;      import java.awt.FlowLayout;         import java.awt.Font;       
import java.awt.Graphics2D;
import java.awt.GridLayout;     import java.awt.event.ActionEvent;                               import javax.swing.BorderFactory;
import java.util.Random;        import java.util.Timer;             import java.util.TimerTask;
import javax.swing.JFrame;      import javax.swing.JTextField;      import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;      import javax.swing.JProgressBar;    import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


class GUI extends JFrame{
    
    Container c; JButton btTeste = new JButton("?"), btPlus = new JButton("+");
    int[][] skmap = new int[1][1]; boolean condx, condy;
    boolean[][] sksponge = SkMap.sponge(9, 9);
    
    String skTeste = ""; char rngId = "abcdefghijklmnopqrstuvwxyz".charAt(new Random().nextInt(26));
    
    Color[] cores = {
        Color.BLACK, Color.yellow,  Color.red,  Color.BLUE,  Color.orange,
        Color.GREEN, Color.MAGENTA, Color.gray, Color.yellow,Color.red
    };
    
    JPanel south = new JPanel(), center = new JPanel( new GridLayout(9, 9) );
    
    Border normal_border = BorderFactory .createEmptyBorder(),
           mark_border   = BorderFactory .createEtchedBorder(EtchedBorder.RAISED),//.createLineBorder(Color.black, 2),
           bad_border    = BorderFactory .createLineBorder(Color.red, 2);
    
    JProgressBar time_bar = new JProgressBar();
    
    JLabel msg = new JLabel();
    
    Timer timer = new Timer();
    
    int total_time = 360, time_left = 360, tests_left = 5;
    
    boolean won = false;
    
    private void bg(Color color){
        
        south .setBackground( color );
        center.setBackground( color );
        
        btTeste .setBackground( color );
        btPlus  .setBackground( color );
        msg     .setBackground( color );
        
    }
    
    private void fg(Color color){
        
        btTeste .setForeground( color );
        btPlus  .setForeground( color );
        msg     .setForeground( color );
        
    }
    
    void finish(){
        
        String nums = "";
        
        for (int n = 0; n < 81; n++){
                    
            JTextField jT = (JTextField) center .getComponent(n);
            jT .setBorder( normal_border );
            jT .setEnabled(false);
            
            if (jT .getText().trim().isEmpty() ) { nums += "0"; } 
                                      else { nums += jT .getText(); }
            
            won = nums .equals(skTeste);
                        
        }
        
        bg( Color.white );
        btTeste .setEnabled(false);
        btPlus  .setForeground(Color.blue);
        
        msg .setText( won ? "Ganhou!" : "Perdeu!" );
        msg .setForeground( won ? Color.BLUE : Color.RED );
        msg .setVisible(true);
        
    }
    
    void stop(){
        
        timer.cancel(); timer.purge();
        
    }
    
    private void setBtTesteText(){
        
        if (tests_left > 0){
            
            btTeste .setText( "" + tests_left + "?" );
            
        } else {
            
            btTeste .setText("Ω");
            btTeste .setPreferredSize( new Dimension(20, 20) );
            
        }
        
    }
    
    public GUI(){
        
        c = getContentPane();   c .setLayout( new BorderLayout() );     setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        c .add( south, BorderLayout .SOUTH ); c .add( center, BorderLayout .CENTER );
        
        bg( Color.white );
        
        Font font = new Font("Courier New", Font.BOLD, 25);
        
        while (skmap .length == 1){ skmap = SkMap.gen(9, 9, 1, 9); }
         
        // CRIAR CÉLULAS // =======================================================================================#
         
        for (int i = 0; i < 9; i++){   for (int j = 0; j < 9; j++){
            
            JTextField jT = new JTextField();
             
            jT .setHorizontalAlignment(JTextField.CENTER);  jT .setForeground(cores[skmap[i][j]]);
            jT .setBorder( normal_border );                 jT .setFont(font);
             
            if (sksponge[i][j]){ jT .setText(""+skmap[i][j]); jT .setEditable(false); } 
                          else { jT .setForeground(Color.BLACK); }
             
            condx = ( ((int)(i/3))%2 ) == 1; condy = ( ((int)(j/3))%2 ) == 1;
            if (condx ^ condy){ jT .setBackground(Color.lightGray); }
                          else{ jT .setBackground(Color.white); }
             
            center .add(jT);
            
            skTeste += String .valueOf(skmap[i][j]);
             
         }}
         
        // =========================================================================================================#
         
        // BOTÃO DE TESTE //
        
        setBtTesteText();
        btTeste .setBorder( BorderFactory .createEmptyBorder() );
        btTeste .setPreferredSize( new Dimension(40, 20) );
        btTeste .addActionListener((ActionEvent ae) -> {
            
            String nums = "";
            
            if (tests_left > -1){
                tests_left --;
                setBtTesteText();
                
            }
               
            for (int n = 0; n < 81; n++){
                    
                JTextField jT = (JTextField) center .getComponent(n);
                
                if (tests_left > 0){
                    
                    if (!jT .getText().equals(""+skTeste.charAt(n))) { jT .setBorder( bad_border ); } 
                                           else if(jT .isEditable()) { jT .setBorder( normal_border ); 
                                                                       jT .setForeground(cores[Integer.parseInt(jT .getText())]);
                                                                       jT .setEditable(false);
                                                                       }

                    if (jT .getText().trim().isEmpty() ) { nums += "0"; jT .setBorder( mark_border ); } 
                                                               else { nums += jT .getText(); }
                    
                }
                
            }
             
            if (nums .equals(skTeste)) { 
                
                won = true;
                btTeste .setEnabled(false); finish(); stop();
                
            } else if (tests_left == -1){
                
                finish(); stop();
                
            }
                
            });
        
        // =======================================================================================================#
         
        // BOTÃO DE ADIÇÃO //
        
        btPlus  .setBorder( BorderFactory .createEmptyBorder() );
        btPlus  .setPreferredSize( new Dimension(20, 20) );
        btPlus  .addActionListener((ActionEvent ae) -> {
            
            GUI gui = new GUI();
            stop();
            dispose();
               
        });
         
        // ========================================================================================================#
        
        time_bar .setFocusable(true);
        time_bar .setBorder( BorderFactory .createLoweredBevelBorder() );
        time_bar .setValue(100*(time_left/total_time));
        time_bar .setPreferredSize( new Dimension(50, 10) );
        time_bar .setForeground( Color .GREEN );
        time_bar .requestFocusInWindow();
        
        // ========================================================================================================#
        
        msg .setVisible(false);
        
        // ========================================================================================================#
        
        center .setBorder( 
                BorderFactory .createCompoundBorder( 
                        BorderFactory.createEmptyBorder(10, 10, 10, 10) , 
                    BorderFactory .createBevelBorder(0, Color.lightGray, Color.gray)
                )
        );
        
        btTeste .setFont(font);     btPlus .setFont(font);      msg .setFont(font);
        south .add(btPlus);         south .add(btTeste);        south .add(msg);
        south .add( time_bar );
         
        setSize(250, 290); setResizable(false); setTitle("Sudoku 1.2"); setVisible( true ); setLocationRelativeTo(null);
        
        timer .scheduleAtFixedRate( new TimerTask(){
            @Override
            public void run() {
                
                System.out.println(rngId+":"+time_left);
                time_bar .setValue((int) (100*( (double)time_left/(double)total_time )));
                time_bar .setForeground( new Color( (int)(255-time_bar.getValue()*2.55), (int)(time_bar.getValue()*2.55), 0 ) );
                
                if (time_left < 15){
                    bg( time_left%2==0 ? Color.white : Color.red );
                }
                
                if (time_left > 0){
                    time_left --;
                } else {
                    finish();
                    stop();
                }
            }
        } , 0, 1000);
        
    }
    
}
