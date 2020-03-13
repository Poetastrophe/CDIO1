package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDTO implements Serializable{

  private static final long serialVersionUID = 4545864587995944260L;
  private int	userId;
  private String userName = "null";
  private String ini = "null";
  private List<String> roles = new ArrayList<>(Arrays.asList("null"));
  private String cpr = "null";
  private String password = "null";


  public UserDTO() {
    this.roles = new ArrayList<>();
    this.password = newPassword();

  }

  public UserDTO(int userId, String userName, String ini, String CPR, String password, String role){
    this.roles = new ArrayList<>();
    this.userId = userId;
    this.userName = userName;
    this.ini = ini;
    this.cpr = CPR;
    this.password = password;
    roles.add(role);


  }

  public String getCpr() {
    return cpr;
  }

  public String getPassword() {
    return password;
  }

  public void setUserCpr(String Cpr){ this.cpr = Cpr;}
  public String getUserCpr(){return cpr;}

  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public void setPassword(String pass){this.password = pass;}

  public String getIni() {
    return ini;
  }
  public void setIni(String ini) {
    this.ini = ini;
  }

  public List<String> getRoles() {
    return roles;
  }
  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public void addRole(String role){
    this.roles.add(role);
  }
  /**
   *
   * @param role
   * @return true if role existed, false if not
   */
  public boolean removeRole(String role){
    return this.roles.remove(role);
  }

  @Override
  public String toString() {
    return "UserDTO [userId=" + userId + ", userName=" + userName + ", ini=" + ini + ", roles=" + roles + "]";
  }

  public  String newPassword(){
    int min = 6;
    int max = 50;
    int len =(int) (Math.random()*(max - min)+ min);
    ArrayList<Character> pass = new ArrayList<Character>();

    char[] special = {'.', '-', '_', '+', '!', '?', '='};
    int x=0;
    for(int i = 0; i < len; i++){
      if(x>2)
        x=0;
      if(x == 0)
        pass.add((char) (Math.random()*(90 - 65)+65));
      else if(x == 1)
        pass.add((char) (Math.random()*(122 - 97) + 97));
      else if(x == 2)
        pass.add(special[(int)(Math.random()*(special.length))]);
      x++;
    }
    Collections.shuffle(pass);
    StringBuilder res = new StringBuilder();
    for(int i =0; i<len; i++ ){
      res.append(pass.get(i));
    }
    return res.toString();
  }


}
