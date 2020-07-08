package entities;

import lombok.Getter;

@Getter
public enum MenuItem {
    GINGER_TEA("GINGER TEA"),
    BLACK_TEA("BLACK TEA"),
    HOT_MILK("HOT MILK"),
    HOT_WATER("HOT WATER"),
    HOT_TEA("HOT TEA"),
    BLACK_COFFEE("BLACK COFFEE"),
    GREEN_TEA("GREEN TEA"),
    HOT_COFFEE("HOT COFFEE");
    private String displayName;

    MenuItem(String name){
        this.displayName = name;
    }
}
