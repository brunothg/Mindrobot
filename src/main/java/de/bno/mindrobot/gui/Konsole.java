package de.bno.mindrobot.gui;

import static de.bno.mindrobot.gui.Strings.CMD_LINKS;
import static de.bno.mindrobot.gui.Strings.CMD_RECHTS;
import static de.bno.mindrobot.gui.Strings.CMD_RUECKWAERTS;
import static de.bno.mindrobot.gui.Strings.CMD_VORWAERTS;
import static de.bno.mindrobot.gui.Strings.EDIT;
import static de.bno.mindrobot.gui.Strings.LOGGING;
import static de.bno.mindrobot.gui.Strings.QU_HINDERNIS;
import static de.bno.mindrobot.gui.Strings.QU_VERWIRRT;
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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import de.bno.mindrobot.MindRobot;
import de.bno.mindrobot.data.exporter.JavaExporter;
import de.bno.mindrobot.data.exporter.ScriptExporter;
import de.bno.mindrobot.data.spielfeld.SpielfeldData;
import de.bno.mindrobot.gui.parser.MindTalk;
import de.bno.mindrobot.gui.parser.Parser;

public class Konsole extends JPanel implements KeyListener, MouseListener,
		SignalListener {

	private static final Logger LOG = MindRobot.getLogger(Konsole.class);

	private static final long serialVersionUID = -6470828939746541026L;

	private JLabel title;

	private JPanel topPanel;
	private JSplitPane centerPanel;
	private JSplitPane centerRightPanel;

	private JScrollPane sp;

	private JTextPane editor;

	private Parser parser;

	private PlaygroundPreview preview;

	private boolean stopped;

	private boolean isFirstTimePaint = true;

	private JBufferedTextArea logArea;

	private static final String[] HIGHLIGHT_DEF = new String[] {
			String(SYNTAX_WENN), String(SYNTAX_DANN), String(SYNTAX_SONST),
			String(SYNTAX_ENDE), String(SYNTAX_WIEDERHOLE),
			String(SYNTAX_SOLANGE) };

	private static final Color DEFAULT_KEYWORD_COLOR = new Color(149, 0, 85);
	public static String HIGHLIGHT_DEF_REGEX;

	private JScrollPane logScrollPane;

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
		this(null);
	}

	public Konsole(PlaygroundPreview preview) {
		super();

		this.preview = preview;

		setLayout(new BorderLayout());

		createTopPanel();

		createCenterPanel();

		createParser();

		addListener();

	}

	private void createCenterPanel() {
		centerPanel = new JSplitPane();
		centerPanel.setDividerSize(Pixel.pointsToPixel(10));
		centerPanel.setDividerLocation(0.7);
		centerPanel.setOneTouchExpandable(true);
		centerPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		add(centerPanel, BorderLayout.CENTER);

		createTextArea();
		createCenterRightPanel();
	}

	private void createCenterRightPanel() {
		centerRightPanel = new JSplitPane();
		centerRightPanel.setDividerSize(Pixel.pointsToPixel(15));
		centerRightPanel.setDividerLocation(0.7);
		centerRightPanel.setOneTouchExpandable(true);
		centerRightPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		centerPanel.setRightComponent(centerRightPanel);

		createPreview();
		createRightBottom();
	}

	private void createRightBottom() {
		logScrollPane = new JScrollPane();
		logArea = new JBufferedTextArea(5000);
		logArea.setEditable(false);
		logScrollPane.setBorder(BorderFactory
				.createTitledBorder(String(LOGGING)));
		logScrollPane.setViewportView(logArea);
		centerRightPanel.setBottomComponent(logScrollPane);
	}

	private void createPreview() {
		if (preview == null) {
			preview = new PlaygroundPreview(null);
		}
		centerRightPanel.setTopComponent(preview);
	}

	private void addListener() {
		Signals.addListener(this);
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

		editor.addMouseListener(this);
	}

	private void createScrollPane() {
		sp = new JScrollPane();
		centerPanel.setLeftComponent(sp);
	}

	private void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(topPanel, BorderLayout.NORTH);

		createLabel();
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

		try {
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
		} catch (Exception e) {
		} finally {
			stopped = true;
			Signals.sendSignal(Signals.SIGNAL_RUN_FINISHED);
			LOG.info("Finished running program");
		}
	}

	public void stopProgram() {
		if (!stopped) {
			parser.stop();
			stopped = true;
		}
	}

	protected void paintComponent(Graphics g) {
		if (isFirstTimePaint) {
			isFirstTimePaint = false;
			centerPanel.setDividerLocation(0.7);
			centerRightPanel.setDividerLocation(0.8);
		}
		super.paintComponent(g);
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		if (arg0.getSource() == editor) {
			if (arg0.isPopupTrigger()) {
				popUp(arg0.getX(), arg0.getY());
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		if (arg0.getSource() == editor) {
			if (arg0.isPopupTrigger()) {
				popUp(arg0.getX(), arg0.getY());
			}
		}

	}

	private void popUp(int x, int y) {
		new KonsolenPopup().show(editor, x, y);
	}

	@Override
	public boolean Signal(String signal, Object... values) {

		switch (signal) {
		case Signals.SIGNAL_KONSOLE_INSERT:
			insertCMD(values);
			return true;
		case Signals.SIGNAL_KONSOLE_LOG:
			konsolenLog(values);
			return true;
		case Signals.SIGNAL_EXPORT_AS:
			return exportScriptAs(values);
		default:
			break;
		}

		return false;
	}

	private boolean exportScriptAs(Object[] values) {
		String format = "";

		if (values == null || values.length < 1) {
			return false;
		}

		format = values[0].toString();

		LOG.info("Export script as " + format);

		ScriptExporter exporter;
		String filename;

		switch (format.toLowerCase()) {
		case "java":
			exporter = new JavaExporter();
			filename = "MindExport.java";
			break;
		default:
			exporter = null;
			filename = null;
			break;
		}

		if (exporter == null) {
			return false;
		}

		if (!MindRobot.isFullscreen()) {

			Path out = openPath(filename);

			if (out == null) {
				return true;
			}

			try {
				exporter.exportAs(editor.getText(), out);
			} catch (IOException e) {
				LOG.warning("Fehler beim exportieren des Scripts: "
						+ e.getMessage());
			}

		} else {
			final ScriptExporter _exporter = exporter;
			exporter = null;

			final JFileChooser fc = new JFileChooser();
			fc.setDialogType(JFileChooser.SAVE_DIALOG);

			if (filename != null) {
				fc.setSelectedFile(new File(filename));
			}

			fc.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String command = e.getActionCommand();
					if (command.equals(JFileChooser.APPROVE_SELECTION)) {
						File selectedFile = fc.getSelectedFile();
						Path out = selectedFile.toPath();

						if (out == null) {
							return;
						}

						try {
							_exporter.exportAs(editor.getText(), out);
						} catch (IOException e2) {
							LOG.warning("Fehler beim exportieren des Scripts: "
									+ e2.getMessage());
						} finally {
							sp.setViewportView(editor);
						}
					} else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
						sp.setViewportView(editor);
					}
				}
			});

			sp.setViewportView(fc);
		}

		return true;
	}

	private Path openPath(String name) {
		JFileChooser fc = new JFileChooser();

		if (name != null) {
			fc.setSelectedFile(new File(name));
		}

		File f = null;

		int ret = fc.showSaveDialog(editor);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		f = fc.getSelectedFile();

		return (f != null) ? f.toPath() : null;
	}

	private void konsolenLog(Object[] values) {
		for (Object obj : values) {
			logArea.appendText(obj.toString());
		}
	}

	private void insertCMD(Object[] values) {
		if (values == null || values.length < 1
				|| !(values[0] instanceof Insert)) {
			return;
		}

		Insert what = (Insert) values[0];

		String insertString = null;

		switch (what) {
		case WENN_DANN_SONST:
			insertString = String.format("%s ...?%n%s%n%n%s%n%n%s ",
					String(SYNTAX_WENN), String(SYNTAX_DANN),
					String(SYNTAX_SONST), String(SYNTAX_ENDE));
			break;
		case WIEDERHOLE_X:
			insertString = String.format("%s 1%n%n%s ",
					String(SYNTAX_WIEDERHOLE), String(SYNTAX_ENDE));
			break;
		case SOLANGE_WIE:
			insertString = String.format("%s ...?%n%n%s ",
					String(SYNTAX_SOLANGE), String(SYNTAX_ENDE));
			break;
		case VORWAERTS:
			insertString = String.format("%s. ", String(CMD_VORWAERTS));
			break;
		case RUECKWAERTS:
			insertString = String.format("%s. ", String(CMD_RUECKWAERTS));
			break;
		case LINKS:
			insertString = String.format("%s. ", String(CMD_LINKS));
			break;
		case RECHTS:
			insertString = String.format("%s. ", String(CMD_RECHTS));
			break;
		case HINDERNIS_Q:
			insertString = String.format("%s? ", String(QU_HINDERNIS));
			break;
		case VERWIRRT_Q:
			insertString = String.format("%s? ", String(QU_VERWIRRT));
			break;
		default:
			break;
		}

		if (insertString != null && !insertString.isEmpty()) {
			StyledDocument doc = editor.getStyledDocument();
			try {
				doc.insertString(editor.getCaretPosition(), insertString,
						SimpleAttributeSet.EMPTY);
			} catch (BadLocationException e) {
				LOG.warning("Fehler beim einfügen von Befehlen: "
						+ e.getMessage());
			}

			colorizeAction();
		}
	}

	public void setMapData(SpielfeldData spielfeld) {
		if (preview != null) {
			preview.setSpielfeld(spielfeld);
		}
	}
}
