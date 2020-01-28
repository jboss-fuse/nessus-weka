package io.nessus.weka;

@SuppressWarnings("serial")
public class UncheckedException extends RuntimeException {

    public static RuntimeException create(Throwable cause) {
        
    	if (cause instanceof RuntimeException) 
        	return (RuntimeException) cause;
    	
        return new UncheckedException(cause);
    }
    
    private UncheckedException(Throwable cause) {
        super(cause);
    }
}