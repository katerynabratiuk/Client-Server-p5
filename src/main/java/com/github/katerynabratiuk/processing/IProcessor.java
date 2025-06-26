package com.github.katerynabratiuk.processing;

import com.github.katerynabratiuk.command.CommandMessage;
import com.github.katerynabratiuk.domain.Message;

public interface IProcessor {

    void process(Message message);
}
