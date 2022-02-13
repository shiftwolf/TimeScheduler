package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when the upload request could not be performed successfully
 */
public class UploadFailedException extends RuntimeException{
    public UploadFailedException(){super("Could not upload file, check file size and type");}
}
