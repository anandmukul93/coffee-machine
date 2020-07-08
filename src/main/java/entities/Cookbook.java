package entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

//contains a map of menuitem to recipes
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cookbook {
    Map<MenuItem, Recipe> recipes;
}
