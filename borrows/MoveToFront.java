import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import java.util.LinkedList;

public class MoveToFront {
    private static final int R    = 256;
    private static final int LG_R = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> chars = initialSequence();

        while (!BinaryStdIn.isEmpty()) {
            char letter = BinaryStdIn.readChar();

            int index = chars.indexOf(letter);
            BinaryStdOut.write(index, LG_R);

            chars.remove(index);
            chars.addFirst(letter);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> chars = initialSequence();

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(LG_R);

            char letter = chars.get(index);
            BinaryStdOut.write(letter);

            chars.remove(index);
            chars.addFirst(letter);
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> initialSequence() {
        LinkedList<Character> result = new LinkedList<>();

        for (int i = 0; i < R; i++) {
            result.addLast((char) i);
        }

        return result;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String mode = args[0];

        if (mode.equals("-")) {
            encode();
        } else if (mode.equals("+")) {
            decode();
        }
    }

}