package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import util.EntireFileReader;
import view.custom.messages.MessageUtils;

public class InitialConfigScreen extends JDialog {

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = 5987403725951973127L;
	private JPanel pnlOkCancel;
	private JPanel pnlCombo;
	private JButton btnOk;
	private JButton btnExit;
	private JComboBox<String> cmbWorkSpace;
	private JButton btnChoose;
	private File workSpace;
	private final File arquivoConfig = new File(System.getProperty("user.dir") + File.separator + ".workspacelist");

	/**
	 * Creates a Configuration Screen.
	 */
	public InitialConfigScreen() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				onWindowOpened();
			}
		});
		setAlwaysOnTop(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setType(Type.UTILITY);
		setTitle("Selecione o WorkSpace");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlOkCancel = new JPanel();
		getContentPane().add(pnlOkCancel, BorderLayout.SOUTH);
		pnlOkCancel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickOk(); 
			}
		});
		pnlOkCancel.add(btnOk);
		
		btnExit = new JButton("Sair");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pnlOkCancel.add(btnExit);
		
		pnlCombo = new JPanel();
		getContentPane().add(pnlCombo, BorderLayout.CENTER);
		GridBagLayout gbl_pnlCombo = new GridBagLayout();
		gbl_pnlCombo.columnWidths = new int[]{154, 422, 61, 0};
		gbl_pnlCombo.rowHeights = new int[]{22, 0, 0, 0};
		gbl_pnlCombo.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_pnlCombo.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlCombo.setLayout(gbl_pnlCombo);
		
		JLabel lblWorkspace = new JLabel("WorkSpace: ");
		GridBagConstraints gbc_lblWorkspace = new GridBagConstraints();
		gbc_lblWorkspace.anchor = GridBagConstraints.EAST;
		gbc_lblWorkspace.insets = new Insets(0, 0, 5, 5);
		gbc_lblWorkspace.gridx = 0;
		gbc_lblWorkspace.gridy = 1;
		pnlCombo.add(lblWorkspace, gbc_lblWorkspace);
		
		cmbWorkSpace = new JComboBox<String>();
		
		cmbWorkSpace.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbWorkSpace.setEditable(true);
		GridBagConstraints gbc_cmbWorkSpace = new GridBagConstraints();
		gbc_cmbWorkSpace.insets = new Insets(0, 0, 5, 5);
		gbc_cmbWorkSpace.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbWorkSpace.anchor = GridBagConstraints.NORTH;
		gbc_cmbWorkSpace.gridx = 1;
		gbc_cmbWorkSpace.gridy = 1;
		pnlCombo.add(cmbWorkSpace, gbc_cmbWorkSpace);
		
		btnChoose = new JButton("...");
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClickChoose(); 
			}
		});
		GridBagConstraints gbc_btnChoose = new GridBagConstraints();
		gbc_btnChoose.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChoose.insets = new Insets(0, 0, 5, 0);
		gbc_btnChoose.gridx = 2;
		gbc_btnChoose.gridy = 1;
		pnlCombo.add(btnChoose, gbc_btnChoose);
	}
	
	/**
	 * Carrega os dados necessários quando a Screen for mostrada.
	 */
	private void onWindowOpened() {
		
		for (String linha : this.readConfigFile()) {
			
			this.getCmbWorkSpace().addItem(linha);
			this.getCmbWorkSpace().setSelectedIndex(this.getCmbWorkSpace().getItemCount() - 1);
		}
	}
	
	/**
	 * Ações quando o botão OK for clicado.
	 */
	private void onClickOk() { 
		
		String wsPath = (String)this.getCmbWorkSpace().getSelectedItem();
		
		this.setWorkSpaceSelected(wsPath);
		
		Set<String> linhasConfig = readConfigFile();
		
		if (!linhasConfig.contains(wsPath)) {
			
			this.writeConfigFile(wsPath);
		}
		
		this.dispose();
	}
	
	/**
	 * Escreve uma nova linha no arquivo
	 * @param linha
	 */
	private void writeConfigFile(String linha) {
		
		File configFile = this.getArquivoConfig();
		
		if (configFile != null) {
			
			FileWriter fw = null;
			try {
				
				fw = new FileWriter(configFile, true);
				fw.write(System.lineSeparator() + linha);
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			} finally {
				
				try { fw.close(); } catch (IOException e) { e.printStackTrace(); }
			}
			
		}
	}
	
	/**
	 * Retorna todas as linhas do arquivo de configuração em uma lista.
	 * @return 
	 */
	private Set<String> readConfigFile() {
		
		File configFile = this.getArquivoConfig();
		
		if (configFile != null) {
			
			EntireFileReader efr = null; 
			try {
				
				efr = new EntireFileReader(configFile);
				
				Set<String> linhas = efr.readAllLines();
				
				return linhas;
				
			} catch (IOException e) {
				
				e.printStackTrace();
				MessageUtils.showErrorMessage("Problemas ao ler o arquivo de configuração. Causa:" + e.getMessage());
				
			} finally {
				
				try { efr.close(); } catch (IOException e) { e.printStackTrace(); }
			}
			
		}
		
		return null;
	}
	
	/**
	 * Localiza o arquivo de configuração onde são gravados os workspaces já selecionados.
	 * @return
	 */
	private File getArquivoConfig() {
		
		File configFile = this.arquivoConfig;
		
		if (!configFile.exists())
			
			try {
				
				configFile.createNewFile();
			
			} catch (IOException e) { e.printStackTrace(); }
		
		return configFile;
	}
	
	/**
	 * Executar as ações para escolha do arquivo.
	 */
	private void onClickChoose() {
		
		JFileChooser jfc = new JFileChooser();
		
		jfc.setAcceptAllFileFilterUsed(false);  
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// só diretórios
		jfc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Diretório";
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
				
				this.getCmbWorkSpace().addItem(selectedFile.getAbsolutePath());
				this.getCmbWorkSpace().setSelectedItem(selectedFile.getAbsolutePath());
				this.setWorkSpace(selectedFile);
			}
		}
	}
	
	/**
	 * Seta o diret�rio selecionado.
	 * @param path
	 */
	private void setWorkSpaceSelected(String path) { 
		
		File ws = new File(path);
		
		if (!ws.exists()) 
			ws.mkdirs();
		
		this.setWorkSpace(ws);
	}
	
	public JPanel getPnlOkCancel() {
		return pnlOkCancel;
	}
	public JButton getBtnOk() {
		return btnOk;
	}
	public JButton getBtnExit() {
		return btnExit;
	}
	public JPanel getPnlCombo() {
		return pnlCombo;
	}
	public JComboBox<String> getCmbWorkSpace() {
		return cmbWorkSpace;
	}
	public JButton getBtnChoose() {
		return btnChoose;
	}
	public File getWorkSpace() {
		return workSpace;
	}
	public void setWorkSpace(File workSpace) {
		this.workSpace = workSpace;
	}
}
