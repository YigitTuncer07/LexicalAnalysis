import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class Main {

    static ArrayList<String> tokens = new ArrayList<>();
    static String currentLine;
    static int asciiCode;
    static char character;
    static int state = 0;
    static int start = 0;
    static int tempS = 0;
    static int keywordLength = 0;
    static int i = 0;
    static int j = 0;
    static boolean foundKeyword = false;

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(args[0]));// Initialize scanner using args

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();

            for (i = 0; i < currentLine.length(); i++) {
                asciiCode = (int) currentLine.charAt(i);
                character = currentLine.charAt(i);

                if (character == '~') {// This is a comment so the rest of the line is skipped
                    i = currentLine.length() - 1;

                } else if (character == '(') {
                    addToken("LEFTPAR", i);

                } else if (character == ')') {
                    addToken("RIGHTPAR", i);

                } else if (character == '[') {
                    addToken("LEFTSQUAREB", i);

                } else if (character == ']') {
                    addToken("RIGHTSQUAREB", i);

                } else if (character == '{') {
                    addToken("LEFTCURLYB", i);

                } else if (character == '}') {
                    addToken("RIGHTCURLYB", i);

                } else if (character == '"') {// This part reads until it finds another ", otherwise anounces an error.
                    start = i;// This is to remember where the string starts

                    if (i == currentLine.length() - 1) {// " cannot be at the end of the line
                        announceError("\"");
                        return;
                    }
                    do {// This loops until it finds a " or EOL

                        int prev = i;
                        i++;
                        character = currentLine.charAt(i);

                        // This part checks if the " has a / behind it
                        if (character == '\"') {
                            if (currentLine.charAt(prev) == '\\') {
                                if (!(i == currentLine.length() - 1)) {
                                    i++;
                                } else {
                                    announceError(currentLine.substring(start, i + 1));
                                    return;
                                }
                            }
                        }

                    } while (!((i == currentLine.length() - 1) || character == '"'));

                    if (character == '"') {// If the last char read isnt a ", anounce error
                        addToken("STRING", start);
                    } else {
                        announceError(currentLine.substring(start, i + 1));
                        return;
                    }
                } else if (character == '\'') {
                    start = i;

                    if (i == currentLine.length() - 1) { // " cannot be at the end of the line
                        announceError("\'");
                        return;
                    } else if (i == currentLine.length() - 2) {
                        announceError(currentLine.substring(i, i + 2));
                        return;
                    }

                    i++;
                    character = currentLine.charAt(i);

                    if (character == '\\') {
                        // i++;
                        // character = currentLine.charAt(i);

                        // if (character == '\''){
                        // addToken("CHAR", start);

                        // }
                    } else {
                        i++;
                        character = currentLine.charAt(i);

                        if (character == '\'') {
                            addToken("CHAR", start);

                        } else {
                            announceError(currentLine.substring(start, i));
                            return;
                        }
                    }
                } else if (character == 'd') {
                    start = i;
                    keywordLength = 6;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("define")) {// If it contains define this
                            System.out.println("contains define");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added define because of eol");
                                addToken("DEFINE", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added define because of tokenBreaker");
                                addToken("DEFINE", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }
                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();

                } else if (character == 'l') {
                    start = i;
                    keywordLength = 3;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("let")) {// If it contains define this
                            System.out.println("contains let");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added let because of eol");
                                addToken("LET", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added define because of tokenBreaker");
                                addToken("LET", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }
                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();

                } else if (character == 'c') {
                    start = i;
                    keywordLength = 4;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("cond")) {// If it contains define this
                            System.out.println("contains cond");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added cond because of eol");
                                addToken("COND", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added define because of tokenBreaker");
                                addToken("COND", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }
                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();
                } else if (character == 'i') {
                    start = i;
                    keywordLength = 2;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("if")) {// If it contains define this
                            System.out.println("contains if");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added if because of eol");
                                addToken("IF", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added if because of tokenBreaker");
                                addToken("IF", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }
                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();
                } else if (character == 'b') {
                    start = i;
                    keywordLength = 5;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("begin")) {// If it contains define this
                            System.out.println("contains begin");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added begin because of eol");
                                addToken("BEGIN", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added begin because of tokenBreaker");
                                addToken("BEGIN", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }

                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();
                } else if (character == 't') {
                    start = i;
                    keywordLength = 4;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("true")) {// If it contains define this
                            System.out.println("contains true");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added true because of eol");
                                addToken("TRUE", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added true because of tokenBreaker");
                                addToken("TRUE", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }

                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();
                } else if (character == 'f') {
                    start = i;
                    keywordLength = 5;
                    if (currentLine.length() - i > keywordLength - 1) {
                        System.out.println("long enough");
                        if (currentLine.substring(i, i + keywordLength).equals("false")) {// If it contains define this
                            System.out.println("contains false");
                            if ((i + keywordLength - 1) == currentLine.length() - 1) {
                                System.out.println("added false because of eol");
                                addToken("FALSE", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            } else if (isTokenBreaker(currentLine.charAt(i + keywordLength))) {
                                System.out.println("added false because of tokenBreaker");
                                addToken("FALSE", start);
                                foundKeyword = true;
                                i = i + keywordLength - 1;
                            }
                        }
                    }

                    if (currentLine.length() - 1 == i) {// This prevents error from single characters
                        addToken("IDENTIFIER", start);
                        foundKeyword = true;
                    }
                    if (!foundKeyword) {
                        do {

                            i++;
                            character = currentLine.charAt(i);
                            System.out.print(character);

                        } while (isRestOfIdentifier(character) && !(i == currentLine.length() - 1));
                        System.out.println();

                        if (i == currentLine.length() - 1) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                        } else if (isTokenBreaker(character)) {
                            addToken("IDENTIFIER", start);
                            System.out.println("starting loop again at " + i);
                            i--;
                        } else {
                            announceError(currentLine.substring(start, i));
                            return;

                        }

                    }

                    foundKeyword = false;

                    System.out.println();
                }
            
            }


            //END OF IF STATEMENTS ---------------------------------------------------
            j++;
            // Prints the token if the program didnt stop due to errors.
            printArrayList();

        }
    }

    public static boolean isStartOfIdentifier(char c){
        if (c == ' '){
            return false;
        }   

        if ((c == '!') || (c == '*') || (c == '/') || (c == ':') || (c == '<') || (c == '=') || (c == '>') || (c == '?')){
            return true;
        } else if (isLetter(c)){
            return true;
        }
        return false;
    }

    public static boolean isRestOfIdentifier(char c) {
        int i = (int) c;

        if (c == ' ') {
            return false;
        }
        if (isLetter(c)) {
            // System.out.println("ascii = " + i);
            // System.out.println("i am a letter, " + c);
            return true;
        } else if ((i >= 30) && (i <= 39)) {
            // System.out.println("ascii = " + i);
            // System.out.println("i am a number, " + c);
            return true;
        } else if ((c == '.') || (c == '+') || (c == '-')) {
            // System.out.println("i am " + c);
            return true;
        } else {
            // System.out.println("i am not part of an identifier --> " + c);
            return false;
        }
    }

    public static boolean isLetter(char c){
        int i = (int) c;
        if ((i >= 97) && (i <= 122)) {
            // System.out.println("ascii = " + i);
            // System.out.println("i am a letter, " + c);
            return true;
        }
        return false;
    }

    public static boolean isTokenBreaker(char c) {
        if ((c == '\'') || (c == '\"') || (c == '(') || (c == ')') || (c == '[') || (c == ']') || (c == '{')
                || (c == '}') || (c == ' ') || (c == '~')) {
            return true;
        }
        return false;

    }

    public static void addToken(String token, int index) {
        tokens.add(token + " " + (j + 1) + ":" + (index + 1));
    }

    public static void printArrayList() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("output.txt");
        for (String str : tokens) {
            writer.println(str);
        }
        writer.close();
    }

    public static void announceError(String lex) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("output.txt");
        writer.println("LEXICAL ERROR [" + (j + 1) + ":" + (i + 1) + "]: Invalid token `" + lex + "`");
        writer.close();
    }

}
