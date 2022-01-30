package com.example.scheduler.exceptions;

public class UploadFailedException extends RuntimeException{
    public UploadFailedException(){super("Could not upload file, check file size and type");}
}
