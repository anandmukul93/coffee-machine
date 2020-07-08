package entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

//contains a list of menuitems
@Getter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Menu {
    Set<MenuItem> menuItems;

    public void display(){
        System.out.println("Menu items : ");
        for(MenuItem item: menuItems){
            item.getDisplayName();
        }
    }

    public boolean hasMenuItem(MenuItem item) {
        return menuItems.contains(item);
    }
}
