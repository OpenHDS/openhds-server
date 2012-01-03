package org.openhds.configuration.ui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openhds.configuration.util.ValueConstraintService;
import org.openhds.configuration.util.XMLWriter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {

	private JPanel panel;
	private DefaultTableModel tableModel;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode top;
	
	private JFrame frmOpenhdsConfigurationUtility;
	private JTextField nameField;
	private JTextField descriptionField;
	private JTextField valueField;
	private JTable table;
	
	private XMLWriter xmlWriter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			 UIManager.setLookAndFeel(
			            UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {			
			public void run() {
				try {
					Main window = new Main();
					window.frmOpenhdsConfigurationUtility.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		xmlWriter = new XMLWriter();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmOpenhdsConfigurationUtility = new JFrame();
		frmOpenhdsConfigurationUtility.setTitle("OpenHDS Configuration Utility");
		frmOpenhdsConfigurationUtility.setBounds(100, 100, 542, 674);
		frmOpenhdsConfigurationUtility.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOpenhdsConfigurationUtility.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(60, 32, 283, 486);
		frmOpenhdsConfigurationUtility.getContentPane().add(tabbedPane);
		
		panel = new JPanel();
		tabbedPane.addTab("Constraint", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 11, 57, 14);
		panel.add(lblName);
		
		nameField = new JTextField();
		nameField.setBounds(82, 8, 146, 20);
		panel.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 36, 72, 14);
		panel.add(lblDescription);
		
		descriptionField = new JTextField();
		descriptionField.setBounds(82, 33, 146, 20);
		panel.add(descriptionField);
		descriptionField.setColumns(10);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(10, 61, 46, 14);
		panel.add(lblValue);
		
		valueField = new JTextField();
		valueField.setBounds(82, 61, 146, 20);
		panel.add(valueField);
		valueField.setColumns(10);
		
		JButton btnAddValue = new JButton("Add Value");
		btnAddValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if (nameField.getText().length() > 0 && descriptionField.getText().length() > 0 && valueField.getText().length() > 0) {	
					tableModel.insertRow(tableModel.getRowCount(), new Object[] {descriptionField.getText(), valueField.getText()});
					descriptionField.setText("");
					valueField.setText("");
					nameField.disable();
				}
			}
		});
		btnAddValue.setBounds(10, 92, 81, 23);
		panel.add(btnAddValue);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
				nameField.setText("");
				descriptionField.setText("");
				valueField.setText("");
				nameField.enable();
			}
		});
		btnClear.setBounds(171, 92, 57, 23);
		panel.add(btnClear);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, String> map = new HashMap<String, String>();
				
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					String key = tableModel.getValueAt(i, 0).toString();
					String value = tableModel.getValueAt(i, 1).toString();
					map.put(key, value);
				}
				xmlWriter.createConstraint(nameField.getText(), map);
								
				DefaultMutableTreeNode category = new DefaultMutableTreeNode(nameField.getText());
				top.add(category);
	
				List<String> keys = new ArrayList<String>();
				keys.addAll(map.keySet());
				
				for (String key : keys) {
					DefaultMutableTreeNode desc = new DefaultMutableTreeNode(map.get(key));
					DefaultMutableTreeNode k = new DefaultMutableTreeNode(key);
					category.add(desc);	
					desc.add(k);
				}
				treeModel.reload();
				
				nameField.setText("");
				descriptionField.setText("");
				valueField.setText("");
				nameField.enable();
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
			}
		});
		btnCreate.setBounds(96, 92, 65, 23);
		panel.add(btnCreate);
								
		JScrollPane treeScrollPane = new JScrollPane();
		treeScrollPane.setBounds(10, 229, 254, 216);
		panel.add(treeScrollPane);	

		JTree tree = new JTree();		
		top = new DefaultMutableTreeNode("Constraints");
		
		ValueConstraintService valueConstraintService = new ValueConstraintService();
		
		List<String> names = valueConstraintService.getAllConstraintNames();
		for (String name : names) {
			DefaultMutableTreeNode category = new DefaultMutableTreeNode(name);
			top.add(category);
			
			Map<String, String> map = valueConstraintService.getMapForConstraint(name);
			List<String> keys = new ArrayList<String>();
			keys.addAll(map.keySet());
			
			for (String key : keys) {
				DefaultMutableTreeNode desc = new DefaultMutableTreeNode(map.get(key));
				DefaultMutableTreeNode k = new DefaultMutableTreeNode(key);
				category.add(desc);	
				desc.add(k);
			}
		}
		treeModel = new DefaultTreeModel(top);
		tree.setModel(treeModel);	
		treeScrollPane.setColumnHeaderView(tree);
						
		JScrollPane tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 126, 254, 92);
		panel.add(tableScrollPane);
		
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Description");
		tableModel.addColumn("Value");
		table = new JTable(tableModel);
		tableScrollPane.setViewportView(table);
	}	
}
