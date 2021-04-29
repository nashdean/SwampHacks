using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using C = System.Console;
using Client.RPServiceReference;

namespace RegistrationClient
{
    class Program
    {
        static void Main(string[] args)
        {
            RegisterClient rps = new RegisterClient();

            C.WriteLine("I, Nash Dean and Travis Dietz, certify that this project is my work only.  " +
                    "I have not used source code from third parties (except those that were discussed in-class).");
            

        C.WriteLine("Please enter your 'Year' and 'Semester'.");
        String year = C.ReadLine();
        String semester = C.ReadLine();

        String[] l;

            do
            {
                l = rps.ListAvailable(semester, year);

                if (l.Any())
                {
                    C.WriteLine(String.Format("{0,10} | {1,10} | {2,10} | {3,10} | {4,10}", "CourseNo", "SectionNo", "Room", "Days", "Time"));
                    C.WriteLine(String.Format("{0,10} | {1,10} | {2,10} | {3,10} | {4,10}", "----------", "----------", "----------", "----------", "----------"));

                    for (int i = 0; i<l.Length; i++)
                    {
                        String sec = l[i]; //a[i];

        C.WriteLine(String.Format("{0}", sec));
                    }
}
                else
{
    C.WriteLine("There are no sections in the database for the 'Year'/'Semester' you entered.");
    C.WriteLine("Please try entering your 'Year' and 'Semester' again.");
    year = C.ReadLine();
    semester = C.ReadLine();
}
            } while (!l.Any()) ;


C.WriteLine();
C.WriteLine("Enter R to Register,  L to list classes , Q to quit");

String input = C.ReadLine();
bool quit = false;
bool inputError = true;
List<String> ord = new List<String>();
while (!quit)
{
    int c = input[0];
    int sno = 0;
    switch (c)
    {
        case 'r':
        case 'R':

            do
            {


                C.WriteLine("Enter Student No: ");

                try
                {
                    sno = Int32.Parse(C.ReadLine());
                    inputError = false;
                }
                catch (Exception e)
                {
                    C.WriteLine("Input mismatch Error; Enter an integer for Student No");
                }
                C.WriteLine("Enter Course No: ");
                String cid = C.ReadLine();
                C.WriteLine("Enter Section No: ");

                String sid = C.ReadLine();

                int n = rps.Register(sno, cid, sid);

                if (n == 2)
                {
                    C.WriteLine("Student {0} successfully registered.", sno);
                    C.WriteLine(String.Format("{0} records got updated", n));
                }
                else if (n == -100)
                    C.WriteLine("The course {0} section {1} is at Capacity.  Cannot register Student {2}", cid, sid, sno);
                else if (n == -200)
                    C.WriteLine("The Student No you have entered is not a student in the registry");
                else if (n == -300)
                    C.WriteLine("Student {0} is already registered.  Cannot double register Student {1}", sno, sno);
                else if (n == -1)
                    C.WriteLine("SQL EXCEPTION: Transaction Commit Failed.  Rollback Successful.");
                

            } while (inputError);
            inputError = true;
            break;
        case 'L':
        case 'l':

            C.WriteLine("Enter Student No: ");

            try
            {
                sno = Int32.Parse(C.ReadLine());
                inputError = false;
            }
            catch (Exception e)
            {
                C.WriteLine("Input mismatch Error; Must be an integer for Student No");
            }

            Console.WriteLine("Enter Year: ");
            year = C.ReadLine();


            Console.WriteLine("Enter Semester: ");
            semester = C.ReadLine();

            l = rps.ListRegistered(sno, semester, year);

            if (l.Any())
            {
                C.WriteLine("{0,10} | {1,10} | {2,10} | {3,10} | {4,10}", "CourseNo", "SectionNo", "Room", "Days", "Time");
                C.WriteLine("{0,10} | {1,10} | {2,10} | {3,10} | {4,10}", "----------", "----------", "----------", "----------", "----------");

                for (int i = 0; i < l.Length; i++)
                {
                    String sec = l[i]; //a[i];

                    Console.WriteLine(String.Format("{0}", sec));
                }
            }
            else
            {
                C.WriteLine("You have entered a wrong Student No, Year, and/or Semester");
            }

            break;
        default:
            quit = true;
            break;

    }

    if (!quit)
    {
        C.WriteLine("Enter R to register,  L to list classes , Q to quit");

        input = C.ReadLine();
    }
}
            }
        }
    }

