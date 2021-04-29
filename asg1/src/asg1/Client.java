package asg1;

import dblib.Registration;

import java.io.PrintStream;
//import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) {

        // TODO code application logic here
        PrintStream cout = System.out;
        Scanner cin = new Scanner(System.in);
        cout.println("I, Nash Dean, certify that this project is my work only.  " +
                "I have not used source code from third parties (except those that were discussed in-class).");
        cout.println("Please enter your password");
        Registration db = null;
        int count = 0;
        String pass = cin.nextLine();
        db = new Registration("ism6236", pass);

        cout.println("Please enter your 'Year' and 'Semester'.");
        String year = cin.nextLine();
        String semester = cin.nextLine();

        List<String> l;

        do
        {
            l = db.List(semester, year);

            if (!l.isEmpty()) {
                System.out.printf("%10s | %10s | %10s | %10s | %10s\n", "CourseNo", "SectionNo", "Room", "Days", "Time");
                System.out.printf("%10s + %10s + %10s + %10s + %10s\n", "----------", "----------", "----------", "----------", "----------");

                for (int i = 0; i < l.size(); i++) {
                    String sec = l.get(i); //a[i];

                    System.out.println(String.format("%s", sec));
                }
            } else {
                cout.println("There are no sections in the database for the 'Year'/'Semester' you entered.");
                cout.println("Please try entering your 'Year' and 'Semester' again.");
                year = cin.nextLine();
                semester = cin.nextLine();
            }
        } while(l.isEmpty());

        cout.println();
        cout.print("Enter R to Register,  L to list classes , Q to quit");
        cout.flush();
        String input = cin.nextLine();
        boolean quit = false;
        boolean inputError = true;
        ArrayList<String> ord = new ArrayList<String>();
        while (!quit) {
            int c = input.charAt(0);
            switch (c) {
                case 'r':
                case 'R':

                    do {
                        int sno = 0;

                        cout.print("Enter Student No: ");
                        cout.flush();
                        try {
                            sno = Integer.parseInt(cin.nextLine());
                            inputError = false;
                        } catch (Exception e){
                            cout.println("Input mismatch Error; Enter an integer for Student No");
                        }
                        cout.print("Enter Course No: ");
                        String cid = cin.nextLine();
                        cout.print("Enter Section No: ");
                        cout.flush();
                        String sid = cin.nextLine();

                        int n = db.Register(sno, cid, sid);
//                    int n = db.Purchase(cno, ord);
                        //Update(accno, amt);
                        cout.println(String.format("%d records got updated\n", n));
                    } while(inputError);
                    inputError = true;
                    break;
                case 'L':
                case 'l':
                    int sno = 0;
                    cout.print("Enter Student No: ");
                    cout.flush();
                    try {
                        sno = Integer.parseInt(cin.nextLine());
                        inputError = false;
                    } catch (Exception e){
                        cout.println("Input mismatch Error; Must be an integer for Student No");
                    }

                    System.out.println("Enter Year: ");
                    year = cin.nextLine();


                    System.out.println("Enter Semester: ");
                    semester = cin.nextLine();

                   l = db.List(sno, semester, year);

                        if (!l.isEmpty()) {
                            System.out.printf("%10s | %10s | %10s | %10s | %10s\n", "CourseNo", "SectionNo", "Room", "Days", "Time");
                            System.out.printf("%10s + %10s + %10s + %10s + %10s\n", "----------", "----------", "----------", "----------", "----------");

                            for (int i = 0; i < l.size(); i++) {
                                String sec = l.get(i); //a[i];

                                System.out.println(String.format("%s", sec));
                            }
                        } else {
                            cout.println("You have entered a wrong Student No, Year, and/or Semester");
                        }

                    break;
                default:
                    quit = true;

            }

            if (!quit) {
                cout.print("Enter R to register,  L to list classes , Q to quit");
                cout.flush();
                input = cin.nextLine();
            }

        }
    }
}
