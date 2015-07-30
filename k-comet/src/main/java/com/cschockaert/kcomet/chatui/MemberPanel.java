package com.cschockaert.kcomet.chatui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

public class MemberPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6515388780429497441L;

	private JList<String> members = new JList<String>();
	
	private DefaultListModel<String> membersModel = new DefaultListModel<String>();
	
	public MemberPanel() {
		this.setLayout(new BorderLayout());
		this.add(members, BorderLayout.CENTER);
	
		members.setModel(membersModel);
		membersModel.addElement("members");
	}
}
