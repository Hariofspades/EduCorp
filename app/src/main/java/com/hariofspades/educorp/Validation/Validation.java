package com.hariofspades.educorp.Validation;

 
public interface Validation {

    String getErrorMessage();

    boolean isValid(String text);

}