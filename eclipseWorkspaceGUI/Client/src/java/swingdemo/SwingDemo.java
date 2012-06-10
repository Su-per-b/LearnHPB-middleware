package swingdemo;

import javax.swing.*;

import swingdemo.framework.AbstractController;

public class SwingDemo {

    public static AbstractController applicationController;

    public static void main(String[] args) throws Exception {

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                applicationController = new SwingDemoController();
            }
        });
    }

}
