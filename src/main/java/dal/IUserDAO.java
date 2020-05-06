package dal;
import java.util.List;

import dto.UserDTO;

public interface IUserDAO {

  UserDTO getUser(int userId) throws DALException;
  List<UserDTO> getUserList() throws DALException;
  void createUser(UserDTO user) throws UserFormatException, DALException;
  void updateUser(UserDTO user) throws UserFormatException, DALException;
  void deleteUser(int userId) throws DALException;

  class DALException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 7355418246336739229L;

    public DALException(String msg, Throwable e) {
      super(msg,e);
    }

    public DALException(String msg) {
      super(msg);
    }

  }
  public static class RoleNames{
   public static  final String ADMIN = "Administrator";
   public static  final String FORMAND = "Formand";
   public static  final String FARMACEUT = "Farmaceut";
   public static  final String OPERATOR = "Operatør";


  }

  public static class UserFormatException extends Exception{
    public List<errortypes> errorlist;
    UserFormatException(String message, List<errortypes> errorlist){
      super(message);
      this.errorlist = errorlist;
    }
    public enum errortypes{
      ID,
      username,
      CPR,
      roles,
      password
    }
  }
}
