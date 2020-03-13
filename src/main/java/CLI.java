import dal.IUserDAO;
//import dal.UserDAOSQL;
import dto.UserDTO;
import func.IFunc;

import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CLI{
    final IFunc func;
    final Scanner in;

    public CLI(IFunc func, Scanner scanner){
        this.func = func;
        this.in = scanner;
    }

    // Starts the commandline program
    void run() throws InterruptedException{
        mainMenu0();
    }

    /**
     * Start screen, main menu.
     * @throws InterruptedException
     */
    void mainMenu0() throws InterruptedException{
        int input = -1;
        while(input == -1){
            System.out.println(screen0Mainmenu());
            System.out.println(promptInput());
            input = getInput(Arrays.asList(1,2,3,4,5));
            if(input == -1){
                System.out.println("Ikke korrekt input, prøv igen.");
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch(InterruptedException e){
                    System.out.println(e);
                }
                continue;
            }
        }
        if(input == 1){
            createUser1();
        }else if(input == 2){
            listUsers2();
        }else if(input == 3){
            updateUser3();
        }else if(input == 4){
            deleteUser4();
        }else if(input == 5){
            System.out.println("Lukker ned...");
            TimeUnit.SECONDS.sleep(1);
            return;
        }
        mainMenu0();
    }

    String screen0Mainmenu(){
        StringBuilder acc = new StringBuilder();
        acc.append("1) Opret bruger \n");
        acc.append("2) List brugere \n");
        acc.append("3) Opdater bruger \n");
        acc.append("4) Slet bruger \n");
        acc.append("5) Afslut program \n");
        return acc.toString();
    }

    /**
     * Option 1 from mainmenu: create user
     * @throws InterruptedException
     */
    void createUser1() throws InterruptedException {
        int id = -1;
        String input;

        System.out.println("Indtast unikt ID (11-99) (skriv STOP for at afbryde): ");
        while(id == -1){
            input = in.nextLine().trim();
            if(input.equals("STOP")){
                return;
            }
            try{
                id = Integer.parseInt(input);
            }catch(NumberFormatException e){
                System.out.println("Ikke korrekt input, prøv igen: ");
                continue;
            }
        }


        System.out.println("Indtast brugernavn (skriv STOP for at afbryde): ");
        input = in.nextLine().trim();
        if(input.equals("STOP")){
            return;
        }
        String userName = input;

        System.out.println("Indtast CPR (skriv STOP for at afbryde): ");
        input = in.nextLine().trim();
        if(input.equals("STOP")){
            return;
        }
        String cpr = input;

        // Assign roles
        int choice = -1;
        boolean admin=false;
        boolean farma=false;
        boolean formand=false;
        boolean operator=false;
        while(choice != 5){
            choice = -1;
            System.out.println("1) Administratør" + " " +isSelected(admin));
            System.out.println("2) Farmaceut" + " " + isSelected(farma));
            System.out.println("3) Formand " + " " +  isSelected(formand));
            System.out.println("4) Operatør" + " " +  isSelected(operator));
            System.out.println("5) Gem rolletildeling og opret bruger");
            choice = getInput(Arrays.asList(1,2,3,4,5));
            if(choice == -1){
                System.out.println("Ikke korrekt input, prøv igen: ");
                continue;
            }
            if(choice == 1){
                admin=!admin;
            }else if(choice == 2){
                farma=!farma;
            }else if(choice == 3){
                formand = !formand;
            }else if(choice == 4){
                operator = !operator;
            }
        }
        List<String> roles = new ArrayList<>();
        if(admin){
            roles.add("Administrator");
        }
        if(farma){
            roles.add("Farmaceut");
        }
        if(formand){
            roles.add("Formand");
        }
        if(operator){
            roles.add("Operatør");
        }
        try {
            func.createUser(id, userName, cpr, roles);
        }catch(IFunc.UserFormatException e){
            System.out.println("En eller flere parametre er ukorrekt formateret:");
            if(e.errorlist.contains(IFunc.UserFormatException.errortypes.CPR)){
                System.out.println("- CPR er ukorrekt formateret");
            }
            if(e.errorlist.contains(IFunc.UserFormatException.errortypes.ID)){
                System.out.println("- ID er ukorrekt formateret");
            }
            if(e.errorlist.contains(IFunc.UserFormatException.errortypes.roles)){
                System.out.println("- Rolleangivelse er ukorrekt formateret");
            }
            if(e.errorlist.contains(IFunc.UserFormatException.errortypes.username)){
                System.out.println("- Brugernavn er ukorrekt formateret");
            }
            if(e.errorlist.contains(IFunc.UserFormatException.errortypes.password)){
                System.out.println("- Kodeord er ukorrekt formateret");
            }
            TimeUnit.SECONDS.sleep(2);
        }catch(IFunc.DatabaseException e){
            System.out.println("Enten er der ikke en database, eller brugeren er identisk med et andet element i databasen. Prøv igen");
            TimeUnit.SECONDS.sleep(1);
        }

    }

    String isSelected(boolean b){
        if(b){
            return "✓";
        }else{
            return "x";
        }
    }


    //Prints table along the lines of:
    //+-------+------+------+
    //| att1  | att2 | att3 |
    //+-------+------+------+

    /**
     * Option 2 from main menu: List users
     */
    void listUsers2(){
        printTable(Arrays.asList("ID", "Username","Initials","CPR", "Kodeord"), getUserRows());
    }

    /**
     * Given attributes, and subsequent tubles (rows) it will print a SQL-like table
     * @param attributes
     * @param rows
     */
    void printTable(List<String> attributes, List<List<String>> rows){
        if (attributes.size() == rows.get(0).size()){
            throw new AssertionError("The number of attributes should always be equal to the row size");
        }
        List<Integer> attWidth = new ArrayList<>();
        for(int i = 0; i<attributes.size(); ++i){
            attWidth.add(attributes.get(i).length());
        }
        for(int r = 0; r<rows.size();++r){
            for(int elem = 0; elem<attWidth.size(); ++elem){
                if(attWidth.get(elem)<rows.get(r).get(elem).length()){
                    attWidth.set(elem, rows.get(r).get(elem).length());
                }
            }
        }
        StringBuilder firstLine = new StringBuilder();
        firstLine.append("+");
        for(int i = 0; i<attWidth.size(); ++i){
            StringBuilder tmp = new StringBuilder();
            for(int k = 0; k<attWidth.get(i)+2; k++){
                tmp.append("-");
            }
            firstLine.append(tmp);
            firstLine.append("+");
        }
        String seperator = firstLine.toString();

        StringBuilder attBuilder = new StringBuilder();
        attBuilder.append("| ");
        for(int i = 0; i<attributes.size(); ++i){
            StringBuilder spaces = new StringBuilder();
            for(int k = 0; k<attWidth.get(i)-attributes.get(i).length(); k++){
                spaces.append(" ");
            }
            attBuilder.append(attributes.get(i) + spaces + " | ");
        }

        System.out.println(seperator);
        System.out.println(attBuilder.toString());
        System.out.println(seperator);

        for (int i = 0; i < rows.size(); ++i) {
            StringBuilder rowBuilder = new StringBuilder();
            rowBuilder.append("| ");
            for(int att = 0; att<attributes.size(); ++att) {
                StringBuilder spaces = new StringBuilder();
                for (int k = 0; k < attWidth.get(att)-rows.get(i).get(att).length(); k++) {
                    spaces.append(" ");
                }
                rowBuilder.append(rows.get(i).get(att) + spaces + " | ");
            }
            System.out.println(rowBuilder.toString());
            System.out.println(seperator);
        }
    }

    List<List<String>> getUserRows(){
        List<UserDTO> list = new ArrayList<>();
        try {
            list = func.getUserList();
        }catch(IFunc.DatabaseException e){
            //TODO: better message
            System.out.println(e.getMessage());
        }
        List<List<String>> rows = new ArrayList<>();
        for(int i = 0; i<list.size(); ++i){
            List<String> tmp = new ArrayList<>();
            tmp.add(new Integer(list.get(i).getUserId()).toString());
            tmp.add(list.get(i).getUserName());
            tmp.add(list.get(i).getIni());
            tmp.add(list.get(i).getCpr());
            tmp.add(list.get(i).getPassword());
            StringBuilder acc = new StringBuilder();
            for(int j = 0; j<list.get(i).getRoles().size();++j){
                if(j==0){
                    acc.append(list.get(i).getRoles().get(j));
                }else{
                    acc.append(" og "+list.get(i).getRoles().get(j));
                }
            }
            tmp.add(acc.toString());
            rows.add(tmp);
        }
        return rows;
    }


    //TODO: This needs to be redone so that it actually follows our requirements given on the website Alexander.
    // This includes bringing it back to the Update page when a setting has been made,
    // and _not_ bring it to main menu. See createUser
    // It also needs to follow the correct output format.
    // It needs an "save and exit to main menu" button
    // Sincerely Christoffer

    /**
     * Option 3 from main menu
     */
    void updateUser3() {
        String useless;
        System.out.println("Indtast unikt id for bruger: ");
        int startID = in.nextInt();
        useless = in.nextLine();

        UserDTO user = null;
        try {
            user = user = func.getUser(startID);
        } catch (IFunc.DatabaseException e) {
            System.out.println("User findes ikke i databasen");
            e.printStackTrace();
        }
        try {

            //System.out.println("== Bruger med ændringer ==");
            String name = user.getUserName();
            String ini = user.getIni();
            String cpr = user.getCpr();
            String pass = user.getPassword();

            System.out.println("Unikt ID: " + startID);
            System.out.println("Brugernavn: " + name);
            System.out.println("Initialer: " + ini);
            System.out.println("CPR: " + cpr);
            System.out.println("Kodeord: " + pass);
            System.out.println("Roller: " + user.getRoles());

            int x = 0;
            while(x != 6){
                System.out.println("Hvilken værdi vil du ændre: ");
                System.out.println("1) ID");
                System.out.println("2) Navn");
                System.out.println("3) Cpr");
                System.out.println("4) Password");
                System.out.println("5) Role");
                System.out.println("6) Gem og afslut");
                System.out.println("7) Afslut uden at gemme");
                x = getInput(Arrays.asList(1, 2, 3, 4, 5, 6,7));
            switch (x) {
                case 1:
                    System.out.println("Indtast nyt ID:");
                    user.setUserId(in.nextInt());
                    in.nextLine();
                    break;
                case 2:
                    System.out.println("Indtast nyt navn:");
                    user.setUserName(in.nextLine());
                    break;
                case 3:
                    System.out.println("Indtast nyt cpr: ");
                    user.setUserCpr(in.nextLine());
                    break;
                case 4:
                    System.out.println("Indtast nyt Password: ");
                    user.setPassword(in.nextLine());
                    break;
                case 5:

                    int choice = -1;
                    boolean admin = false;
                    boolean farma = false;
                    boolean formand = false;
                    boolean operator = false;
                    while (choice != 5) {
                        choice = -1;
                        System.out.println("1) Administratør" + " " + isSelected(admin));
                        System.out.println("2) Farmaceut" + " " + isSelected(farma));
                        System.out.println("3) Formand " + " " + isSelected(formand));
                        System.out.println("4) Operatør" + " " + isSelected(operator));
                        System.out.println("5) Gem rolletildeling og opret bruger");
                        choice = getInput(Arrays.asList(1, 2, 3, 4, 5));
                        if (choice == -1) {
                            System.out.println("Ikke korrekt input, prøv igen: ");
                            continue;
                        }
                        if (choice == 1) {
                            admin = !admin;
                        } else if (choice == 2) {
                            farma = !farma;
                        } else if (choice == 3) {
                            formand = !formand;
                        } else if (choice == 4) {
                            operator = !operator;
                        }
                    }
                    List<String> roles = new ArrayList<>();
                    if (admin) {
                        roles.add("Administrator");
                    }
                    if (farma) {
                        roles.add("Farmaceut");
                    }
                    if (formand) {
                        roles.add("Formand");
                    }
                    if (operator) {
                        roles.add("Operatør");
                    }
                    user.setRoles(roles);
                    break;
                case 6:
                    break;
                case 7:
                    return;

                default:
                    System.out.println("Fejl i Switch");
            }
            }
            System.out.println("Skriv JA (med stort) hvis du er sikker (alt andet vil afbryde handlingen): ");
            if(in.nextLine().equals("JA")) {
                try {
                    func.deleteUser(startID);
                } catch (IFunc.DatabaseException e) {
                    System.out.println(e.getMessage());
                }

                try {
                    func.createUser(user.getUserId(), user.getUserName(), user.getCpr(), user.getRoles());
                } catch (IFunc.UserFormatException e) {
                    //TODO: Make better
                    System.out.println(e.getMessage());

                } catch (IFunc.DatabaseException e) {
                System.out.println(e.getMessage());
            }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //TODO: this needs to follow the website as well Alexander, more specifically to print the user in the
    // correct format as specificed by the website, see userStrFormat method
    // Sincerely Christoffer

    /**
     * Option 4 from main menu.
     */
    void deleteUser4() {
        System.out.println("Indtast user ID, som skal slettes: ");
        try {
            System.out.println("User: " + func.deleteUser(in.nextInt()) + " er slettet");
        }catch(IFunc.DatabaseException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    String userStrFormat(UserDTO user){
        StringBuilder acc = new StringBuilder();
        acc.append("Unikt ID: " + user.getUserId() + "\n");
        acc.append("Brugernavn: " + user.getUserName() + "\n");
        acc.append("Initialer: " + user.getIni() + "\n");
        acc.append("CPR: " + user.getUserCpr() + "\n");
        acc.append("Kodeord: " + user.getPassword() + "\n");
        acc.append("Roller: ");

        for(int i = 0; i<user.getRoles().size(); ++i){
            if ( i == 0){
                acc.append(user.getRoles().get(i));
            }else{
                acc.append(" og " +user.getRoles().get(i));
            }
        }
        return acc.toString();
    }

    String promptInput(){
        return "Indtast valgmulighed: ";
    }

    /**
     * Takes a list of valid choice, and will return -1 if
     * the user does not enter either a valid option or something that is not string-formatted
     * @param validChoices
     * @return
     */
    int getInput(List<Integer> validChoices){
        int choice = -1;
        try{
            choice = Integer.parseInt(in.nextLine());
        }catch(NumberFormatException e){
            return -1;
        }

        if(!validChoices.contains(choice)){
            return -1;
        }
        return choice;
    }


    String getSInput(List<String> validChoices){
        String choice = "";
        choice = in.nextLine();
        if(!validChoices.contains(choice)){
            return "";
        }
        return choice;
    }
}
