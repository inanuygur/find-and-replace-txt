/* 
 * This projects main purpose is to make a "Find and Replace" operation on '.txt' files.
 * Project is created using Eclipse IDE. So I currently don't know how to compile it but I have added the runnable jar file so that you can basically open and use it.
 * 
 * Follow the below steps:
 *    1. Choose a file by clicking "Search Button" or hust drag and drop a file on the "Search Button".
 *    2. Type a command in the text area which fits the instructions and press 'Enter'.
 *    3. Following result or output will be displayed on the below text area.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

public class MainClass {
	
	// initialization of global variables
	static JFrame mainFrame;
	static JButton searchButton;
	static JPanel searchButtonPanel;
	static JPanel textAreaPanel;
	static JTextArea textArea1;
	static JTextArea textArea2;
	static JTextArea textArea3;
	static JTextArea textArea4;
	static JScrollPane textScrollPane;
	static JFileChooser fileChooser;
	static File pickedFile;
	static String textArea1Text = "";
	
	// Main function
	public static void main(String[] args) {
		
		// Calling the function that will initiate variables
		Setup();
		
		// Calling the function that will setup main logic behind the interface
		Logic();

	}
	
	// Setting up default values for interface
	public static void Setup(){
		
		// A default border for several blocks
		Border border = new LineBorder(Color.black, 1);
		
		// Frame setup
		mainFrame = new JFrame("Find n Replace");
		mainFrame.setSize(600, 350);
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setLayout(new GridBagLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// textArea1 setup
		textArea1 = new JTextArea();
		textArea1.setLineWrap(true);
		textArea1.setBorder(border);
		
		// Button setup
		searchButton = new JButton();
		searchButton.setSize(25, 10);
		searchButton.setText("Search Button");
		
		// Panel behind the search button
		searchButtonPanel = new JPanel();
		searchButtonPanel.setSize(200, 40);
		searchButtonPanel.add(searchButton);
		
		// Scroll Pane behind the textArea1
		textScrollPane = new JScrollPane(textArea1);
		textScrollPane.setPreferredSize(new Dimension(200, 40));
		textScrollPane.setMinimumSize(new Dimension(120, 20));
		
		//
		textAreaPanel = new JPanel();
		textAreaPanel.add(textScrollPane);
		
		// textArea2 setup
		textArea2 = new JTextArea();
		textArea2.setBackground(new Color(UIManager.getColor("control").getRGB()));
		String textArea2String = "  Instructions \n";
		textArea2String += "--------------------------\n";
		textArea2String += " # to Find -> 'F' 'word to be found'\n";
		textArea2String += " # to Replace -> 'R' 'word to be found' and 'replacement word'\n";
		textArea2String += " # to Delete -> 'D' 'word to be found and deleted'\n";
		textArea2String += " # use '*' for any number of any letter in the word\n";
		textArea2String += " # use '-' for 1 number of any letter in the word\n";
		textArea2String += "--------------------------\n";
		textArea2String += " 1. Choose a text file.\n";
		textArea2String += " 2. Write a valid command at the above text area.\n";
		textArea2String += " 3. Press Enter.";
		textArea2.append(textArea2String);
		textArea2.setEditable(false);
		
		// textArea3 setup
		textArea3 = new JTextArea();
		textArea3.setBackground(new Color(UIManager.getColor("control").getRGB()));
		textArea3.setText("Current File: " + ((pickedFile == null) ? "Seçili dosya yok. Dosya seçiniz." : pickedFile));
		textArea3.setBorder(border);
		textArea3.setLineWrap(true);
		textArea3.setPreferredSize(new Dimension(250, 20));
		textArea3.setMinimumSize(new Dimension(250, 20));
		textArea3.setEditable(false);
		
		// textArea4 setup
		textArea4 = new JTextArea();
		textArea4.setBackground(new Color(UIManager.getColor("control").getRGB()));
		textArea4.setText("Welcome");
		textArea4.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		textArea4.setEditable(false);
		
		// Search text input 
		GridBagConstraints textArea1Constrains = new GridBagConstraints();
		textArea1Constrains.gridx = 0;
		textArea1Constrains.gridy = 0;
		textArea1Constrains.ipady = 50;
		textArea1Constrains.fill = GridBagConstraints.HORIZONTAL;
		
		// Search button panel
		GridBagConstraints searchButtonPanelConstrains = new GridBagConstraints();
		searchButtonPanelConstrains.gridx = 1;
		searchButtonPanelConstrains.gridy = 0;
		searchButtonPanelConstrains.anchor = GridBagConstraints.NORTH;
		searchButtonPanelConstrains.fill = GridBagConstraints.HORIZONTAL;
		
		// instructions 
		GridBagConstraints textArea2Constrains = new GridBagConstraints();
		textArea2Constrains.gridx = 0;
		textArea2Constrains.gridy = 1;
		textArea2Constrains.ipady = 30;
		textArea2Constrains.fill = GridBagConstraints.HORIZONTAL;
		
		// File status
		GridBagConstraints textArea3Constrains = new GridBagConstraints();
		textArea3Constrains.gridx = 1;
		textArea3Constrains.gridy = 0;
		textArea3Constrains.ipady = 20;
		textArea3Constrains.anchor = GridBagConstraints.SOUTH;
		textArea3Constrains.fill = GridBagConstraints.HORIZONTAL;

		// information area
		GridBagConstraints textArea4Constrains = new GridBagConstraints();
		textArea4Constrains.gridx = 0;
		textArea4Constrains.gridy = 2;
		textArea4Constrains.ipady = 20;
		textArea4Constrains.gridwidth = 3;
		textArea4Constrains.anchor = GridBagConstraints.PAGE_END;
		textArea4Constrains.fill = GridBagConstraints.HORIZONTAL;
		
		// Adding components to the mainFrame
		mainFrame.add(textAreaPanel, textArea1Constrains);
		mainFrame.add(searchButtonPanel, searchButtonPanelConstrains);
		mainFrame.add(textArea2, textArea2Constrains);
		mainFrame.add(textArea3, textArea3Constrains);
		mainFrame.add(textArea4, textArea4Constrains);
		
		
		// 
		fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		
		// 
		mainFrame.setVisible(true);
	}
	
	// Setting logic behind the interface
	static void Logic() {
		
		/*
		 * Adding key listener to the textArea1 for listening 'Enter' key from user.
		 * 	- When 'Enter' key is pressed, an algorithm runs using the textArea1 text
		 * 		and does the specific search on the file if conditions are met.
		 */
		textArea1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
								
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
				    e.consume();
				    textArea1Text = textArea1.getText();
				    
				    // Search the file if file and word that will be searched is okay.
				    if(isTextAreaTextValid(textArea1Text) && (pickedFile != null)) {
				    	runAction(textArea1Text.split(" ", 0)[0].charAt(0), textArea1Text, pickedFile);
				    } else if(pickedFile == null) {
				    	textArea4.setText("You need to pick a file first.");
				    } else if(!isTextAreaTextValid(textArea1Text)) {
				    	textArea4.setText("Command does not fit the format. Check the instructions.");
				    } else {
				    	textArea4.setText("Something is wrong.");
				    }
			    }
			}
			
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		/*
		 *  Adding Action Listener to the "Search Button"
		 *   - This action listener will help user to choose the file that will be
		 *   worked on by opening a File Choosing window.
		 *   - This function accepts the selected file if the file has '.txt' extension.
		 */
		
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("searchButton is activated.");
				int fileChooserValue = fileChooser.showOpenDialog(searchButton);
				
				if (fileChooserValue == JFileChooser.APPROVE_OPTION){
					File selectedFile = fileChooser.getSelectedFile();
					
					if(getFileExtension(selectedFile).equals("txt")) {
						pickedFile = selectedFile;
						textArea3.setText("Current File: " + pickedFile.getName());
					} else {
						textArea3.setText("Please select or drop a file with '.txt' extension.");
						textArea4.setText("Please select or drop a file with '.txt' extension.");
					}
					
				} else {
					textArea4.setText("File Choosing is interupted.");
				}
			}
			
		});
		
		/*
		 * Adding Drag and Drop to the "Search Button"
		 *  - This drag and drop function helps user to choose a file easier than using file chooser.
		 *  - This function accepts the selected file if the file has '.txt' extension.
		 */
		searchButton.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent event) {
				for(DataFlavor dataFlover : event.getCurrentDataFlavors()) {
					if(dataFlover.isFlavorJavaFileListType()) {
						try {
							event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
							for(File dropedFile : 
								(List<File>)event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)) {
								
								if(dropedFile.isFile() && dropedFile.canRead()) {
									
									if(getFileExtension(dropedFile).equals("txt")) {
										System.out.println("file extension: " + getFileExtension(dropedFile));
										pickedFile = dropedFile;
										textArea3.setText("Current File: " + pickedFile.getName());
									} else {
										textArea3.setText("Please select or drop a file with '.txt' extension.");
										textArea4.setText("Please select or drop a file with '.txt' extension.");
									}
									break;
								}
							}
						} catch (UnsupportedFlavorException | IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	/*
	 *  This function takes a File as its input and returns the extension of that file as String.
	 */
	static String getFileExtension(File file) {
		String fileExtension = "";
		String[] fileNames = file.getName().split("\\.");
		if(fileNames.length > 0) {
			if(fileNames[fileNames.length - 1].equals("txt")) {
				fileExtension = fileNames[fileNames.length - 1];
			}
		}
		return fileExtension;
	}
	
	/*
	 * This function takes user's text input and runs different processes according to 
	 * the command letter.
	 */
	static void runAction(char action, String textAreaText, File file) {
		try {
			switch (action) {
			case 'F': {
				String word = textAreaText.split(" ", 0)[1];
				int occurenceNumber = countOccurence(pickedFile, word);
				textArea4.setText("Occurence number of word \"" + word + "\" is " + occurenceNumber + ".");
				break;
			}
			case 'R': {
				String wordThatWillBeChanged = textAreaText.split(" ", 0)[1];
				String newWord = textAreaText.split(" ", 0)[2];
				textArea4.setText(countOccurence(pickedFile, wordThatWillBeChanged) + " number of word \"" + wordThatWillBeChanged + "\" found and replaced with \"" + newWord + "\".");
				findAndReplaceWords(file, wordThatWillBeChanged, newWord);
				break;
			}
			case 'D': {
				String wordThatWillBeDeleted = textAreaText.split(" ", 0)[1];
				textArea4.setText(countOccurence(pickedFile, wordThatWillBeDeleted) + " number of word \"" + wordThatWillBeDeleted + "\" found and deleted.");
				findAndReplaceWords(file, wordThatWillBeDeleted, "");
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected action value: " + action);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * This function checks if the user's text input is valid and fits the format.
	 */
	static boolean isTextAreaTextValid(String text) {
		
		String texts[] = text.split(" ", 0);
		
		if(!(texts[0].equals("F") || texts[0].equals("R") || texts[0].equals("D"))) {
			textArea4.setText("Given text does not fit the format. Check the instructions.");
			return false;
		} 
		else if(texts[0].equals("R") && (texts.length != 3)) {
			textArea4.setText("Given text does not fit the format. Check the instructions.");
			return false;
		}
		else if((texts[0].equals("F") || texts[0].equals("D")) && (texts.length != 2)) {
			textArea4.setText("Given text does not fit the format. Check the instructions.");
			return false;
		}
		
		return true;
	}
	
	/*
	 * This function reads the picked file line by line searches for the given text.
	 *  - In order to match the words, the special characters that are given in the 
	 *  instruction are changed with some Regular Expression characters. 
	 */
	public static int countOccurence(File file, String word) throws FileNotFoundException {
		int counter = 0;
		
		try {
			BufferedReader bufRead = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
			String fileRow;
			
			while((fileRow = bufRead.readLine()) != null) {
				
				String splitedWords[] = fileRow.split(" ", 0);
				for(String currentWord : splitedWords) {
					
					String searchWord = word.replaceAll("-", "\\.");
					searchWord = searchWord.replaceAll("\\*", "\\.*");
					if(currentWord.matches(searchWord)) {
						counter++;
					}
				}
			}
			
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return counter;
	}
	
	/* 
	 * This function scans the file and searches the word it takes as its input.
	 * Returns a List of words (String) that matches the input.
	 *  - This function used for 'R' and 'D' operations when users input has '*' and '-' characters
	 *  	in order to detect which words are going to be replaced or deleted. 
	 */
	public static List<String> detectWords(File file, String searchWord) {
		List<String> detectedWords = new ArrayList<String>();
		try {
			BufferedReader bufRead = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
			String fileRow;
			while((fileRow = bufRead.readLine()) != null) {
				String splitedWords[] = fileRow.split(" ", 0);
				for(String currentWord : splitedWords) {
					if(currentWord.matches(searchWord)) {
						System.out.println("currentWord: " + currentWord + "\tsearchWord: "+ searchWord);
						detectedWords.add(currentWord);
					}
				}
			}
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return detectedWords;
	}
	
	/* 
	 * This function takes the file and reads all of its bytes and stores them in a String.
	 * Uses "detectWord" function to detect the words that will be replaced or deleted.
	 * 		- Above operation is done because user might use '*' or '-' as input, 
	 * 			so detecting the word that are matching the user's choice first will clear any errors.
	 */
	public static void findAndReplaceWords(File file, String wordThatWillBeChanged, String newWord) {
		try {
			String allContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			
			String searchWord = wordThatWillBeChanged.replaceAll("-", "\\."); 
			// '-' will be changed to '.' which means any letter.
			searchWord = searchWord.replaceAll("\\*", "\\.*");
			// '*' will be changed to '.*' which means and number of any letters.
			
			List<String> detectedWord = detectWords(file, searchWord);
			for(String currentWord : detectedWord) {
				currentWord = (newWord.equals("")) ? currentWord + "\\s": currentWord;
				currentWord = (newWord.equals("")) ? currentWord : "\\b" + currentWord + "\\b";
;				allContent = allContent.replaceAll(currentWord, newWord);
			}
			
			System.out.println("\n\nnew allContent: " + allContent);
			Files.write(file.toPath(), allContent.getBytes(StandardCharsets.UTF_8));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("findAndReplaceWords done.");
	}
	
	/* 
	 * This function basically prints out the file, but it is not used anywhere in the project.
	 */
	public static void readAndDisplayFile(File file) throws FileNotFoundException {
		
		try {
			BufferedReader bufRead = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
			String fileRow;
			
			while((fileRow = bufRead.readLine()) != null) {
				System.out.println(fileRow);
			}
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// END of CODE
