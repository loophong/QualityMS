package io.renren.modules.publicmanagement.controller;

import lombok.Data;

@Data
public class ResMessage {
    public ResMessage(String numberId, int state) {
        this.numberId = numberId;
        this.state = state;
    }

    String numberId;
    int state;

}
