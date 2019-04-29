package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class QuestionCreator {

    private static String getIdentification(){
        int random = (int)(Math.random()* 1024 + 1);
        return String.valueOf(random);
    }

    public static String createQuestion() throws IOException {
        StringBuilder question = new StringBuilder();
        question.append(getIdentification());
        question.append("#0#00#");

        System.out.print("Enter question count: ");
        Scanner in = new Scanner(System.in);
        int questionCount = in.nextInt();
        question.append(String.valueOf(questionCount));

        int i = questionCount;
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        while (i > 0) {
            question.append("#");
            System.out.print("Enter domain name: ");
            String domainName = br.readLine();
            question.append(domainName);
            question.append("#");

            System.out.print("Enter record type: ");
            String recordType = br.readLine();
            question.append(recordType);
            question.append("#");

            System.out.print("Enter record class (IN): ");
            String recordClass = br.readLine();

            if (recordClass.equals(""))
                recordClass = "IN";
            question.append(recordClass);

            i--;
        }
        question.append("!");
        return question.toString();
    }
}
