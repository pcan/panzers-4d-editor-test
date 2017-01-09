/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanzersEditorGui.java
 *
 * Created on 15-ago-2011, 18.01.42
 */

package it.panzers.editor.gui;

import it.panzers.editor.gui.test.BonesTestPanel;
import it.panzers.editor.gui.test.HumanMeshTestPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//cg language vm options:
//-Dj3d.shadingLanguage=Cg
/**
 *
 * @author Luigi
 */
public class PanzersEditorGui extends javax.swing.JFrame {

    HumanMeshTestPanel testPanel = new HumanMeshTestPanel();

    public PanzersEditorGui() {
        initComponents();
        jPanel1.add(testPanel);
        //jPanel2.add(new MeshTestPanel());
        //int vertCount = testPanel.converter.modelVertexes.getVertexesQuantity();
        //jSlider1.setMaximum(vertCount-1);

    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jPanel1 = new JPanel();
        jPanel3 = new JPanel();
        jSlider1 = new JSlider();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new Color(204, 204, 255));
        jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jPanel1.setPreferredSize(new Dimension(700, 600));
        jPanel1.setLayout(new BorderLayout());

        jPanel3.setBackground(new Color(204, 204, 255));
        jPanel3.setPreferredSize(new Dimension(10, 70));
        jPanel3.setRequestFocusEnabled(false);

        jSlider1.setBackground(new Color(204, 204, 255));
        jSlider1.setForeground(new Color(204, 204, 255));
        jSlider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel1.setVerticalAlignment(SwingConstants.TOP);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jLabel1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, BorderLayout.PAGE_END);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
//        int index = jSlider1.getValue();
//        VertexBone18f vertex = testPanel.converter.modelVertexes.getVertexBoneArray()[index];
//        PointArray pointArray = ((PointArray)testPanel.converter.point.getGeometry());
//        pointArray.setCoordinate(0, vertex.getCoord());
//
//        String label1 = String.format("Bones for vertex %d: %d, %d, %d, %d",
//                index, vertex.getBonesIndexes()[0], vertex.getBonesIndexes()[1], vertex.getBonesIndexes()[2], vertex.getBonesIndexes()[3]);
//
//        String label2 = String.format("Weights: %.2f   %.2f   %.2f   %.2f",
//                 vertex.getBonesWeights()[0], vertex.getBonesWeights()[1], vertex.getBonesWeights()[2], vertex.getBonesWeights()[3]);
//
//        String label3 = String.format("mtbl Bones: %d, %d, %d, %d",
//                vertex.getMtblBonesIndexes()[0], vertex.getMtblBonesIndexes()[1], vertex.getMtblBonesIndexes()[2], vertex.getMtblBonesIndexes()[3]);
//
//
//        jLabel1.setText(label1);
//        jLabel2.setText(label2);
//        jLabel3.setText(label3);
    }//GEN-LAST:event_jSlider1StateChanged


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanzersEditorGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JPanel jPanel1;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JSlider jSlider1;
    private JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
