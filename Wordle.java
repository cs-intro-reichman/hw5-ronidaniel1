public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In in = new In(filename);
        return in.readAllStrings(); // 
    }

    // Choose a random secret word from the dictionary.
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
        int randomIndex = (int) (Math.random() * dict.length); // 
        return dict[randomIndex];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            if (c == secret.charAt(i)) {
                resultRow[i] = 'G'; // Green: correct letter in correct position 
            } else if (containsChar(secret, c)) {
                resultRow[i] = 'Y'; // Yellow: letter is in secret but different position [cite: 40, 66]
            } else {
                resultRow[i] = '_'; // Gray: letter does not appear in secret 
            }
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    public static void storeGuess(String guess, char[][] guesses, int row) {
        for (int i = 0; i < 5; i++) {
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G') {
                return false;
            }
        }
        return true; // [cite: 83]
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;

        // Read dictionary
        String[] dict = readDictionary("dictionary.txt"); // [cite: 69]

        // Choose secret word
        String secret = chooseSecretWord(dict); // [cite: 70]

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH]; // 
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH]; // 

        // Prepare to read from the standard input
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString(); // Read input using In class [cite: 76]

                if (guess.length() != 5) { // Check validity: exactly 5 letters [cite: 32, 77]
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]); // [cite: 91]

            // Print board
            printBoard(guesses, results, attempt); // [cite: 81]

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts."); // [cite: 127]
                won = true;
            }

            attempt++;
        }

        if (!won) {
            // Print the loss message exactly as shown in the example
            System.out.println("Sorry, you did not guess the word.");
            System.out.println("The secret word was: " + secret); // 
        }

        inp.close(); // Good practice to close resources, though not explicitly demanded in skeleton
    }
}