package com.cschockaert.kcomet.chatui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ChatWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2259068995819442434L;

	ChatPanel chat = new ChatPanel();
	MemberPanel members = new MemberPanel();
	JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	public ChatWindow() {
		super("cometd chat demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        
        Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(jsp);
		
		jsp.add(members);
		jsp.add(chat);
		
        setVisible(true);
	}
	
	
	public void addExternalMessage(String text) {
		chat.writeInfo(text);
	}
	
}
