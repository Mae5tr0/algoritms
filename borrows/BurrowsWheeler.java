import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;

import java.util.Arrays;

public class BurrowsWheeler {
    //extended ASCII
    private static final int R    = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();

        int resultPosition = 0;
        StringBuilder result = new StringBuilder();

        CircularSuffixArray suffixArray = new CircularSuffixArray(input);
        for (int i = 0; i < input.length(); i++) {
            int position = suffixArray.index(i);
            if (position == 0) resultPosition = i;

            result.append(charOnPosition(input, position, i));
        }

        BinaryStdOut.write(resultPosition);
        BinaryStdOut.write(result.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        char last[] = BinaryStdIn.readString().toCharArray();

        int next[] = new int[last.length];
        char first[] = last.clone();
        Arrays.sort(first);

        //constructing next
        int chars[] = new int[R];
        for (int i = 0; i < first.length; i++) {
            char firstChar = first[i];

            for (int j = chars[firstChar]; j < last.length; j++) {
                if (firstChar == last[j]) {
                    chars[firstChar] = j + 1;
                    next[i] = j;
                    break;
                }
            }
        }

        //Build decoded string
        StringBuilder result = new StringBuilder();
        int nextChar = start;
        for (int i = 0; i < next.length; i++) {
            result.append(first[nextChar]);
            nextChar = next[nextChar];
        }

        BinaryStdOut.write(result.toString());
        BinaryStdOut.close();
    }

    private static char charOnPosition(String input, int position, int i) {
        int charPosition = position - 1;
        if (charPosition < 0) charPosition = input.length() - 1;

        return input.charAt(charPosition);
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        String mode = args[0];

        if (mode.equals("-")) {
            transform();
        } else if (mode.equals("+")) {
            inverseTransform();
        }
    }
}
