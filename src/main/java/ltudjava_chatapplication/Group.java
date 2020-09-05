/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author WIN-10
 */
public class Group implements Serializable{

      private static final long serialVersionUID = 1L;
 
    private String nameGroup;
    private Vector<String> groupList = new Vector<String>();

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public Vector<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(Vector<String> groupList) {
        this.groupList = groupList;
    }
    
    
    
}
