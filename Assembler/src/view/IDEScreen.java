package view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.AnalysisError;
import view.custom.editorTabPane.JEditorTab;
import view.custom.editorTabPane.JEditorTabbedPane;
import view.custom.editorTabPane.JProgramEditor;
import view.custom.messages.MessageUtils;
import view.custom.statusBar.JStatusBar;
import view.custom.tree.FileTreeNode;
import view.custom.tree.FilesTreeModel;
import control.Compiler;

public class IDEScreen extends JFrame {

	/**
	 * JDK default serialVersionUID
	 */
	private static final long serialVersionUID = 642323458773422160L;
	private JTextArea lstConsole;
	private JMenuBar menuBar;
	private JSplitPane sptEditor;
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenu mnCompile;
	private JMenuItem mntmSave;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSelectAll;
	private JMenuItem mntmPaste;
	private JMenuItem mntmCopy;
	private JMenuItem mntmUndo;
	private JMenuItem mntmCut;
	private JMenuItem mntmDelete;
	private JMenuItem mntmExport;
	private JMenuItem mntmCompile;
	private JStatusBar statusBar;
	private JToolBar toolBar;
	private JSpinner actFontSize;
	private JButton actExport;
	private JButton actCompile;
	private JButton actPaste;
	private JButton actCopy;
	private JButton actCut;
	private JButton actSave;
	private JButton actOpen;
	private JButton actNew;
	private JLabel lblFonte;
	private JMenu mnAbout;
	private JMenuItem mntmSobre;
	private JTree treeWS;

	private File workSpace;
	private JSplitPane sptRoot;
	private JEditorTabbedPane tbpnEditor;
	private JMenuItem mntmRedo;

	private List<Integer> stack;
	private JMenuItem mntmQuit;
	private JButton actUndo;
	private JButton actRedo;
	private JButton actClose;
	private JMenuItem mntmCloseFile;
	private JButton actDelete;
	private JMenuItem mntmDeletefile;

