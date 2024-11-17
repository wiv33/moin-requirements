package com.moin.demomoin.application.port.in;

public record LoginCommand(
    String userId,
    String password
) {

}
