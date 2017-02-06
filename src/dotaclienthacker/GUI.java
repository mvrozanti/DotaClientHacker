package dotaclienthacker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI extends javax.swing.JFrame {

    private static class Handler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    static {
        Thread.setDefaultUncaughtExceptionHandler(new Handler());
    }
    
    private ClientDLL c;

    public GUI() {
        initComponents();
        try {
            c = ClientDLL.getClientDLL();
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextFieldZoom.setText(c.getZoom());
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldZoom = new javax.swing.JTextField();
        jButtonSetZoom = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Zoom:");

        jTextFieldZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldZoomActionPerformed(evt);
            }
        });
        jTextFieldZoom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldZoomKeyReleased(evt);
            }
        });

        jButtonSetZoom.setText("Set");
        jButtonSetZoom.setEnabled(false);
        jButtonSetZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetZoomActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSetZoom)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldZoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSetZoom))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldZoomActionPerformed
        jButtonSetZoom.doClick();
    }//GEN-LAST:event_jTextFieldZoomActionPerformed

    private void jButtonSetZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetZoomActionPerformed
        try {
            c.applyWantedZoom(jTextFieldZoom.getText());
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSetZoomActionPerformed

    private void jTextFieldZoomKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldZoomKeyReleased
        jButtonSetZoom.setEnabled(jTextFieldZoom.getText().matches("\\d{4}"));
    }//GEN-LAST:event_jTextFieldZoomKeyReleased

    static void test() {
        try {
            Process p = Runtime.getRuntime().exec("reg query HKCR\\dota2\\Shell\\Open\\Command");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String res = "";
            String aux;
            while ((aux = br.readLine()) != null) {
                res += aux;
            }
            int exitCode = p.waitFor();
            String REGEX = "REG_SZ\\s+\"([^\"]+)";
            Matcher m = Pattern.compile(REGEX).matcher(res);
                boolean is64 = System.getProperty("os.arch").contains("64");
            if (m.find()) {
                String dotaExecutablePathname = m.group(1);
                File dotaExecutable = new File(dotaExecutablePathname);
                File correctGameFolder = dotaExecutable.getParentFile()/*win64*/.getParentFile()/*bin*/.getParentFile()/*game*/;
                File correctClientDLL = new File(correctGameFolder.getAbsolutePath() + File.separator
                        + "dota" + File.separator
                        + "bin" + File.separator
                        + "win" + (is64 ? "64" : "32")/*changes depending on arch*/ + File.separator
                        + "client.dll");
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    public static void main(String args[]) {
//        test();
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSetZoom;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextFieldZoom;
    // End of variables declaration//GEN-END:variables
}
