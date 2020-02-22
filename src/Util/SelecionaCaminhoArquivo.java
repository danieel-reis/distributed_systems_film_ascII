/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author daniel
 */
public class SelecionaCaminhoArquivo extends JPanel {

    public String selectPaste() {
        JFileChooser arquivo = new JFileChooser();
        arquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (arquivo.showOpenDialog(arquivo) == JFileChooser.APPROVE_OPTION) {
            return arquivo.getSelectedFile().getPath();
        }
        return "";
    }
}
