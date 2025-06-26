package com.github.katerynabratiuk.command;

import com.github.katerynabratiuk.domain.Message;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandMessage {
    private final CommandType type;
    private final String[] arguments;

    public CommandMessage(CommandType type, String... arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public CommandMessage(Message message)
    {
        String[] commands = message.getMessage().split("/");
        type = CommandType.valueOf(commands[0]);
        arguments = Arrays.copyOfRange(commands, 1, commands.length);
    }

    public CommandType getType() {
        return type;
    }

    public String[] getArguments() {
        return arguments;
    }

}
