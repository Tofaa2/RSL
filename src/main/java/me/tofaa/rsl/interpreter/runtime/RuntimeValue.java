package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

// Lowest level representation of an interpreted value. This can be anything in the script.
public interface RuntimeValue {

    RSLInterpreterValueTypes type();

}
