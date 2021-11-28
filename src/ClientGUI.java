
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Xristakos
 */
public class ClientGUI extends javax.swing.JFrame {
   private BufferedReader Reader;
    private static BufferedWriter Writer;
    private static String userName;
    private Socket socket;
    /**
     * Creates new form ClientGUI
     */
    public ClientGUI(Socket socket,String userName) {
        
         try{
            initComponents();
            this.setTitle(userName);
            this.socket = socket;
            this.userName = userName;
            this.Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.Writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
       catch(IOException e){
           System.out.println("ERROR");
       }
    }
    
    public ClientGUI(){
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        txt_field = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        text_area = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("SEND");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        txt_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_fieldKeyPressed(evt);
            }
        });

        text_area.setColumns(20);
        text_area.setRows(5);
        jScrollPane1.setViewportView(text_area);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_field)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        
        sendMsg();
    
    }//GEN-LAST:event_jButton1MouseClicked

    
    /**
     * Functions that broadcast user message to the server
     */
    public void sendMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                      String msg = txt_field.getText();
                      if(msg.equals(" ") || msg.isEmpty()){
                          System.out.println("Please Enter Your Message!");
                      }else{
                          Writer.write(msg);
                          Writer.newLine();
                          Writer.flush();
                     
                      }
                      
                 
                      
                } catch (Exception e) {
                    System.out.println("Writer is closed");
                }
              
                
            }
        }).start();
    }
    private void txt_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fieldKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            sendMsg();
            
        }
    }//GEN-LAST:event_txt_fieldKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//         try{
//            
//            Socket socket = new Socket("localhost",2020);
//            String name = JOptionPane.showInputDialog(null, "Enter Your UserName");
//            if(name.equals("")){
//                    /**
//                        Checking if name is valid 
//                        * Trimming the name in order to prevent white spaces by the user
//                     */
//                    while( name.trim().length() == 0 || name.equals("") ){
//                        JOptionPane.showMessageDialog(null,"Please Enter Valid Name" );
//                        name = JOptionPane.showInputDialog(null, "Enter Your UserName");
//                    }
//                  
//            }
//            ClientGUI main = new ClientGUI(socket,name);
//            sendMessage(name);
//            main.setVisible(true);
//            main.listenForMessages();
//
//        }
//        catch(IOException e){
//            e.toString();
//        }
//        
         

    }

    public void startClient(){
    try{
            
            Socket socket = new Socket("localhost",2020);
            String name = JOptionPane.showInputDialog(null, "Enter Your UserName");
            if(name.equals("")){
                    /**
                        Checking if name is valid 
                        * Trimming the name in order to prevent white spaces by the user
                     */
                    while( name.trim().length() == 0 || name.equals("") ){
                        JOptionPane.showMessageDialog(null,"Please Enter Valid Name" );
                        name = JOptionPane.showInputDialog(null, "Enter Your UserName");
                    }
                  
            }
            ClientGUI main = new ClientGUI(socket,name);
            sendMessage(name);
            main.setVisible(true);
            main.listenForMessages();

        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null,"The Server is Offline, or MYSQL, Please Run The Server First!" );
            System.out.println(e);
        }}
     
    
 public static void sendMessage(String username){
     
      new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     
                      
                          Writer.write(username);
                          Writer.newLine();
                          Writer.flush();
                     
                      
                      
                 
                      
                } catch (Exception e) {
                    System.out.println("Writer is closed");
                }
              
                
            }
        }).start();
     
 }
    
    public void listenForMessages(){

        new Thread(new Runnable() {
            @Override
            public void run() {
         
                String msgFromChat;
                while(socket.isConnected()){
                    try{
                        
                        msgFromChat = Reader.readLine();
                        
                        System.out.println(msgFromChat);
                        text_area.append(msgFromChat+"\n");
                    }catch(IOException e){
                        e.toString();
                    }

                }
            }
        }).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea text_area;
    private javax.swing.JTextField txt_field;
    // End of variables declaration//GEN-END:variables
}
