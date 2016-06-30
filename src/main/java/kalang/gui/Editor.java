package kalang.gui;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import kalang.compiler.KalangCompiler;
import kalang.util.ClassExecutor;
/**
 *
 * @author Kason Yang
 */
public class Editor extends javax.swing.JFrame {

    /**
     * Creates new form Editor
     */
    public Editor() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        codeArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuTopFIle = new javax.swing.JMenu();
        menuExit = new javax.swing.JMenuItem();
        menuTopRun = new javax.swing.JMenu();
        menuRun = new javax.swing.JMenuItem();
        menuTopView = new javax.swing.JMenu();
        menuLargerFont = new javax.swing.JMenuItem();
        menuSmallerFont = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(420);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        codeArea.setColumns(20);
        codeArea.setRows(5);
        jScrollPane1.setViewportView(codeArea);

        jSplitPane1.setTopComponent(jScrollPane1);

        logArea.setEditable(false);
        logArea.setColumns(20);
        logArea.setRows(5);
        jScrollPane2.setViewportView(logArea);

        jSplitPane1.setRightComponent(jScrollPane2);

        jMenuBar1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        menuTopFIle.setText("File");

        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        menuTopFIle.add(menuExit);

        jMenuBar1.add(menuTopFIle);

        menuTopRun.setText("Run");

        menuRun.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menuRun.setText("Run");
        menuRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRunActionPerformed(evt);
            }
        });
        menuTopRun.add(menuRun);

        jMenuBar1.add(menuTopRun);

        menuTopView.setText("View");

        menuLargerFont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuLargerFont.setText("Larger font");
        menuLargerFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLargerFontActionPerformed(evt);
            }
        });
        menuTopView.add(menuLargerFont);

        menuSmallerFont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuSmallerFont.setText("Smaller font");
        menuSmallerFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSmallerFontActionPerformed(evt);
            }
        });
        menuTopView.add(menuSmallerFont);

        jMenuBar1.add(menuTopView);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuExitActionPerformed

    private void menuSmallerFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSmallerFontActionPerformed
        enlargeFontSize(-2);
    }//GEN-LAST:event_menuSmallerFontActionPerformed

    private void enlargeFontSize(int increment){
        enlargeFontSize(codeArea, increment);
        enlargeFontSize(logArea, increment);
    }
    
    private void enlargeFontSize(JTextArea area,int increment){
        Font f = area.getFont();
        float newSize = f.getSize() + increment;
        if(newSize<8) newSize = 8;
        Font newFont = f.deriveFont(newSize);
        area.setFont(newFont);
    }
    
    private void menuLargerFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLargerFontActionPerformed
        enlargeFontSize(2);
    }//GEN-LAST:event_menuLargerFontActionPerformed
    
    private void menuRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRunActionPerformed
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setErr(ps);
        System.setOut(ps);
        kalang.KalangClassLoader classLoader = new kalang.KalangClassLoader();
        String code = codeArea.getText();
        String className = "Code" + (new Date()).getTime();
        Class clazz = classLoader.parseSource(className, code,className);
        if(clazz!=null){
            try {
                ClassExecutor.executeMain(clazz, new String[0]);
            } catch (Exception ex) {
                ex.printStackTrace(ps);
            }
        }
        logArea.setText(os.toString());
    }//GEN-LAST:event_menuRunActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Editor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea codeArea;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea logArea;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuLargerFont;
    private javax.swing.JMenuItem menuRun;
    private javax.swing.JMenuItem menuSmallerFont;
    private javax.swing.JMenu menuTopFIle;
    private javax.swing.JMenu menuTopRun;
    private javax.swing.JMenu menuTopView;
    // End of variables declaration//GEN-END:variables
}
