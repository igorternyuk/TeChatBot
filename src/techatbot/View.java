/**
 * Java. TeChatBot
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package techatbot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class View implements ActionListener{
    private final String WINDOW_TITLE = "TeChatBot";
    private final String BOT_NICKNAME = "Chatter";
    private final String HUMAN_NICKNAME = "Human";
    private final int WINDOW_WIDTH = 350, WINDOW_HEIGHT = 450;
    private final JFrame window;
    private final JScrollPane scrollPane;
    private final JTextPane chatArea;
    private final JCheckBox ai;
    private final JTextField message;
    private final JButton btnEnter;
    private final JPanel bottomPanel;
    private final SimpleAttributeSet botNickStyle;
    private final SimpleAttributeSet botTextStyle;
    private final SimpleAttributeSet humanNickStyle;
    private final SimpleAttributeSet humanTextStyle;
    private final Model model;
    
    public View() {
        window = new JFrame(WINDOW_TITLE);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setFocusable(true);
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setContentType("text/html");
        scrollPane = new JScrollPane(chatArea);
        botNickStyle = new SimpleAttributeSet();
        //StyleConstants.setItalic(botNickStyle, false);
        StyleConstants.setBold(botNickStyle, true);
        StyleConstants.setForeground(botNickStyle, Color.RED);
        botTextStyle = new SimpleAttributeSet();
        StyleConstants.setItalic(botTextStyle, true);
        StyleConstants.setForeground(botTextStyle, Color.BLUE);
        humanNickStyle = new SimpleAttributeSet();
        StyleConstants.setBold(humanNickStyle, true);
        StyleConstants.setForeground(humanNickStyle, Color.GREEN.darker());
        humanTextStyle = new SimpleAttributeSet();
        StyleConstants.setItalic(humanTextStyle, true);
        StyleConstants.setForeground(humanTextStyle, Color.MAGENTA);
        //Creating the checkbox which will enable or desable
        //artificial intellect of the bot
        ai = new JCheckBox("AI");
        ai.doClick();
        //Creating the text field for message input
        message = new JTextField();
        message.addActionListener(this);
        //Creating the button which will send the message
        btnEnter = new JButton("Enter");
        btnEnter.addActionListener(this);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        //Adding our components to the bottom panel below the chat area
        bottomPanel.add(ai);
        bottomPanel.add(message);
        bottomPanel.add(btnEnter);
        window.add(BorderLayout.CENTER, scrollPane);
        window.add(BorderLayout.SOUTH, bottomPanel);
        model = new Model();
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //If the entered message contains symbols besides spaces
        //we will put that message to the chat area
        
        if(message.getText().trim().length() > 0){
            try {
                StyledDocument doc = chatArea.getStyledDocument();
                //Human phrase
                doc.insertString(doc.getLength(), HUMAN_NICKNAME + ":\n", humanNickStyle);
                doc.insertString(doc.getLength(), message.getText().trim() + "\n", humanTextStyle);
                //Robot phrase
                doc.insertString(doc.getLength(), BOT_NICKNAME + ":\n", botNickStyle);
                doc.insertString(doc.getLength(), model.answer(message.getText().trim(), true) + "\n", botTextStyle);
            } catch (BadLocationException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        message.setText(""); //Clear the text field
        message.requestFocusInWindow(); //Request focus for the text field
    }
}
