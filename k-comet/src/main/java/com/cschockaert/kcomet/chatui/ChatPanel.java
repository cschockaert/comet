package com.cschockaert.kcomet.chatui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.cschockaert.kcomet.bus.Bus;
import com.cschockaert.kcomet.bus.TextInputEvent;

import net.engio.mbassy.bus.BusFactory;
import net.engio.mbassy.bus.MBassador;

public class ChatPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1468912747696518220L;

	private JTextPane chat = new JTextPane();
	private JTextField input = new JTextField();

	public ChatPanel() {
		super();
		this.setLayout(new BorderLayout());
		this.add(chat, BorderLayout.CENTER);
		this.add(input, BorderLayout.SOUTH);
		
		input.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (input.getText() != null) {
						Bus.BUS.publish(new TextInputEvent(input.getText()));
						input.setText("");
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		writeInfo("Chat panel");
		chat.setEditable(false);
	}

	public void writeInfo(String msg) {
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		// StyleConstants.setFontFamily(attrs,"Serif");
		// StyleConstants.setFontSize(attrs, 18);
		StyleConstants.setForeground(attrs, Color.BLACK);
		write(msg, attrs);
	}

	// Show a warning message (yellow, italic text)
	public void writeWarning(String msg) {
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setForeground(attrs, Color.YELLOW);
		StyleConstants.setItalic(attrs, true);
		write(msg, attrs);
	}

	// Show an error message (red, bold/italic text)
	public void writeError(String msg) {
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setForeground(attrs, Color.RED);
		StyleConstants.setItalic(attrs, true);
		StyleConstants.setBold(attrs, true);
		write(msg, attrs);
	}

	// Show a text message using the specified AttributeSet
	public void write(String msg, AttributeSet attrs) {
		Document doc = chat.getDocument();
		msg += "\n";
		try {
			doc.insertString(doc.getLength(), msg, attrs);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

}
