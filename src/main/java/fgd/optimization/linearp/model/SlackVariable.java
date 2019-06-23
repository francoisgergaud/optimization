package fgd.optimization.linearp.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents a slack-variable.
 */
@Getter
@ToString
public class SlackVariable implements Variable {

    private static Integer counter = 0;

    private String name;

    public SlackVariable(){
        this.name = "Slack_"+ counter++;
    }
}
