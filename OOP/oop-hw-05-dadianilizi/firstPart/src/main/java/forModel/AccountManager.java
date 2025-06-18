package forModel;

import java.util.HashMap;

public class  AccountManager {

    private HashMap<String ,String > accManager;

    public  AccountManager(){
        accManager=new HashMap<>();
        accManager.put("Patrick","1234");
        accManager.put("Molly","FloPup");
    }

    public boolean checkAccount(String acc){
        if(acc==null ) return  false;
        return  accManager.containsKey(acc);
    }

    public  boolean checkPassword(String acc,String password){
        if(acc==null || password==null) return  false;
        if(checkAccount(acc)){
            return accManager.get(acc).equals(password);
        } else{
            return  false;
        }

    }

    public void createNewAccount(String acc, String password){
        if(!checkAccount(acc)){
            accManager.put(acc,password);
        }
    }

}
