package com.mrcodage.model;

import java.io.Serializable;

public record UserToConnect(String username, String password) implements Serializable {
}
