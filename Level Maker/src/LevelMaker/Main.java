package LevelMaker;

import javax.swing.*;

public class Main {
    
    public static void main(String[] args){
        JFrame f = new JFrame("Level Maker");
        f.setContentPane(new MainPanel());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
    }
    
}