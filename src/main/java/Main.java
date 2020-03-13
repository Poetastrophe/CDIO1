import dal.IUserDAO;
import dal.UserDAONonPersistent;

//import dal.UserDAOSQL;
import dal.UserDAOSQL;
import func.Func;
import func.IFunc;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        //LOG IN FOR SQL LOCALHOST
        System.out.println("Type in your SQL username and password on a separate line");
        IUserDAO dao = new UserDAOSQL("localhost","3306","user_database",in.nextLine(),in.nextLine());
        //IUserDAO dao = new UserDAONonPersistent();
        IFunc func = new Func(dao);
        CLI cli = new CLI(func, in);
        cli.run();
    }
}
