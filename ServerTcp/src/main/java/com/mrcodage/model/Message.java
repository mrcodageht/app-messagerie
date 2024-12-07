package com.mrcodage.model;

import java.io.Serializable;

public record Message(String to, String content) implements Serializable {
}
