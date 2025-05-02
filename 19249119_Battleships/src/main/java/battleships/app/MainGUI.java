package battleships.app;

import battleships.controller.Controller;
import battleships.model.Model;
import javax.swing.SwingUtilities;


//  GUI entry point: only creates Controller, which wires Model and GuiView.
//  This class satisfies the requirement of initializing the GUI-based application.

public class MainGUI {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure GUI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            Model model = new Model();       // Create a new Model instance with randomly positioned ships
            new Controller(model);           // Create Controller which internally sets up the GuiView and registers observers
        });
    }
}

