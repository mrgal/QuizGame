
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.*;


public class QuizGame {
	private JFrame frame;
	private JButton nextButton;
	private JButton loadButton;
	private ArrayList<QuizCard> cardList;
	private QuizCard currentCard;
	private JTextArea questionDisplay;
	private JLabel[] answer;
	private JRadioButton [] radioAnsw;
	private JProgressBar progBar;
	private int numOfQuestions;
	private int currentCardIndex;
	private ButtonGroup radioGroup;
	private int numOfCorrectAnswers;
	
	public static void main(String[] args) {
		new QuizGame().goGUI();
	}
	
	public void goGUI(){
		frame = new JFrame("Quiz Game 1.0");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    	//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo2.png")));
		cardList = new ArrayList<QuizCard>();
		questionDisplay = new JTextArea(10,20);
		questionDisplay.setEditable(false);
		questionDisplay.setWrapStyleWord(true);
		questionDisplay.setLineWrap(true);
		JScrollPane  scrollP = new JScrollPane(questionDisplay);
		scrollP.setBorder(BorderFactory.createTitledBorder("Question:"));
		scrollP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel progressP = new JPanel();
		JLabel progLabel = new JLabel("Progress: ");
		progBar = new JProgressBar();
		progBar.setStringPainted(true);
		progBar.setMinimum(0);
		progressP.add(progLabel);
		progressP.add(progBar);
		
		radioGroup = new ButtonGroup();
		radioAnsw = new JRadioButton[4];
		for (int i = 0; i < 4; i++) {
			radioAnsw[i] = new JRadioButton(1+ i + "");
			radioGroup.add(radioAnsw[i]);
		}
		
		answer = new JLabel[4];
		for (int i = 0; i < 4; i++) {
			answer[i] = new JLabel("Some Text goes here");
		}
				
		JPanel answPanel = new JPanel();
		answPanel.setLayout(new GridBagLayout());
		answPanel.setBackground(new Color(153,204,255));
		answPanel.setBorder(BorderFactory.createTitledBorder("Choose one of the answers:"));
		for (int i = 0; i < 4; i++) {
			answPanel.add(radioAnsw[i], new GridBagConstraints(0, i, 1, 1, 1, 1,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(2, 2, 2, 2), 0, 0));
			radioAnsw[i].setBackground(new Color(153,204,255));
			answPanel.add(answer[i], new GridBagConstraints(1, i, 1, 1, 1, 1,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 2, 2, 2), 0, 0));
			
		}
		answPanel.setPreferredSize(new Dimension(300, 100));
		
		loadButton = new JButton("Load Qiuz");
		loadButton.addActionListener(new LoadActionListener());
		nextButton = new JButton("Next Question");
		nextButton.addActionListener(new NextActionListener());
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.add(loadButton);
		buttonP.add(Box.createRigidArea(new Dimension(40,0)));
		buttonP.add(nextButton);
		
		frame.add(scrollP);
		frame.add(progressP);
		frame.add(answPanel);
		frame.add(buttonP);
		frame.setSize(320,400);
		frame.setVisible(true);
		
	}
	
	class NextActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentCardIndex < numOfQuestions){
				isCorrect();
				showNextCard();
			}else if(currentCardIndex == numOfQuestions){
				isCorrect();
				nextButton.setText("Show Result");
				JOptionPane.showMessageDialog(frame, "The number of correct answers is: " + numOfCorrectAnswers, "Result", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}
		
	}
	
	private void isCorrect(){
		for(int i = 0; i < 4; i++){
			if(radioAnsw[i].isSelected()){
				if(answer[i].getText().equals(currentCard.getCorrectAnswer())) {
					numOfCorrectAnswers++;
				}
			}
				
		}
	}
	
	class LoadActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);
			loadFile(fileOpen.getSelectedFile());
			showNextCard();
			setNumberOfQuestions(cardList.size());
			progBar.setMaximum(getNumberOfQuestions());
		}
		
	}
	
	// A method that load data from a choosen file on the disk
	private void loadFile(File file){
		
		try {
			BufferedReader readerB = new BufferedReader(new FileReader(file));
			cardList.clear();
			currentCardIndex = 0;
			numOfCorrectAnswers = 0;
			String line = null;
			while ((line = readerB.readLine()) != null) {
				makeCard(line);
			}
			readerB.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void makeCard(String lineToParse){
		String [] result = lineToParse.split("/");
		String question = result[0];
		String correctAnswer = result[5];
		String [] answers = new String[4];
		for(int j = 0; j < 4; j++){
			answers[j] = result[j+1];
		}
		QuizCard card = new QuizCard(question, answers, correctAnswer);
		
		cardList.add(card);
		
	}
	
	private void setNumberOfQuestions(int numOfQuestions){
		this.numOfQuestions = numOfQuestions; 
	}
	
	public int getNumberOfQuestions(){
		return numOfQuestions;
	}
	
	private void showNextCard(){
		currentCard = cardList.get(currentCardIndex);
		currentCardIndex++;
		questionDisplay.setText(currentCard.getQuestion());
		String[] ansVariants = currentCard.getAnswers();
		for(int i = 0; i < 4; i++){
			answer[i].setText(ansVariants[i]);
		}
		progBar.setValue(currentCardIndex);
	}
}
