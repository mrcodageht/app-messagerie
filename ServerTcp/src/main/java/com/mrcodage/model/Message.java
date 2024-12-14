package com.mrcodage.model;

import java.io.Serializable;

public record Message(String sender, String content) implements Serializable {
}
