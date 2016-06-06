/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administratie;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public interface ILogin {
    
    public ArrayList<String> getRitbyName(String searchString) throws Exception;
    
}
