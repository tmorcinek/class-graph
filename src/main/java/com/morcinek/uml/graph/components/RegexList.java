package com.morcinek.uml.graph.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 3/27/13
 * Time: 11:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegexList extends JPanel implements ListSelectionListener {

    private static final String hireString = "Add";
    private static final String fireString = "Remove";

    private JList list;
    private DefaultListModel<String> listModel;

    private JButton fireButton;
    private JTextField employeeName;

    private final ActionListener actionListener;

    public RegexList(ActionListener actionListener) {
        super(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Regex exclude list"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.actionListener = actionListener;

        listModel = new DefaultListModel<String>();

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(3);

        JButton hireButton = new JButton(hireString);
        HireListener hireListener = new HireListener(hireButton);
        hireButton.setActionCommand(hireString);
        hireButton.addActionListener(hireListener);
        hireButton.setEnabled(false);

        fireButton = new JButton(fireString);
        fireButton.setActionCommand(fireString);
        fireButton.addActionListener(new FireListener());
        fireButton.setEnabled(false);

        employeeName = new JTextField(10);
        employeeName.addActionListener(hireListener);
        employeeName.getDocument().addDocumentListener(hireListener);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(fireButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(employeeName);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(hireButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            if (index != -1) {
                listModel.remove(index);

                int size = listModel.getSize();

                if (size == 0) { //Nobody's left, disable firing.
                    fireButton.setEnabled(false);

                } else { //Select an index.
                    if (index == listModel.getSize()) {
                        //removed item in last position
                        index--;
                    }

                    list.setSelectedIndex(index);
                    list.ensureIndexIsVisible(index);
                }
                actionListener.actionPerformed(e);
            }
        }
    }

    //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = employeeName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                employeeName.requestFocusInWindow();
                employeeName.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            listModel.insertElementAt(employeeName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            employeeName.requestFocusInWindow();
            employeeName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            actionListener.actionPerformed(e);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable fire button.
                fireButton.setEnabled(false);

            } else {
                //Selection, enable the fire button.
                fireButton.setEnabled(true);
            }
        }
    }

    public List<String> getRegexPatterns() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < listModel.size(); i++) {
            list.add(listModel.getElementAt(i));
        }
        return list;
    }
}
