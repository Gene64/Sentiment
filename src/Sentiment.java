/*
 * @author: Gene Pressinger (2016)
 * 
 * Collaboration:
 * Searched how to use string arrays and lists.
 * 
 */

import java.io.*;
import java.util.*;

public class Sentiment {

	static Scanner keyboard = new Scanner(System.in);
	final static List<String> BEST_WORDS = Arrays.asList("best", "favorite", "hilarious", "awesome", "impressive");
	final static List<String> GOOD_WORDS = Arrays.asList("good", "okay", "ok", "nice", "like", "decent", "funny", "little", "happy", "pleasant", "great", "cool");
	final static List<String> BAD_WORDS = Arrays.asList("bad", "poor", "careless", "hard", "imperfect", "substandard", "unpleasant", "negligent", "hurtful", "lousy");
	final static List<String> HORRIBLE_WORDS = Arrays.asList("horrible", "awful", "depressing", "terrible", "miserable");

	public static void main(String[] args) throws FileNotFoundException {
		printMenu();
	}

	// Get the average score of reviews that contains a word (Requirement #1)
	private static void getScoreOfWord() throws FileNotFoundException {
		File reviewFile = new File("movieReviews.txt");
		Scanner reviewScanner = new Scanner(reviewFile);

		System.out.println("Please enter a word:");
		String word = keyboard.nextLine();

		Double wordAppearance = 0.0;
		Double totalScore = 0.0;

		while (reviewScanner.hasNext()) {
			String currentLine = reviewScanner.nextLine();
			if (currentLine.contains(word)) {
				totalScore += reviewScanner.nextInt();
				wordAppearance++;
			}
		}
		reviewScanner.close();

		System.out.println(word + " appears " + totalScore + " times.");
		System.out.println("The average score for the reviews containing the word " + word + " is " + (totalScore / wordAppearance) + "\n");
		printMenu();
	} 

	// Get the average scores of each word in a txt file (Requirement #3)
	private static void getAverageScoreOfWord() throws FileNotFoundException {
		System.out.println("Please enter the name of the file with words you want to find the average score for: ");
		File reviewFile = new File(keyboard.nextLine());
		Scanner reviewScanner = new Scanner(reviewFile);

		double totalScore = 0.0;
		double numberOfLines = 0.0;

		while (reviewScanner.hasNext()) {
			String currentLine = reviewScanner.nextLine();
			totalScore += getWordSentimentScore(currentLine.toLowerCase());
			numberOfLines++;
		}
		reviewScanner.close();

		double averageScore = totalScore / numberOfLines;

		System.out.println("The average score of words in " + reviewFile + " is " + averageScore);
		System.out.println("The overall sentiment of " + reviewFile + " is " + sentiment(averageScore) + "\n");
		printMenu();
	}

	// Get the lowest and highest scoring words (Requirement #4)
	private static void getLowestAndHighestWords() throws FileNotFoundException {
		System.out.println("Enter the name of the file with the words you want to score: ");
		File reviewFile = new File(keyboard.nextLine());
		Scanner fileViewer = new Scanner(reviewFile);

		double highestScore = 0.0;
		String highestWord = "";

		double lowestScore = 4.0;
		String lowestWord = "";

		while (fileViewer.hasNextLine()) {
			Scanner nextWordScanner = new Scanner(fileViewer.nextLine());
			while (nextWordScanner.hasNext()) {
				String nextWord = nextWordScanner.next().toLowerCase();
				if (getWordSentimentScore(nextWord) > highestScore) {
					highestWord = nextWord;
					highestScore = getWordSentimentScore(nextWord);
				}
				if (getWordSentimentScore(nextWord) < lowestScore) {
					lowestWord = nextWord;
					lowestScore = getWordSentimentScore(nextWord);
				}
			}
			nextWordScanner.close();
		}
		fileViewer.close();
		System.out.println("The most positive word, with a score of " + highestScore + " is " + highestWord);
		System.out.println("The most negative word, with a score of " + lowestScore + " is " + lowestWord + "\n");
		printMenu();
	}

	// Export the positive and negative words into their own txt files (Requirement #5)
	private static void exportLowestAndHighestWords() throws FileNotFoundException {
		System.out.println("Please select a source file to retrieve words from.");
		File sourceFile = new File(keyboard.nextLine());
		Scanner fileReader = new Scanner(sourceFile);
		String goodWords = "";
		String badWords = "";


		// Read the file and seperate the bad and good words.
		while (fileReader.hasNextLine()) {
			Scanner nextWordScanner = new Scanner(fileReader.nextLine());
			while (nextWordScanner.hasNext()) {
				String nextWord = nextWordScanner.next().toLowerCase();
				if (getWordSentimentScore(nextWord) > 2.1)
					goodWords = goodWords + nextWord + ",";
				else if (getWordSentimentScore(nextWord) < 1.9)
					badWords = badWords + nextWord + ",";
			}
			nextWordScanner.close();
		}
		fileReader.close();


		// Create new txt files and write the good/bad words in them.
		PrintWriter goodWordWriter = new PrintWriter("positive.txt");
		String[] splitGoodWords = goodWords.split(",");
		for (int i = 0; i < splitGoodWords.length; i++)
			goodWordWriter.println(splitGoodWords[i]);
		goodWordWriter.close();

		PrintWriter badWordWriter = new PrintWriter("negative.txt");
		String[] splitBadWords = badWords.split(",");
		for (int i = 0; i < splitBadWords.length; i++)
			badWordWriter.println(splitBadWords[i]);
		badWordWriter.close();

		System.out.print("Finished!\n");
		printMenu();
	}

	// Menu with enhancements (Requirement #2 and #6)
	private static void printMenu() throws FileNotFoundException {
		while (true) {
			System.out.println("Please choose one of the options below.");
			System.out.println("[1] Get the score of a word.");
			System.out.println("[2] Get the average score of words in a file.");
			System.out.println("[3] Find highest and lowest words.");
			System.out.println("[4] Export good and bad words in a file.");
			System.out.println("[5] Exit the program.");

			String menuInput = keyboard.nextLine();

			if (menuInput.equals("1")) {
				getScoreOfWord();
				break;
			}
			else if (menuInput.equals("2")) {
				getAverageScoreOfWord();
				break;
			}
			else if (menuInput.equals("3")) {
				getLowestAndHighestWords();
				break;
			}
			else if (menuInput.equals("4")) {
				exportLowestAndHighestWords();
				break;
			}
			else if (menuInput.equals("5")) {
				System.out.println("Quitting application...");
				return;
			}
			System.out.println("'" + menuInput + "' is not a valid choice.");
		}
	}

	// Determine if score is a negative, positive, or neutral sentiment
	private static String sentiment(Double score) {
		if (score < 1.99)
			return "negative.";
		else if (score > 2.01)
			return "positive.";
		return "neutral.";
	}

	// Return the sentiment score for the word based using the string arrays.
	private static double getWordSentimentScore(String word) {
		if (BEST_WORDS.contains(word))
			return 4.0;
		if (GOOD_WORDS.contains(word))
			return 3.0;
		if (BAD_WORDS.contains(word))
			return 1.0;
		if (HORRIBLE_WORDS.contains(word))
			return 0.0;
		return 2.0; // Neutral
	}
}