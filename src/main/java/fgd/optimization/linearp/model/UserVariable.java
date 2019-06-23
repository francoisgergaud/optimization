package fgd.optimization.linearp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a user-variable (with a name).
 */
@AllArgsConstructor
@Getter
@ToString
public class UserVariable implements Variable{
    private String name;
}
