package org.openhds.configuration.ui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.openhds.configuration.util.ExtensionService;
import org.openhds.configuration.util.ValueConstraintService;
import org.openhds.configuration.util.XMLWriter;
import org.openhds.extensions.ExtensionLoader;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Main {

	private JPanel constraintPanel;
	private JPanel extensionPanel;
	private DefaultTableModel tableModel;
	private JTree constraintTree;
	private JTree extensionTree;
	private DefaultTreeModel constraintTreeModel;
	private DefaultTreeModel extensionTreeModel;
	private DefaultMutableTreeNode constraintTop;
	private DefaultMutableTreeNode extensionTop;
	private DefaultMutableTreeNode selectedConstraintNode = null;
	private DefaultMutableTreeNode selectedExtensionNode = null;
	
	private JFrame frmOpenhdsConfigurationUtility;
	private JTextField constraintNameField;
	private JTextField constraintDescriptionField;
	private JTextField constraintValueField;
	private JTable constraintTable;
	
	private XMLWriter xmlWriter;
	private JTextField attrNameTextField;
	private JTextField attrDescTextField;
	private JTextField constraintTextField;
	private JComboBox entityTypeComboBox;
	private JComboBox attrTypeComboBox;
	
	private HashMap<String, ArrayList<Map<String, String>>> extensionMap;
	private ExtensionService extensionService;

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
		frmOpenhdsConfigurationUtility.setTitle("OpenHDS Extension Configuration Utility");
		frmOpenhdsConfigurationUtility.setBounds(100, 100, 418, 577);
		frmOpenhdsConfigurationUtility.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOpenhdsConfigurationUtility.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 383, 516);
		frmOpenhdsConfigurationUtility.getContentPane().add(tabbedPane);
		
		constraintPanel = new JPanel();
		tabbedPane.addTab("Constraint", null, constraintPanel, null);
		constraintPanel.setLayout(null);
		
		extensionPanel = new JPanel();
		tabbedPane.addTab("Extension", null, extensionPanel, null);
		extensionPanel.setLayout(null);
								
		initializeFields();
		initializeBtns();
		initializeTable();
		initializeConstraintTree();
		initializeExtensionTree();
	}	
	
	public void initializeFields() {
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(36, 11, 57, 14);
		constraintPanel.add(lblName);
		
		constraintNameField = new JTextField();
		constraintNameField.setBounds(108, 8, 176, 20);
		constraintPanel.add(constraintNameField);
		constraintNameField.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(36, 36, 72, 14);
		constraintPanel.add(lblDescription);
		
		constraintDescriptionField = new JTextField();
		constraintDescriptionField.setBounds(108, 33, 176, 20);
		constraintPanel.add(constraintDescriptionField);
		constraintDescriptionField.setColumns(10);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(36, 61, 46, 14);
		constraintPanel.add(lblValue);
		
		constraintValueField = new JTextField();
		constraintValueField.setBounds(108, 58, 176, 20);
		constraintPanel.add(constraintValueField);
		constraintValueField.setColumns(10);
		
		JLabel lblEntityType = new JLabel("Entity Type:");
		lblEntityType.setBounds(34, 11, 78, 14);
		extensionPanel.add(lblEntityType);
		
		entityTypeComboBox = new JComboBox();
		entityTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Individual", "Location", "SocialGroup", "Visit"}));
		entityTypeComboBox.setBounds(169, 8, 113, 20);
		extensionPanel.add(entityTypeComboBox);
		
		JLabel lblAttributeType = new JLabel("Attribute Type:");
		lblAttributeType.setBounds(34, 35, 78, 14);
		extensionPanel.add(lblAttributeType);
		
		attrTypeComboBox = new JComboBox();
		attrTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"String", "Boolean", "Integer"}));
		attrTypeComboBox.setBounds(169, 32, 113, 20);
		attrTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox comboBox = (JComboBox)event.getSource();
				if (comboBox.getSelectedItem().toString().equals("Boolean")) {
					constraintTextField.setText("");
					constraintTextField.disable();
				}
				else {
					constraintTextField.enable();
				}
			}
			
		});
		extensionPanel.add(attrTypeComboBox);
		
		JLabel lblNewLabel = new JLabel("Attribute Name:");
		lblNewLabel.setBounds(34, 60, 78, 14);
		extensionPanel.add(lblNewLabel);
		
		attrNameTextField = new JTextField();
		attrNameTextField.setBounds(169, 57, 165, 20);
		extensionPanel.add(attrNameTextField);
		attrNameTextField.setColumns(10);
		
		JLabel lblAttributeDescription = new JLabel("Attribute Description:");
		lblAttributeDescription.setBounds(34, 85, 110, 14);
		extensionPanel.add(lblAttributeDescription);
		
		attrDescTextField = new JTextField();
		attrDescTextField.setBounds(169, 82, 165, 20);
		extensionPanel.add(attrDescTextField);
		attrDescTextField.setColumns(10);
		
		JLabel lblConstraint = new JLabel("Constraint:");
		lblConstraint.setBounds(34, 110, 78, 14);
		extensionPanel.add(lblConstraint);
		
		constraintTextField = new JTextField();
		constraintTextField.setBounds(169, 107, 165, 20);
		extensionPanel.add(constraintTextField);
		constraintTextField.setColumns(10);
	}
	
	public void initializeBtns() {
		
		JButton constraintAddValueBtn = new JButton("Add Value");
		constraintAddValueBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if (constraintNameField.getText().length() > 0 && constraintDescriptionField.getText().length() > 0 && constraintValueField.getText().length() > 0) {	
					tableModel.insertRow(tableModel.getRowCount(), new Object[] {constraintDescriptionField.getText(), constraintValueField.getText()});
					constraintDescriptionField.setText("");
					constraintValueField.setText("");
					constraintNameField.disable();
				}
			}
		});
		constraintAddValueBtn.setBounds(36, 92, 87, 23);
		constraintPanel.add(constraintAddValueBtn);
		
		JButton constraintClearBtn = new JButton("Clear");
		constraintClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
				constraintNameField.setText("");
				constraintDescriptionField.setText("");
				constraintValueField.setText("");
				constraintNameField.enable();
			}
		});
		constraintClearBtn.setBounds(215, 92, 69, 23);
		constraintPanel.add(constraintClearBtn);
		
		JButton constraintDeleteBtn = new JButton("Delete");
		constraintDeleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (selectedConstraintNode != null) {
					xmlWriter.removeConstraint(selectedConstraintNode.toString());
					constraintTreeModel.removeNodeFromParent(selectedConstraintNode);
					constraintTreeModel.reload();
					selectedConstraintNode = null;
				}
			}
		});
		constraintDeleteBtn.setBounds(36, 456, 72, 23);
		constraintPanel.add(constraintDeleteBtn);
		
		JButton constraintCreateBtn = new JButton("Create");
		constraintCreateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, String> map = new TreeMap<String, String>();
				
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					String key = tableModel.getValueAt(i, 0).toString();
					String value = tableModel.getValueAt(i, 1).toString();
					map.put(key, value);
				}
				xmlWriter.createConstraint(constraintNameField.getText(), map);
								
				DefaultMutableTreeNode category = new DefaultMutableTreeNode(constraintNameField.getText());
				constraintTop.add(category);
	
				List<String> keys = new ArrayList<String>();
				keys.addAll(map.keySet());
				
				for (String key : keys) {
					DefaultMutableTreeNode desc = new DefaultMutableTreeNode(key);
					DefaultMutableTreeNode k = new DefaultMutableTreeNode(map.get(key));
					category.add(desc);	
					desc.add(k);
				}
				constraintTreeModel.reload();
				
				constraintNameField.setText("");
				constraintDescriptionField.setText("");
				constraintValueField.setText("");
				constraintNameField.enable();
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
			}
		});
		constraintCreateBtn.setBounds(133, 92, 72, 23);
		constraintPanel.add(constraintCreateBtn);
		
		JButton extensionCreateBtn = new JButton("Create");
		extensionCreateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean error = false;
				ValueConstraintService valueConstraintService = new ValueConstraintService();
				
				String entity = entityTypeComboBox.getSelectedItem().toString();
				String attributeName = attrNameTextField.getText();
				String description = attrDescTextField.getText();
				String type = attrTypeComboBox.getSelectedItem().toString();
				String constraint = constraintTextField.getText();
				
				if (type.equals("String") && !constraint.equals("none")) {
					List<String> names = valueConstraintService.getAllConstraintNames();
					
					if (!names.contains(constraint)) {
						JOptionPane.showMessageDialog(null, "The constraint entered is not valid.");
						error = true;
					}
				}
				
				if (error == false) {
					xmlWriter.createExtension(entity, attributeName, description, type, constraint);
					
					DefaultMutableTreeNode category = new DefaultMutableTreeNode(attributeName);
					
					for (int i = 0; i < extensionTreeModel.getChildCount(constraintTreeModel.getRoot()); i++) {
						DefaultMutableTreeNode item = (DefaultMutableTreeNode) extensionTreeModel.getChild(extensionTreeModel.getRoot(), i);
						
						if (item.toString().equals(entity)) {
							item.add(category);
							break;
						}
					}
					extensionTreeModel.reload();
					
					attrNameTextField.setText("");
					attrDescTextField.setText("");
					constraintTextField.setText("");
					entityTypeComboBox.setSelectedIndex(0);
					attrTypeComboBox.setSelectedIndex(0);
				}
			}
		});
		extensionCreateBtn.setBounds(34, 135, 78, 23);
		extensionPanel.add(extensionCreateBtn);
		
		JButton extensionClearBtn = new JButton("Clear");
		extensionClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attrNameTextField.setText("");
				attrDescTextField.setText("");
				constraintTextField.setText("");
				entityTypeComboBox.setSelectedIndex(0);
				attrTypeComboBox.setSelectedIndex(0);
			}
		});
		extensionClearBtn.setBounds(123, 135, 73, 23);
		extensionPanel.add(extensionClearBtn);
		
		JButton extensionDeleteBtn = new JButton("Delete");
		extensionDeleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedExtensionNode != null) {
					xmlWriter.removeExtension(selectedExtensionNode.getParent().toString(), selectedExtensionNode.toString());
					extensionTreeModel.removeNodeFromParent(selectedExtensionNode);
					extensionTreeModel.reload();
					selectedExtensionNode = null;
					
					attrNameTextField.setText("");
					attrDescTextField.setText("");
					constraintTextField.setText("");
					entityTypeComboBox.setSelectedIndex(0);
					attrTypeComboBox.setSelectedIndex(0);
				}
			}
		});
		extensionDeleteBtn.setBounds(34, 454, 78, 23);
		extensionPanel.add(extensionDeleteBtn);
	}
	
	public void initializeTable() {
		
		JScrollPane constraintTableScrollPane = new JScrollPane();
		constraintTableScrollPane.setBounds(36, 126, 303, 92);
		constraintPanel.add(constraintTableScrollPane);
		
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Description");
		tableModel.addColumn("Value");
		constraintTable = new JTable(tableModel);
		constraintTableScrollPane.setViewportView(constraintTable);
	}
	
	public void initializeConstraintTree() {
		
		constraintTop = new DefaultMutableTreeNode("Constraints");
		
		ValueConstraintService valueConstraintService = new ValueConstraintService();
		
		List<String> names = valueConstraintService.getAllConstraintNames();
		for (String name : names) {
			DefaultMutableTreeNode category = new DefaultMutableTreeNode(name);
			constraintTop.add(category);
			
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
		constraintTreeModel = new DefaultTreeModel(constraintTop);
		
		JScrollPane constraintTreeScrollPane = new JScrollPane();
		constraintTreeScrollPane.setBounds(36, 229, 303, 216);
		constraintPanel.add(constraintTreeScrollPane);	
		
		constraintTree = new JTree();
		constraintTreeScrollPane.setViewportView(constraintTree);
		constraintTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		constraintTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) constraintTree.getLastSelectedPathComponent();
				selectedConstraintNode = null;
				if (node == null)
					return;
				
				Object nodeInfo = node.getUserObject();
				
				int children = constraintTreeModel.getChildCount(constraintTreeModel.getRoot());
				for (int i = 0; i < children; i++) {
					Object item = constraintTreeModel.getChild(constraintTreeModel.getRoot(), i);
					
					if (nodeInfo.toString().equals(item.toString())) 
						selectedConstraintNode = node;	
				}
			}
		});
		constraintTree.setModel(constraintTreeModel);
	}
	
	public void initializeExtensionTree() {
		
		extensionTop = new DefaultMutableTreeNode("Extensions");
		
		JScrollPane extensionScrollPane = new JScrollPane();
		extensionScrollPane.setBounds(34, 169, 300, 276);
		extensionPanel.add(extensionScrollPane);
		
		extensionService = new ExtensionService();
		extensionMap = extensionService.getMapForExtensions();
		List<String> keys = new ArrayList<String>();
		keys.addAll(extensionMap.keySet());
		
		for (String name : keys) {
			DefaultMutableTreeNode category = new DefaultMutableTreeNode(name);
			extensionTop.add(category);
			
			 ArrayList<Map<String, String>> list = extensionMap.get(name);
			 
			 for (int i = 0; i < list.size(); i++) {
				 
				 Map<String, String> attrs = list.get(i); 
				 String nameAttr = attrs.get("name");
				 DefaultMutableTreeNode nameAttrNode = new DefaultMutableTreeNode(nameAttr);
				 category.add(nameAttrNode);
			 }
		}
		extensionTreeModel = new DefaultTreeModel(extensionTop);
			
		extensionTree = new JTree();
		extensionScrollPane.setViewportView(extensionTree);
		extensionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		extensionTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				
				extensionService = new ExtensionService();
				extensionMap = extensionService.getMapForExtensions();
				List<String> keys = new ArrayList<String>();
				keys.addAll(extensionMap.keySet());
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) extensionTree.getLastSelectedPathComponent();
				selectedExtensionNode = null;
				if (node == null)
					return;
				
				Object nodeInfo = node.getUserObject();
						
				if (extensionTreeModel.isLeaf(node) && !node.getParent().toString().equals(extensionTreeModel.getRoot().toString())) {
					selectedExtensionNode = node;	
					
					String entityType = node.getParent().toString();
					ArrayList<Map<String, String>> list = extensionMap.get(entityType);
					
					for (int i = 0; i < list.size(); i++) {
						
						Map<String, String> attrs = list.get(i);
						if (attrs.get("name").equals(nodeInfo.toString())) {
							attrNameTextField.setText(nodeInfo.toString());
							attrDescTextField.setText(attrs.get("description"));
							constraintTextField.setText(attrs.get("constraint"));
							entityTypeComboBox.setSelectedItem(entityType);
							attrTypeComboBox.setSelectedItem(attrs.get("type"));
						}
					}	
				}
				else {
					attrNameTextField.setText("");
					attrDescTextField.setText("");
					constraintTextField.setText("");
					entityTypeComboBox.setSelectedIndex(0);
					attrTypeComboBox.setSelectedIndex(0);
				}
			}
		});
		extensionTree.setModel(extensionTreeModel);
		
		JButton btnNewButton = new JButton("Generate Source");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExtensionLoader extensionLoader = new ExtensionLoader();
				if (extensionLoader.start()) {
					JOptionPane.showMessageDialog(frmOpenhdsConfigurationUtility, "The source code has been successfully " +
							"modified, re-build the OpenHDS core and redeploy the application for changes to take effect.");
				}
				else {
					JOptionPane.showMessageDialog(frmOpenhdsConfigurationUtility, "There was an error executing the updates " +
							"to the source files.");
				}
			}
		});
		btnNewButton.setBounds(123, 454, 126, 23);
		extensionPanel.add(btnNewButton);
	}
}