	public IDEScreen() {
		super();
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IDEScreen.class.getResource("/view/images/Host24.gif")));
		setMinimumSize(new Dimension(800, 600));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				initialSizeComponents();
			}
		});
		setTitle("M++ Assembly Compiler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		statusBar = new JStatusBar();
		statusBar.setMessage("");
		statusBar.setPreferredSize(new Dimension(14, 21));
		statusBar.setMinimumSize(new Dimension(14, 21));
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		toolBar = new JToolBar("Editor Actions");
		getContentPane().add(toolBar, BorderLayout.NORTH);

		actNew = new JButton("");
		actNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
			}
		});
		actNew.setToolTipText("Novo (Ctrl + N)");

		URL imageURL = null;
		actNew.setActionCommand("new");
		imageURL = IDEScreen.class.getResource("images/Add24.gif");

		if (imageURL != null) {
			actNew.setIcon(new ImageIcon(imageURL, "Novo"));
		} else {
			actNew.setText("Novo");
			System.err.println("Resource not found images/Add24.gif");
		}
		toolBar.add(actNew);

		actOpen = new JButton("");
		actOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switchWorkspace();
			}
		});
		actOpen.setToolTipText("Trocar Workspace (Ctrl + W)");
		actOpen.setActionCommand("open");
		imageURL = IDEScreen.class.getResource("images/Open24.gif");

		if (imageURL != null) {
			actOpen.setIcon(new ImageIcon(imageURL, "Abrir"));
		} else {
			actOpen.setText("Abrir");
			System.err.println("Resource not found images/Open24.gif");
		}
		toolBar.add(actOpen);

		actSave = new JButton("");
		actSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		actSave.setToolTipText("Salvar (Ctrl + S)");
		actSave.setActionCommand("save");
		imageURL = IDEScreen.class.getResource("images/Save24.gif");

		if (imageURL != null) {
			actSave.setIcon(new ImageIcon(imageURL, "Salvar (Ctrl + N)"));
		} else {
			actSave.setText("Salvar");
			System.err.println("Resource not found images/Save24.gif");
		}
		toolBar.add(actSave);

		actClose = new JButton("");
		actClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTab();
			}
		});
		actClose.setActionCommand("close");
		actClose.setToolTipText("Fechar arquivo (Ctrl + F)");
		imageURL = IDEScreen.class.getResource("images/Import24.gif");

		if (imageURL != null) {
			actClose.setIcon(new ImageIcon(imageURL, "Fechar (Ctrl + F)"));
		} else {
			actClose.setText("Fechar arquivo");
			System.err.println("Resource not found images/Import24.gif");
		}
		toolBar.add(actClose);

		actDelete = new JButton("");
		actDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteFile();
			}
		});
		actDelete.setActionCommand("deleteFile");
		actDelete.setToolTipText("Deletar arquivo (Ctrl + Shift + DEL)");
		imageURL = IDEScreen.class.getResource("images/Delete24.gif");

		if (imageURL != null) {
			actDelete.setIcon(new ImageIcon(imageURL,
					"Deletar arquivo (Ctrl + Shift + DEL)"));
		} else {
			actDelete.setText("Deletar arquivo");
			System.err.println("Resource not found images/Delete24.gif");
		}
		toolBar.add(actDelete);

		actCut = new JButton("");
		actCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cut();
			}
		});
		actCut.setToolTipText("Recortar (Ctrl + X)");
		actCut.setActionCommand("cut");
		imageURL = IDEScreen.class.getResource("images/Cut24.gif");

		if (imageURL != null) {
			actCut.setIcon(new ImageIcon(imageURL, "Recortar (Ctrl + X)"));
		} else {
			actCut.setText("Recortar");
			System.err.println("Resource not found images/Cut24.gif");
		}
		toolBar.add(actCut);

		actCopy = new JButton("");
		actCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});
		actCopy.setToolTipText("Copiar (Ctrl + C)");
		actCopy.setActionCommand("copy");
		imageURL = IDEScreen.class.getResource("images/Copy24.gif");

		if (imageURL != null) {
			actCopy.setIcon(new ImageIcon(imageURL, "Copiar (Ctrl + C)"));
		} else {
			actCopy.setText("Copiar");
			System.err.println("Resource not found images/Copy24.gif");
		}
		toolBar.add(actCopy);

		actPaste = new JButton("");
		actPaste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		});
		actPaste.setToolTipText("Colar (Ctrl + V)");
		actPaste.setActionCommand("paste");
		imageURL = IDEScreen.class.getResource("images/Paste24.gif");

		if (imageURL != null) {
			actPaste.setIcon(new ImageIcon(imageURL, "Colar (Ctrl + V)"));
		} else {
			actPaste.setText("Colar");
			System.err.println("Resource not found images/Paste24.gif");
		}
		toolBar.add(actPaste);

		actUndo = new JButton("");
		actUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		actUndo.setToolTipText("Desfazer (Ctrl + Z)");
		actUndo.setActionCommand("undo");
		imageURL = IDEScreen.class.getResource("images/Undo24.gif");

		if (imageURL != null) {
			actUndo.setIcon(new ImageIcon(imageURL, "Desfazer (Ctrl + Z)"));
		} else {
			actUndo.setText("Desfazer");
			System.err.println("Resource not found images/Undo24.gif");
		}
		toolBar.add(actUndo);

		actRedo = new JButton("");
		actRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		actRedo.setToolTipText("Refazer (Ctrl + Y)");
		actRedo.setActionCommand("redo");
		imageURL = IDEScreen.class.getResource("images/Redo24.gif");

		if (imageURL != null) {
			actRedo.setIcon(new ImageIcon(imageURL, "Refazer (Ctrl + Y)"));
		} else {
			actRedo.setText("Refazer");
			System.err.println("Resource not found images/Redo24.gif");
		}
		toolBar.add(actRedo);

		actCompile = new JButton("");
		actCompile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					compile();
				} catch (AnalysisError e1) {
					analisysException(e1);
				} catch (IOException e2) {
					e2.printStackTrace();
					MessageUtils
							.showErrorMessage("Problemas ao tentar ler o arquivo fonte. Causa: "
									+ e2.getMessage());
				}
			}
		});
		actCompile.setToolTipText("Compilar (F9)");
		actCompile.setActionCommand("compile");

		imageURL = IDEScreen.class.getResource("images/Play24.gif");

		if (imageURL != null) {
			actCompile.setIcon(new ImageIcon(imageURL, "Compilar (F9)"));
		} else {
			actCompile.setText("Compilar");
			System.err.println("Resource not found images/Play24.gif");
		}
		toolBar.add(actCompile);

		actExport = new JButton("");
		actExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToFile();
			}
		});
		actExport.setToolTipText("Exportar (Ctrl + F9)");
		actExport.setActionCommand("export");
		imageURL = IDEScreen.class.getResource("images/Export24.gif");

		if (imageURL != null) {
			actExport.setIcon(new ImageIcon(imageURL, "Exportar (Ctrl + F9)"));
		} else {
			actExport.setText("Exportar");
			System.err.println("Resource not found images/Export24.gif");
		}
		toolBar.add(actExport);

		lblFonte = new JLabel("Fonte: ");
		toolBar.add(lblFonte);

		actFontSize = new JSpinner();
		actFontSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setFontSize();
			}
		});
		actFontSize.setModel(new SpinnerNumberModel(new Integer(12), null,
				null, new Integer(1)));
		toolBar.add(actFontSize);

		sptRoot = new JSplitPane();
		sptRoot.setOneTouchExpandable(true);
		sptRoot.setDividerSize(12);
		getContentPane().add(sptRoot, BorderLayout.CENTER);

		sptEditor = new JSplitPane();
		sptRoot.setRightComponent(sptEditor);
		sptEditor.setDividerSize(12);
		sptEditor.setOneTouchExpandable(true);
		sptEditor.setOrientation(JSplitPane.VERTICAL_SPLIT);

		lstConsole = new JTextArea();
		lstConsole.setEditable(false);
		lstConsole.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lstConsole.setBorder(new TitledBorder(new CompoundBorder(null,
				UIManager.getBorder("CheckBoxMenuItem.border")), "Console",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lstConsole
				.setToolTipText("Resultados da compila\u00E7\u00E3o do arquivo.");
		sptEditor.setRightComponent(lstConsole);

		tbpnEditor = new JEditorTabbedPane();
		sptEditor.setLeftComponent(tbpnEditor);
		sptEditor.setDividerLocation(0.8d);

		treeWS = new JTree();
		treeWS.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				onFileSelected(e);
			}
		});
		sptRoot.setLeftComponent(treeWS);
		treeWS.setPreferredSize(new Dimension(200, 64));
		treeWS.setFont(new Font("SansSerif", Font.PLAIN, 12));
		treeWS.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("  ") {
			/**
				 * 
				 */
			private static final long serialVersionUID = -5377071600138020854L;

			{
			}
		}));

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("Arquivo");
		mnFile.setMnemonic('A');
		menuBar.add(mnFile);

		mntmNew = new JMenuItem("Novo");
		mntmNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
			}
		});
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem("Trocar Workspace");
		mntmOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switchWorkspace();
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Salvar");
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		mntmQuit = new JMenuItem("Sair");
		mntmQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (checkExit())
					dispose();
			}
		});

		mntmCloseFile = new JMenuItem("Fechar arquivo");
		mntmCloseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTab();
			}
		});
		mntmCloseFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmCloseFile);

		mntmDeletefile = new JMenuItem("Deletar arquivo");
		mntmDeletefile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteFile();
			}
		});
		mntmDeletefile.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, InputEvent.CTRL_MASK
						| InputEvent.SHIFT_MASK));
		mnFile.add(mntmDeletefile);
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmQuit);

		mnEdit = new JMenu("Edi\u00E7\u00E3o");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);

		mntmUndo = new JMenuItem("Desfazer");
		mntmUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});

		mntmRedo = new JMenuItem("Refazer");
		mntmRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmRedo);
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmUndo);

		mntmCut = new JMenuItem("Recortar");
		mntmCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cut();
			}
		});
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmCut);

		mntmCopy = new JMenuItem("Copiar");
		mntmCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmCopy);

		mntmPaste = new JMenuItem("Colar");
		mntmPaste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		});
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmPaste);

		mntmDelete = new JMenuItem("Excluir");
		mntmDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		mntmDelete
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		mnEdit.add(mntmDelete);

		mntmSelectAll = new JMenuItem("Selecionar Tudo");
		mntmSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmSelectAll);

		mnCompile = new JMenu("Compilacao");
		mnCompile.setMnemonic('C');
		menuBar.add(mnCompile);

		mntmCompile = new JMenuItem("Compilar");
		mntmCompile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					compile();
				} catch (AnalysisError e1) {
					analisysException(e1);
				} catch (IOException e2) {
					e2.printStackTrace();
					MessageUtils
							.showErrorMessage("Problemas ao ler o arquivo fonte. Causa: "
									+ e2.getMessage());
				}
			}
		});
		mntmCompile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		mnCompile.add(mntmCompile);

		mntmExport = new JMenuItem("Exportar");
		mntmExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToFile();
			}
		});
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,
				InputEvent.CTRL_MASK));
		mnCompile.add(mntmExport);

		mnAbout = new JMenu("Sobre");
		menuBar.add(mnAbout);

		mntmSobre = new JMenuItem("Sobre");
		mntmSobre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(null,
						"M++ Assembly Compiler v1.0" + System.lineSeparator()
								+ System.lineSeparator()
								+ "Projeto original: Jonathan Manoel Borges "
								+ System.lineSeparator()
								+ "Versão Java: Jean Jung", "Sobre",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnAbout.add(mntmSobre);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

				// if is to to exit then sets EXIT_ON_CLOSE action
				if (checkExit())
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

	/**
	 * Delete the selected file.
	 */
	private void deleteFile() {

		JEditorTabbedPane pane = this.getTbpnEditor();

		int i = pane.getSelectedIndex();

		if (i > -1) {

			String tabTitle = pane.getTitleAt(i).replace('*', ' ');

			int opcao = MessageUtils
					.showConfirmWarning("Tem certeza que deseja excluir o arquivo "
							+ tabTitle
							+ "?"
							+ System.lineSeparator()
							+ "Esta ação não poderá ser desfeita.");

			if (opcao == JOptionPane.YES_OPTION) {

				pane.deleteSelectedTab();
				this.loadWSFiles();
			}

		}

	}

	/**
	 * Closes the selected tab.
	 */
	private void closeTab() {

		this.getTbpnEditor().closeSelectedTab();
	}

	/**
	 * Sets the size of the font how the user select on the font size spinner.
	 */
	private void setFontSize() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null) {

			Object value = this.getActFontSize().getValue();

			if (value != null) {

				int size = (Integer) value;

				Style s = StyleContext.getDefaultStyleContext().getStyle(
						StyleContext.DEFAULT_STYLE);
				StyleConstants.setFontSize(s, size);

				try {
					tab.getEditor().applyRules();
				} catch (BadLocationException e) {
					// When this exception occurs its nothing that we have to
					// care, just the styles will not be applied.
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Check if any source is not saved and ask the user to out.
	 * 
	 * @return
	 */
	private boolean checkExit() {

		if (!this.getTbpnEditor().checkAllSaved()) {

			int option = MessageUtils
					.showConfirmWarning("Existem arquivos não salvos, deseja sair mesmo assim?");

			return option == JOptionPane.YES_OPTION;
		}

		return true;
	}

	/**
	 * Select all the text into the selected tab.
	 */
	private void selectAll() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null)
			tab.getEditor().selectAll();
	}

	/**
	 * Delete the selected text or the next character if no one is selected.
	 * Same of user press delete in any text editor.
	 */
	private void delete() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null) {

			try {
				tab.getEditor().delete();
			} catch (BadLocationException e) {
				// When this exception occurs its nothing that we have to care.
				e.printStackTrace();
			}
		}
	}

	/**
	 * Paste text on transfer area of the OS to the selected tab.
	 */
	private void paste() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null)
			tab.getEditor().paste();
	}

	/**
	 * Copy the selected text to transfer area of the OS.
	 */
	private void copy() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null)
			tab.getEditor().copy();
	}

	/**
	 * Cuts the selected text on selected tab.
	 */
	private void cut() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null)
			tab.getEditor().cut();
	}

	/**
	 * Redo last undoed edit.
	 */
	private void redo() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null)
			tab.getEditor().redo();
	}

	/**
	 * Undo the last change.
	 */
	private void undo() {

		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();

		if (tab != null)
			tab.getEditor().undo();
	}

	/**
	 * Compile the code and let the generated bytes on stack.
	 * 
	 * @throws AnalysisError
	 *             if any problem was found on user source than this exception
	 *             will be generated.
	 * @throws IOException
	 */
	private void compile() throws AnalysisError, IOException {
		// clean up.
		this.getLstConsole().setText("");
		this.stack = null;

		Compiler compiler = new Compiler();

		JEditorTab tab = ((JEditorTab) this.getTbpnEditor()
				.getSelectedComponent());

		if (tab != null) {

			tab.saveToFile();
			this.stack = compiler.compile(tab.getFile());
			this.getLstConsole().append("Compilado com sucesso.\n");
		}
	}

	/**
	 * Assembly the program and export it on the M+++ executable file.
	 */
	private void exportToFile() {

		try {
			this.compile();

			if (this.stack != null) {

				String fileContent = "v2.0 raw" + System.lineSeparator();

				for (int i = 2; i < this.stack.size(); i++) {

					Integer b = this.stack.get(i);

					if (b != null)
						fileContent += Integer.toHexString(b) + " ";

					// broke the row in 16 occurrences for easy reading the
					// generated file.
					if (i % 16 == 0)
						fileContent += System.lineSeparator();
				}

				FileTreeNode ftn = (FileTreeNode) this.getTreeWS()
						.getSelectionPath().getLastPathComponent();

				File f = (File) ftn.getUserObject();

				File fc = new File(f.getAbsolutePath()
						.replace(".mmmf", ".mmmp"));

				if (!fc.exists())
					fc.createNewFile();

				FileWriter fw = new FileWriter(fc, false);

				fw.write(fileContent);
				fw.close();

				this.getLstConsole().append(
						"Código do programa gerado com sucesso em "
								+ fc.getAbsolutePath());

				int option = MessageUtils
						.showConfirmWarning("Código do programa gerado com sucesso."
								+ System.lineSeparator()
								+ "Deseja abrir o local do arquivo?");

				if (option == JOptionPane.YES_OPTION)
					Desktop.getDesktop().open(fc.getParentFile());
			}

		} catch (AnalysisError e) {
			analisysException(e);
		} catch (IOException e) {
			e.printStackTrace();
			MessageUtils
					.showErrorMessage("Problemas ao gerar o executável, causa: "
							+ e.getMessage());
		}
	}

	/**
	 * Grabs the error of syntax or semantic of the user source and left he know
	 * by writing the log on the console and setting the position of the caret
	 * to where the error occurred.
	 * 
	 * @param e
	 *            The exception that was generated.
	 */
	private void analisysException(AnalysisError e) {

		e.printStackTrace();
		this.getLstConsole().append("Erro:\n" + e.getMessage() + "\n");
		JEditorTab tab = (JEditorTab) this.getTbpnEditor()
				.getSelectedComponent();
		JProgramEditor editor = tab.getEditor();
		editor.setCaretPosition(e.getPosition());
		editor.requestFocus();
	}

	/**
	 * Switch the current user workspace to another one that he chooses.
	 */
	private void switchWorkspace() {

		JFileChooser jfc = new JFileChooser();

		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setCurrentDirectory(this.getWorkSpace());
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// only directories
		jfc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "WorkSpace";
			}

			@Override
			public boolean accept(File f) {

				return f.isDirectory();
			}
		});

		int op = jfc.showOpenDialog(this);

		if (op == JFileChooser.APPROVE_OPTION) {

			File selectedFile = jfc.getSelectedFile();

			if (selectedFile != null) {

				this.getTbpnEditor().removeAll();
				this.getLstConsole().setText("");
				this.setWorkSpace(selectedFile);
				this.loadWSFiles();
				this.getStatusBar().setMessage("Wokspace trocado");
			}
		}
	}

	/**
	 * Saves the content of the editor into the associated file, if any.
	 */
	private void save() {

		this.getTbpnEditor().saveToFile();
		this.getStatusBar().setMessage("Arquivo salvo");
	}

	/**
	 * When a file was selected on the workspace tree view.
	 * 
	 * @param e
	 *            the event that was generated by the view.
	 */
	private void onFileSelected(TreeSelectionEvent e) {

		File file = null;

		if (e.getNewLeadSelectionPath() != null) {

			FileTreeNode ftn = (FileTreeNode) e.getNewLeadSelectionPath()
					.getLastPathComponent();

			file = (File) ftn.getUserObject();
		}

		this.switchSelectedFile(file);
	}

	/**
	 * Switch the selected tab to that contains the given file.
	 * 
	 * @param file
	 *            The file that must be selected.
	 */
	private void switchSelectedFile(File file) {

		if (file != null && !(file.isDirectory())) {

			this.getStatusBar()
					.setMessage("Arquivo: " + file.getAbsolutePath());
		}

		if (file != null && !file.isDirectory()) {

			int tabIdx = this.getTbpnEditor().indexOfTab(file);

			if (tabIdx == -1) {

				this.getTbpnEditor().addTab(file);

			} else {

				this.getTbpnEditor().setSelectedIndex(tabIdx);
			}
		}
	}

	/**
	 * Creates a new file on the selected workspace.
	 * 
	 * @return the success status in boolean format.
	 */
	private boolean newFile() {

		String fileName = JOptionPane
				.showInputDialog("Informe o nome do arquivo");

		if (fileName == null || "".equals(fileName))
			return false;
		else if (!fileName.endsWith(".mmmf"))
			fileName += ".mmmf";

		File newFile = new File(this.getWorkSpace().getAbsolutePath()
				+ File.separator + fileName);

		FileTreeNode tRoot = (FileTreeNode) this.getTreeWS().getModel()
				.getRoot();
		FileTreeNode newNode = new FileTreeNode(newFile);

		tRoot.add(newNode);
		((FilesTreeModel) this.getTreeWS().getModel()).reload();
		this.getTreeWS().setSelectionRow(tRoot.getChildCount());

		return true;
	}

	/**
	 * Load the workspace content on the tree view.
	 */
	private void loadWSFiles() {

		this.getTreeWS().setModel(
				new FilesTreeModel(new FileTreeNode(this.getWorkSpace()), true,
						new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {

								// Only files that was edited here.
								return name.endsWith(".mmmf");
							}
						}));
	}
	/**
	 * Adjust the initial size of the components.
	 */
	private void initialSizeComponents() {

		// Split do Editor
		this.getSptEditor().setDividerLocation(0.8d);

		// Split do Tree
		this.getSptRoot().setDividerLocation(0.2d);
	}

	public JTextArea getLstConsole() {
		return lstConsole;
	}

	@Override
	public JMenuBar getJMenuBar() {
		return menuBar;
	}

	public JSplitPane getSptEditor() {
		return sptEditor;
	}

	public JMenu getMnFile() {
		return mnFile;
	}

	public JMenu getMnEdit() {
		return mnEdit;
	}

	public JMenu getMnCompile() {
		return mnCompile;
	}

	public JMenuItem getMnSobre() {
		return mnAbout;
	}

	public JMenuItem getMntmSave() {
		return mntmSave;
	}

	public JMenuItem getMntmNew() {
		return mntmNew;
	}

	public JMenuItem getMntmOpen() {
		return mntmOpen;
	}

	public JMenuItem getMntmSelectAll() {
		return mntmSelectAll;
	}

	public JMenuItem getMntmPaste() {
		return mntmPaste;
	}

	public JMenuItem getMntmCopy() {
		return mntmCopy;
	}

	public JMenuItem getMntmUndo() {
		return mntmUndo;
	}

	public JMenuItem getMntmCut() {
		return mntmCut;
	}

	public JMenuItem getMntmDelete() {
		return mntmDelete;
	}

	public JMenuItem getMntmExport() {
		return mntmExport;
	}

	public JMenuItem getMntmCompile() {
		return mntmCompile;
	}

	public JStatusBar getStatusBar() {
		return statusBar;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public JButton getBtnExport() {
		return actExport;
	}

	public JButton getActCompile() {
		return actCompile;
	}

	public JButton getActPaste() {
		return actPaste;
	}

	public JButton getActCopy() {
		return actCopy;
	}

	public JButton getActCut() {
		return actCut;
	}

	public JButton getActOpen() {
		return actOpen;
	}

	public JButton getActNew() {
		return actNew;
	}

	public JTree getTreeWS() {
		return treeWS;
	}

	public File getWorkSpace() {
		return workSpace;
	}

	public void setWorkSpace(File workSpace) {
		this.workSpace = workSpace;
		this.loadWSFiles();
	}

	public JSplitPane getSptRoot() {
		return sptRoot;
	}

	public JEditorTabbedPane getTbpnEditor() {
		return tbpnEditor;
	}

	public JSpinner getActFontSize() {
		return actFontSize;
	}
}
