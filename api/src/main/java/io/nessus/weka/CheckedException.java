package io.nessus.weka;

@SuppressWarnings("serial")
public class CheckedException extends RuntimeException {

    public static RuntimeException create(Throwable cause) {
        
    	if (cause instanceof RuntimeException) 
        	return (RuntimeException) cause;
    	
        return new CheckedException(cause);
    }
    
    public CheckedException(Throwable cause) {
        super(cause);
    }
}