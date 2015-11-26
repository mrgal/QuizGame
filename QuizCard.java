
public class QuizCard {
	private String question;
	private String [] answers;
	private String correctAnswer;
	private static int count = 0;
	public QuizCard(String question, String [] answers, String correctAnswer) {
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		count++;
		System.out.println("Card Created Successefull " + count);
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	
}
