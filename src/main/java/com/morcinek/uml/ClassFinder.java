package com.morcinek.uml;

import com.morcinek.uml.graph.SimulationVisualization;
import com.morcinek.uml.relations.RelationsProvider;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ClassFinder {

    private static String developmentMode(String[] args, boolean isDeveloped) {
        String pathName;
        if (isDeveloped) {
//            pathName = "c:\\dev\\repositories\\taptera\\android-maven-build\\apps\\sunbelt\\src\\main\\java";
            pathName = "test-data/Test1";
        } else {
            pathName = args[0];
        }

        return pathName;
    }

    public static void main(String[] args) {
        try {
            String pathName = developmentMode(args, true);

            RelationsProvider relationsProvider = new RelationsProvider();
            final Map<String, HashMap<String, Integer>> relations = relationsProvider.provideRelations(pathName, null);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SimulationVisualization simulationVisualization = new SimulationVisualization(relations);
                    simulationVisualization.setVisible(true);
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error occured!! There is no directory path in execution argument!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error occured!! " + e.getMessage() + "!");
            e.printStackTrace();
        }
    }
}
