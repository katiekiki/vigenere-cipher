package exercise.vigenere;


import java.io.IOException;

public class App {

    public static final String CIPHER_CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz \t\n\r~!@#$%^&*()_+-=[]\\{}|;':\",./<>?";


    public static void main(String args[]) throws IOException {

        if (args.length != 3) {
            System.out.println("Exact 3 parameters required - [action] [key] [target]");
            System.exit(1);
        }

        String action, key, target;
        action = args[0];
        key = args[1];
        target = args[2];

        if ("encrypt".equalsIgnoreCase(action)) {
            System.out.println("encrypt [" + key + "], [" + target + "]");
            VigenereCipher vigenereCipher = new VigenereCipher(CIPHER_CHAR_SET,key);
            System.out.println("output [" + vigenereCipher.encrypt(target) + "]");
        } else if ("decrypt".equalsIgnoreCase(action)) {
            System.out.println("decrypt [" + key + "], [" + target + "]");
            VigenereCipher vigenereCipher = new VigenereCipher(CIPHER_CHAR_SET,key);
            System.out.println("output [" + vigenereCipher.decrypt(target) + "]");
        } else if ("encryptDir".equalsIgnoreCase(action)) {
            System.out.println("encryptDir [" + key + "], [" + target + "]");
            VigenereFileCipher vigenereFileCipher = new VigenereFileCipher(CIPHER_CHAR_SET,key,target, "txt");
            vigenereFileCipher.encryptFiles();
        } else if ("decryptDir".equalsIgnoreCase(action)) {
            System.out.println("decryptDir [" + key + "], [" + target + "]");
            VigenereFileCipher vigenereFileCipher = new VigenereFileCipher(CIPHER_CHAR_SET,key,target, "txt");
            vigenereFileCipher.decryptFiles();
        } else {
            System.out.println("action [" + action + "] not implemented");
        }

    }

}