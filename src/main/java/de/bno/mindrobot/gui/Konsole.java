package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.EDIT;
import static de.bno.mindrobot.gui.Strings.SYNTAX_DANN;
import static de.bno.mindrobot.gui.Strings.SYNTAX_ENDE;
import static de.bno.mindrobot.gui.Strings.SYNTAX_SOLANGE;
import static de.bno.mindrobot.gui.Strings.SYNTAX_SONST;
import static de.bno.mindrobot.gui.Strings.SYNTAX_WENN;
import static de.bno.mindrobot.gui.Strings.SYNTAX_WIEDERHOLE;
import static de.bno.mindrobot.gui.Strings.String;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.gui.parser.MindTalk;
import de.bno.mindrobot.gui.parser.Parser;

public class Konsole extends JPanel implements KeyListener {

	private static final Logger LOG = MindRobot.getLogger(Konsole.class);

	private static final long serialVersionUID = -6470828939746541026L;

	private JLabel title;

	private JPanel topPanel;

	private JScrollPane sp;

	private JTextPane editor;

	private Parser parser;

	private boolean stopped;

	private static final String[] HIGHLIGHT_DEF = new String[] {
			String(SYNTAX_WENN), String(SYNTAX_DANN), String(SYNTAX_SONST),
			String(SYNTAX_ENDE), String(SYNTAX_WIEDERHOLE),
			String(SYNTAX_SOLANGE) };

	private static final Color DEFAULT_KEYWORD_COLOR = new Color(149, 0, 85);
	public static String HIGHLIGHT_DEF_REGEX;

	static {
		StringBuilder buff = new StringBuilder("");
		buff.append("(");
		for (String keyword : HIGHLIGHT_DEF) {
			buff.append("\\b").append(keyword).append("\\b").append("|");
		}
		buff.deleteCharAt(buff.length() - 1);
		buff.append(")");
		HIGHLIGHT_DEF_REGEX = buff.toString();
	}

	public Konsole() {
		super();

		setLayout(new BorderLayout());

		createTopPanel();
		createLabel();
		createTextArea();

		createParser();
	}

	private void createParser() {
		parser = new MindTalk();
	}

	private void createTextArea() {
		createScrollPane();

		editor = new JTextPane();
		editor.setContentType("text/plain");
		editor.addKeyListener(this);
		sp.setViewportView(editor);
	}

	private void createScrollPane() {
		sp = new JScrollPane();
		add(sp, BorderLayout.CENTER);
	}

	private void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(topPanel, BorderLayout.NORTH);
	}

	private void createLabel() {
		title = new JLabel(String(EDIT));
		setTitleSize(20);
		topPanel.add(title);
	}

	public void setTitleSize(int size) {
		title.setFont(new Font(title.getFont().getName(), Font.BOLD, size));
	}

	public void updateTextHighlighting(int offset, int length, Color c,
			boolean bold) {
		StyleContext sc = StyleContext.getDefaultStyleContext();

		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);
		aset = sc.addAttribute(aset, StyleConstants.Bold, bold);

		editor.getStyledDocument().setCharacterAttributes(offset, length, aset,
				true);
	}

	public void clearTextHighlighting() {
		updateTextHighlighting(0, editor.getText().length(), Color.BLACK, false);
	}

	public void updateTextHighlighting(int offset, int length) {
		updateTextHighlighting(offset, length, DEFAULT_KEYWORD_COLOR, true);
	}

	public void runProgram(RobotControl ctr) {
		LOG.info("Start running program");
		stopped = false;

		if (parser != null) {
			try {
				parser.run(
						ctr,
						editor.getDocument().getText(0,
								editor.getDocument().getLength()));
			} catch (BadLocationException e) {
				LOG.warning("Fehler beim Parsen des Programms: "
						+ e.getMessage());
			}
		}

		stopped = true;
		Signals.sendSignal(Signals.RUN_FINISHED);
		LOG.info("Finished running program");
	}

	public void stopProgram() {
		if (!stopped) {
			parser.stop();
			stopped = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == editor) {
			colorizeAction();
		}
	}

	private void colorizeAction() {
		clearTextHighlighting();
		Pattern pattern = Pattern.compile(HIGHLIGHT_DEF_REGEX);
		Matcher match;
		try {
			match = pattern.matcher(editor.getDocument().getText(0,
					editor.getDocument().getLength()));
			while (match.find()) {
				updateTextHighlighting(match.start(),
						match.end() - match.start());
			}
		} catch (BadLocationException e) {
			LOG.warning("Fehler beim SyntaxHighlightig " + e.getMessage());
		}
	}

}
